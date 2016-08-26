CREATE OR REPLACE PACKAGE pkg_LTLF
as

	COPY_ERR_NO CONSTANT NUMBER := -20273;
	COPY_FAILURE EXCEPTION;
	PRAGMA EXCEPTION_INIT(COPY_FAILURE, -20273);

	GAP_ANALYSIS_ERR_NO CONSTANT NUMBER := -20423;
	GAP_ANALYSIS_FAILURE EXCEPTION;
	PRAGMA EXCEPTION_INIT(GAP_ANALYSIS_FAILURE, -20423);

	UNITIZE_ERR_NO CONSTANT NUMBER := -20283;
	UNITIZE_FAILURE EXCEPTION;
	PRAGMA EXCEPTION_INIT(UNITIZE_FAILURE, -20283);

   SUCCESS                    CONSTANT  NUMBER := 0;
   FAILURE                    CONSTANT  NUMBER := -1;
   INPROGRESS                 CONSTANT  VARCHAR2(16) := 'INPROGRESS';
   READY                      CONSTANT  VARCHAR2(16) := 'READY';
   FAILED                      CONSTANT  VARCHAR2(16) := 'FAILED';
   DEBUG                      CONSTANT  NUMBER := 0;
	DATE_FORMAT					CONSTANT	VARCHAR2(21) := 'YYYY-MM-DD HH24:MI:SS';
   VERSION_MAX                CONSTANT  NUMBER := 10;

	  TYPE datepoint_date_tabtype IS TABLE OF date INDEX BY BINARY_INTEGER;

	  TYPE datepoint_he_tabtype IS TABLE OF varchar2(3) INDEX BY BINARY_INTEGER;

   function LoadFull(pdStartDt in date,
                     pdEndDt in date,
					 psError out varchar2) return number;

	function CopyFromStaging(pnRowCount out number, psError out varchar2) return number;

	function CopyFromStaging(pnRowCount out number, psError out varchar2, i_mp_name in varchar2) return number;

	function GapAnalysis(psError out varchar2, i_mp_oid MEASUREMENT_POINT_DETAILS.MP_OID%type default NULL) return number;

	procedure LoadFull_proc(i_start_date in varchar2, i_end_date in varchar2);

	function Unitize(i_mp_oid MEASUREMENT_POINT_DETAILS.MP_OID%type default NULL, psError out varchar2) return number;

	procedure Unitize_Proc(i_start_date1 in varchar2, i_end_date1 in varchar2, i_start_date2 in varchar2, i_end_date2 in varchar2);

	procedure CopyMPFromTASMo;
end pkg_LTLF;
/
show errors;

create or replace package body pkg_LTLF as
---------------------  LOCAL VARIABLES BEGIN -----------------------
	LOG_LEVEL_ERROR constant number := 3;
	LOG_LEVEL_INFO constant number := 2;
	LOG_LEVEL_DEBUG constant number := 1;
	g_log_level number := LOG_LEVEL_ERROR;	-- default to error
---------------------  LOCAL VARIABLES END -----------------------

---------------------  LOCAL FUNCTIONS BEGIN -----------------------
	function getAnalysisEndDate(i_start_date in date) return date
	AS
	begin
		return add_months(i_start_date, 24);	-- returns the end date(exclusive) of a two year period
	end;

	procedure log(i_log_level number, i_error in error_log.error%type, i_module error_log.module%type default NULL, i_time error_log.time_dt%type default NULL)
	AS
		PRAGMA AUTONOMOUS_TRANSACTION;
		v_error error_log.error%type := i_error;
	begin
		if i_log_level < g_log_level then
			return;
		end if;

		if length(v_error) > 1024 then
			v_error := substr(v_error, 1, 1021) || '...';
		end if;

		if i_module is NULL then
			if i_time is NULL then
				insert into error_log (error) values (v_error);
			else
				insert into error_log (error, time_dt) values (v_error, i_time);
			end if;
		else
			if i_time is NULL then
				insert into error_log (error, module) values (v_error, i_module);
			else
				insert into error_log (error, module, time_dt) values (v_error, i_module, i_time);
			end if;
		end if;

		commit;
	exception
		when others then
			rollback;
	end;

	-- delete records from a table a small chunk at a time to avoid using large undo tablespace
	procedure ClearTable(i_table_name varchar2)
	AS
		v_records_to_delete number := 50000;
		v_sql varchar2(100);
	begin
		v_sql := 'delete from ' || i_table_name || ' where rownum < ' || to_char(v_records_to_delete + 1);
		loop
			execute immediate v_sql;
			commit;
			exit when SQL%ROWCOUNT < v_records_to_delete;
		end loop;
	end;

	procedure SetLogLevel
	as
		v_log_level number;
	begin
		select code_value_num into v_log_level from codes_t where code_type = 'LOG_LEVEL';
		g_log_level := v_log_level;
	exception
		when others then
			NULL;
	end;

	function Unitize_MP(pdStartDt1 in date,
		pdEndDt1 in date,
		pdStartDt2 in date,
		pdEndDt2 in date,
		i_mp_oid in MEASUREMENT_POINT_DETAILS.MP_OID%type,
		psError out varchar2) return number
	is
		v_count number;
		lsErrorMsg varchar2(255);
		lnLoadShapeKey number;
		lnLoadShapeSummKey number;
	begin
		delete from load_shape_details where mp_oid = i_mp_oid;
		select oid into lnLoadShapeKey from load_shape;
		delete from load_shape_summary where mp_oid = i_mp_oid;

		select count(*) into v_count from MEASUREMENT_POINT_DETAILS mp_id
		where MP_OID = i_mp_oid
			and EXPIRY_DATE IS NULL
			and measurement_point_exp > sysdate;
		if v_count < 1 then		-- expired
			return SUCCESS;
		end if;

		insert into load_shape_summary (mp_oid, status, load_shape_oid, version_num) values (i_mp_oid, 'No', lnLoadShapeKey, 1);
		select oid into lnLoadShapeSummKey from load_shape_summary where mp_oid = i_mp_oid;

		insert into load_shape_details(mp_oid,load_shape_summ_oid,base_year,base_day,base_hour_end,unit_value)
			(select t110.mp_oid,
				lnLoadShapeSummKey,
				TO_NUMBER(TO_CHAR(t110.cal_day_date,'YYYY')) as base_year,
				TO_NUMBER(TO_CHAR(t110.cal_day_date, 'DDD')) as base_day,	-- day of year
				t110.cal_hour_ending,
				case
					when t100.unit is null then
						t110.unit
					else
						((t100.unit * 50 / 100) + (t110.unit * (100 - 50) / 100))
				end avg_unit
			from
				(select t10.cal_day_date,
					t10.cal_hour_ending,
					t10.mp_oid,
					t10.sum_load_mw,
					t10.max_sum_load_mw,
					decode(t10.max_sum_load_mw, 0, 0, (t10.sum_load_mw / t10.max_sum_load_mw)) unit,
					rank_sum_load_mw
				from
					(select cal_day_date, cal_hour_ending, mp_oid,
						sum(MW) sum_load_mw,
						max(sum(MW)) over(partition by mp_oid) as max_sum_load_mw,
						row_number() over(partition by mp_oid order by sum(MW) desc) as rank_sum_load_mw
					from analysis_details_v
					where
						cal_day_date >= pdStartDt1
						and cal_day_date < pdEndDt1
						and mp_oid = i_mp_oid
					group by cal_day_date,cal_hour_ending, mp_oid) t10
				) t100,
				(select t10.cal_day_Date,
					t10.cal_hour_ending,
					t10.mp_oid,
					t10.sum_load_mw,
					t10.max_sum_load_mw,
					decode(t10.max_sum_load_mw, 0, 0, (t10.sum_load_mw / t10.max_sum_load_mw)) unit,
					rank_sum_load_mw
				from
					(select cal_day_date, cal_hour_ending, mp_oid,
						sum(MW) sum_load_mw,
						max(sum(MW)) over(partition by mp_oid) as max_sum_load_mw,
						row_number() over(partition by mp_oid order by sum(MW) desc) as rank_sum_load_mw
					from analysis_details_v
					where
						cal_day_date >= pdStartDt2
						and cal_day_date < pdEndDt2
						and mp_oid = i_mp_oid
					group by cal_day_date, cal_hour_ending, mp_oid) t10
				) t110
			where t110.rank_sum_load_mw = t100.rank_sum_load_mw(+)
				and t110.mp_oid = t100.mp_oid(+));

		if SQL%ROWCOUNT > 0 then
			update load_shape_summary set status = 'Yes' where mp_oid = i_mp_oid;
		end if;

		commit;

		return SUCCESS;
	EXCEPTION
		WHEN OTHERS THEN
			lsErrorMsg := to_char(sqlcode) || ': ' || sqlerrm;
			psError := lsErrorMsg;
			dbms_output.put_line(lsErrorMsg);
			rollback;
			log(LOG_LEVEL_ERROR, lsErrorMsg, 'Unitize_MP');

			return FAILURE;
	end;
---------------------  LOCAL FUNCTIONS END -----------------------

	function CopyFromStaging(pnRowCount out number, psError out varchar2, i_mp_name in varchar2) return number is
	/*
	 * This function copies data, for the specified MP, from staging table into
	 * analysis_details table.
	 * 
	 * During the copying process, it will map the staging.mp_name into analysis_details.mp_oid.
	 * If there is no match found, the record will not be copied into analysis_details.
	 * 
	 * Expired MP's will be copied for reference
	*/
		lsErrorMsg varchar2(256);
		ldStartDt date;
		ldEndDt date;
		v_two_year_period_end date;
		v_count number;
		v_mp_oid MEASUREMENT_POINT_DETAILS.MP_OID%TYPE;
	begin
  
		select count(oid) into v_count from analysis;
		if v_count = 0 then
			return SUCCESS;
		end if;

		-- get mp_name to mp_oid mapping
		begin
			select MP_OID into v_mp_oid from MEASUREMENT_POINT_DETAILS where MP_NAME = i_mp_name;
		exception
			when NO_DATA_FOUND then	-- no match found
				return SUCCESS;
		end;

	    select import_start_dt into ldStartDt from analysis;
	    select import_end_dt into ldEndDt from analysis;
		v_two_year_period_end := getAnalysisEndDate(ldStartDt);
		if v_two_year_period_end < ldEndDt then
			ldEndDt := v_two_year_period_end;
		end if;
    
		delete from analysis_details where mp_oid = v_mp_oid;

		-- copy to analysis_details and fill gaps with 0 within the analysis date range
		insert into analysis_details (mp_oid, cal_day_date, cal_hour_ending, mw)
			select staging_no_gap.mp_oid,
				staging_no_gap.cal_day_date,
				staging_no_gap.cal_hour_ending,
				nvl(staging.mw, 0) AS MW
			from
				staging,
				(select v_mp_oid as mp_oid, i_mp_name as mp_name, cal_day_date, cal_hour_ending from
			 		(select unique local_day AS cal_day_date,
			 			local_hr_ending AS cal_hour_ending
			       		from hub.application_time
			     	where ldStartDt<=local_day
						and local_day<ldEndDt) time_no_gap
				) staging_no_gap
			where staging_no_gap.mp_name = staging.mp_name(+)
				and staging_no_gap.cal_day_date = staging.cal_day_date(+)
				and staging_no_gap.cal_hour_ending = staging.cal_hour_ending(+);

		pnRowCount := SQL%ROWCOUNT;

		-- copy to analysis_details (NOT filling gaps) for records after the analysis end date
		insert into analysis_details (mp_oid, cal_day_date, cal_hour_ending, mw)
			select v_mp_oid, cal_day_date, cal_hour_ending, nvl(mw, 0)
			from staging
			where mp_name = i_mp_name and cal_day_date >= ldEndDt;

		pnRowCount := pnRowCount + SQL%ROWCOUNT;

		commit;

		return SUCCESS;
	EXCEPTION
		WHEN OTHERS THEN
			lsErrorMsg := 'MP_NAME: ' || i_mp_name || ' ' || to_char(sqlcode) || ': ' || sqlerrm;
			log(LOG_LEVEL_ERROR, lsErrorMsg, 'CopyFromStaging');
			dbms_output.put_line(lsErrorMsg);
			rollback;
			psError:=lsErrorMsg;
			return FAILURE;
	end;

	function CopyFromStaging(pnRowCount out number, psError out varchar2) return number is
	/*
	 * This function copies data from staging table into analysis_details table, MP by MP
	*/
		v_return_code number;
		lsErrorMsg varchar2(256);
		v_copy_count number;
		v_total_count number := 0;
	begin
		for v_mp_rec in (select distinct mp_name from staging)
		loop
			v_return_code := CopyFromStaging(v_copy_count, lsErrorMsg, v_mp_rec.mp_name);
			if v_return_code <> SUCCESS then
				raise_application_error(COPY_ERR_NO, lsErrorMsg);
			else
				v_total_count := v_total_count + v_copy_count;
			end if;
		end loop;
		pnRowCount := v_total_count;

		return SUCCESS;
	EXCEPTION
		when COPY_FAILURE then
			NULL;	-- error has been handled in the CopyFromStaging function
		WHEN OTHERS THEN
			lsErrorMsg := to_char(sqlcode) || ': ' || sqlerrm;
			log(LOG_LEVEL_ERROR, lsErrorMsg, 'CopyFromStaging - All');
			dbms_output.put_line(lsErrorMsg);
			psError:=lsErrorMsg;
			return FAILURE;
	end;

	function LoadFull(pdStartDt in date,
                     pdEndDt in date,
					 psError out varchar2) return number
	AS
		v_job_num binary_integer;
		lsErrorMsg varchar2(256);
	begin
		lsErrorMsg := '';

		-- check whether there is running job
		for v_job_rec in (select what from user_jobs where upper(what) like '%LOADFULL_PROC(%')
		loop
			psError := 'Loading job is running';
			return FAILURE;
		end loop;

		-- submit the loading job
		dbms_job.submit(v_job_num, 'pkg_LTLF.LoadFull_proc(''' || to_char(pdStartDt, DATE_FORMAT) || ''',''' || to_char(pdEndDt, DATE_FORMAT) || ''');');
		commit;

		return SUCCESS;
	exception
		when others then
			lsErrorMsg := to_char(sqlcode) || ': ' || sqlerrm;
			psError := lsErrorMsg;
			log(LOG_LEVEL_ERROR, lsErrorMsg, 'LoadFull');
			return FAILURE;
	end;

	procedure LoadFull_proc(i_start_date in varchar2, i_end_date in varchar2) is
/**************************************************************
   THIS WILL START A FRESH:
      * cleaning STAGING, analysis and analysis_details
	  * then it will update analysis for IMPORT_START_DT, IMPORT_END_DT, CREATED_DT and progress
	  * then it will insert into STAGING the subset of data from major star schema
	  * then it will update analysis for progress and number of records copied into staging
	  
	After copying data into staging table, it will copy data from staging table to
	analysis_details table and run gap analysis.
**************************************************************/
		v_loop_count number;
		v_counter number := 0;
		v_progress number := 0.0;
		v_start_dt date;
  		v_end_dt date;
		v_loop_start_dt date;
		v_loop_end_dt date;
		ldSys date;
		v_return_code number;
		liCount number;
		liCopyCount number;
		lsErrorMsg varchar2(256);
		v_temp_count number;
		v_days_per_loop number := 10.0;
   begin

		select sysdate into ldSys from dual;
		dbms_output.put_line('Starting: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));
		log(LOG_LEVEL_INFO, 'Start loading for ' || i_start_date || ' to ' || i_end_date, 'LoadFull_Proc');

		liCount :=0;
		lsErrorMsg:='';

		select to_date(i_start_date, DATE_FORMAT) into v_start_dt from dual;
		select to_date(i_end_date, DATE_FORMAT) into v_end_dt from dual;

		delete from ANALYSIS;
    	insert into ANALYSIS(OID,CREATED_DT, IMPORT_START_DT, IMPORT_END_DT, STATUS, PROGRESS, TOTAL_COUNT) values 
    		(LTLF_SEQ.NEXTVAL, sysdate, v_start_dt, v_end_dt, INPROGRESS, 0, 0);
		commit;
		log(LOG_LEVEL_DEBUG, 'Analysis table updated', 'LoadFull_Proc');

		execute immediate 'truncate table ANALYSIS_SUMMARY';
		log(LOG_LEVEL_DEBUG, 'Analysis_summary table cleared', 'LoadFull_Proc');
		execute immediate 'truncate table STAGING';
		log(LOG_LEVEL_DEBUG, 'Staging table cleared', 'LoadFull_Proc');
		execute immediate 'truncate table ANALYSIS_DETAILS';
		log(LOG_LEVEL_DEBUG, 'Analysis_details table cleared', 'LoadFull_Proc');

		-- joining with hub.dsm is extremely slow when the data volume is large
		-- avoid joining with hub.dsm where possible
		
		-- get count
		v_loop_count := ceil((v_end_dt - v_start_dt) / v_days_per_loop);		-- copy data for a certain amount of days at a time

		v_loop_end_dt := v_start_dt;
		log(LOG_LEVEL_DEBUG, 'Copying to staging table', 'LoadFull_Proc');
		loop
			v_loop_start_dt := v_loop_end_dt;
			v_loop_end_dt := v_loop_start_dt + v_days_per_loop;
			if v_loop_end_dt > v_end_dt then
				v_loop_end_dt := v_end_dt;
			end if;
  
			-- copy to staging
			insert into STAGING(MEASURE_POINT_ID, MP_NAME, CAL_DAY_DATE, CAL_HOUR_ENDING, MW)
				select
					m.measurement_point_id,
					m.measurement_point_name,
					t.local_day,
					t.local_hr_ending,
					sum(d.mwh)
				from
					hub.dsm d,
					hub.application_time t,
					hub.measurement_point m
				where
					d.record_type = 'LOD'
				    and m.measurement_point_id = d.measurement_point_id
					and m.IS_IN_TTL_GRID_LOAD = 'Y'
					and m.record_type = 'LOD'
					and v_loop_start_dt <= t.local_day
					and t.local_day < v_loop_end_dt
					and d.version_stop_date_time = to_date('9999-12-31','yyyy-mm-dd')
					and t.time_id = d.time_id
				group by
					m.measurement_point_id, m.measurement_point_name, t.local_day, t.local_hr_ending;

			v_temp_count := SQL%ROWCOUNT;
			liCount := liCount + v_temp_count;
	
			v_counter := v_counter + 1;
			v_progress := round(v_counter * 100 / v_loop_count, 1);
			if v_progress = 100 /*and v_counter <> v_loop_count*/ then
				-- set it to 99.9 when done copying to staging. set it to 100 later when done copying to analysis_details
				v_progress := 99.9;
			end if;

			-- update progress
	    	update ANALYSIS set PROGRESS = v_progress, STATUS = INPROGRESS, TOTAL_COUNT = liCount;
			
			commit;

			exit when v_loop_end_dt >= v_end_dt;
		end loop;

		if liCount > 0 then
			-- copy to analysis_details
			log(LOG_LEVEL_DEBUG, 'Copying to analysis_details table', 'LoadFull_Proc');
			v_return_code := CopyFromStaging(liCopyCount, lsErrorMsg);
			if v_return_code <> SUCCESS then
				raise_application_error(COPY_ERR_NO, lsErrorMsg);
			end if;
	
			-- run gap analysis for all MPs
			log(LOG_LEVEL_DEBUG, 'Running gap analysis', 'LoadFull_Proc');
			v_return_code := GapAnalysis(lsErrorMsg);
			if v_return_code <> SUCCESS then
				raise_application_error(GAP_ANALYSIS_ERR_NO, lsErrorMsg);
			end if;
		end if;
		
		-- update status and count
    	update ANALYSIS set PROGRESS = 100, STATUS = READY, TOTAL_COUNT = liCount;
		
		commit;

		select sysdate into ldSys from dual;
		dbms_output.put_line('     '||to_char(liCount)||' records have been processed');
		dbms_output.put_line('Ending: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));
		log(LOG_LEVEL_INFO, 'Ending', 'LoadFull_Proc');

	EXCEPTION
		WHEN OTHERS THEN
			lsErrorMsg := to_char(sqlcode) || ': ' || sqlerrm;
			log(LOG_LEVEL_ERROR, lsErrorMsg, 'LoadFull_Proc');
			dbms_output.put_line(lsErrorMsg);

			-- update status
	    	update ANALYSIS set STATUS = FAILED;
			commit;
			-- cleanup
			execute immediate 'truncate table STAGING';
			execute immediate 'truncate table ANALYSIS_DETAILS';
			execute immediate 'truncate table ANALYSIS_SUMMARY';
   end;

	function GapAnalysis(psError out varchar2, i_mp_oid MEASUREMENT_POINT_DETAILS.MP_OID%type default NULL) return number is
/**************************************************************
   THIS WILL go thru analysis_details records trying to identify GAPS
   RESULTS WILL bE LOADED INTO THE analysis_summary table.
   
   Expired MP's will be skiped
**************************************************************/
		ldStartDt date;
		ldEndDt date;
		lsErrorMsg varchar2(256);
		lnParentId number;
		v_two_year_period_end date;
		v_count number;
	begin

		select count(oid) into v_count from analysis;
		if v_count = 0 then
			return SUCCESS;
		end if;

		select import_start_dt into ldStartDt from analysis;
		select import_end_dt into ldEndDt from analysis;
		v_two_year_period_end := getAnalysisEndDate(ldStartDt);
		if v_two_year_period_end < ldEndDt then
			ldEndDt := v_two_year_period_end;
		end if;
		select oid into lnParentId from analysis;
  
		if i_mp_oid is null then
			-- re-run gap analysis for all mp_id's
			/* clear analysis_summary */
			delete from analysis_summary;
		
			insert into analysis_summary (analysis_oid, mp_oid, gap_count, status)
				select lnParentId, mp.mp_oid, nvl(gap.gap_count, 0), (CASE WHEN (nvl(gap.gap_count, 0) > 0) THEN 'Invalid' ELSE 'Valid' END)
				from
					(select distinct ad.mp_oid
					from
						analysis_details ad,
						MEASUREMENT_POINT_DETAILS mp_id
					where ad.mp_oid = mp_id.MP_OID
						and mp_id.EXPIRY_DATE IS NULL	-- active record. One MP_OID may link to multiple mp_name, only the one with EXPIRY_DATE IS NULL is active
						and mp_id.measurement_point_exp > sysdate) mp,
					(select mp_oid, count(*) AS gap_count
					from analysis_details_v
					where mw <= 0
						and ldStartDt <= cal_day_date
						and cal_day_date < ldEndDt
					group by mp_oid) gap
				where mp.mp_oid = gap.mp_oid(+);

			-- update load factors
			update analysis_summary summary set (prev_year_load_factor, base_year_load_factor) =  
				(select sum(factor1) factor1, sum(factor2) factor2 from
					(select mp_oid, decode(row_num, 1, load_factor, 0) factor1, decode(row_num, 2, load_factor, 0) factor2 from
						(select mp_oid,
							case sum(sum_load_mw) when 0 then 0 else round(100 * sum(sum_load_mw) / sum(max_sum_load_mw), 1) end as load_factor,
							row_number() over(partition by mp_oid order by to_char(cal_day_date, 'YYYY')) as row_num
						from
							(select cal_day_date, cal_hour_ending, mp_oid,
								sum(MW) sum_load_mw,
								max(sum(MW)) over(partition by mp_oid, to_char(cal_day_date, 'YYYY')) as max_sum_load_mw
							from analysis_details_v
							where
								cal_day_date >= ldStartDt
								and cal_day_date < ldEndDt
							group by cal_day_date,cal_hour_ending, mp_oid)
						group by mp_oid, to_char(cal_day_date, 'YYYY')
						)
					)
				where mp_oid = summary.mp_oid
				group by mp_oid);
		else
			-- gap analysis for single mp_id
			update analysis_summary set (mp_oid, gap_count, status) =
				(select mp.mp_oid, nvl(gap.gap_count, 0), (CASE WHEN (nvl(gap.gap_count, 0) > 0) THEN 'Invalid' ELSE 'Valid' END)
				from
					(select mp_id.MP_OID as mp_oid
					from
						MEASUREMENT_POINT_DETAILS mp_id
					where mp_id.EXPIRY_DATE IS NULL	-- active record. One MP_OID may link to multiple mp_name, only the one with EXPIRY_DATE IS NULL is active
						and mp_id.measurement_point_exp > sysdate
						and mp_id.MP_OID = i_mp_oid) mp,
					(select mp_oid, count(*) AS gap_count
					from analysis_details_v
					where mp_oid = i_mp_oid
						and mw <= 0
						and ldStartDt <= cal_day_date
						and cal_day_date < ldEndDt
					group by mp_oid) gap
				where mp.mp_oid = gap.mp_oid(+)
				)
			where mp_oid = i_mp_oid;

			-- update load factors
			update analysis_summary summary set (prev_year_load_factor, base_year_load_factor) =  
				(select sum(factor1) factor1, sum(factor2) factor2 from
					(select mp_oid, decode(row_num, 1, load_factor, 0) factor1, decode(row_num, 2, load_factor, 0) factor2 from
						(select mp_oid,
							case sum(sum_load_mw) when 0 then 0 else round(100 * sum(sum_load_mw) / sum(max_sum_load_mw), 1) end as load_factor,
							row_number() over(partition by mp_oid order by to_char(cal_day_date, 'YYYY')) as row_num
						from
							(select cal_day_date, cal_hour_ending, mp_oid,
								sum(MW) sum_load_mw,
								max(sum(MW)) over(partition by mp_oid, to_char(cal_day_date, 'YYYY')) as max_sum_load_mw
							from analysis_details_v
							where
								cal_day_date >= ldStartDt
								and cal_day_date < ldEndDt
								and mp_oid = i_mp_oid
							group by cal_day_date,cal_hour_ending, mp_oid)
						group by mp_oid, to_char(cal_day_date, 'YYYY')
						)
					)
				where mp_oid = summary.mp_oid
				group by mp_oid)
			where mp_oid = i_mp_oid;
		end if;

		commit;

		return SUCCESS;

	EXCEPTION
		WHEN OTHERS THEN
			lsErrorMsg := to_char(sqlcode) || ': ' || sqlerrm;
			log(LOG_LEVEL_ERROR, lsErrorMsg, 'GapAnalysis');
			dbms_output.put_line(lsErrorMsg);
			rollback;
			psError:=lsErrorMsg;
			return FAILURE;
	end;

	function Unitize(i_mp_oid in MEASUREMENT_POINT_DETAILS.MP_OID%type default NULL,
		psError out varchar2)
	return number
	is
		v_base_year codes_t.code_value_num%type;
		v_date1 varchar2(10);
		v_date2 varchar2(10);
		v_date3 varchar2(10);
		pdStartDt1 date;
		pdEndDt1 date;
		pdStartDt2 date;
		pdEndDt2 date;
		v_job_num binary_integer;
		v_return_code number;
		lsErrorMsg varchar2(255);
	begin
		-- check whether there is running job
		for v_job_rec in (select what from user_jobs where upper(what) like '%UNITIZE_PROC(%')
		loop
			psError := 'Unitizing job is running';
			return FAILURE;
		end loop;

		-- get date range
		select code_value_num into v_base_year from codes_t where code_type = 'BASE_YEAR';
		v_date1 := (v_base_year - 1) || '01-01';
		v_date2 := v_base_year || '01-01';
		v_date3 := (v_base_year + 1) || '01-01';
		select to_date(v_date1, DATE_FORMAT) into pdStartDt1 from dual;
		select to_date(v_date2, DATE_FORMAT) into pdEndDt1 from dual;
		select to_date(v_date2, DATE_FORMAT) into pdStartDt2 from dual;
		select to_date(v_date3, DATE_FORMAT) into pdEndDt2 from dual;

		-- submit the loading job
		if i_mp_oid is null then
			dbms_job.submit(v_job_num, 'pkg_LTLF.Unitize_Proc(''' || to_char(pdStartDt1, DATE_FORMAT) || ''',''' || to_char(pdEndDt1, DATE_FORMAT) || ''',''' || to_char(pdStartDt2, DATE_FORMAT) || ''',''' || to_char(pdEndDt2, DATE_FORMAT) || ''');');
			commit;
		else
			v_return_code := Unitize_MP(pdStartDt1, pdEndDt1, pdStartDt2, pdEndDt2, i_mp_oid, lsErrorMsg);
			if v_return_code <> SUCCESS then
				raise_application_error(UNITIZE_ERR_NO, lsErrorMsg);
			end if;
		end if;

		return SUCCESS;
	exception
		when others then
			lsErrorMsg := to_char(sqlcode) || ': ' || sqlerrm;
			psError := lsErrorMsg;
			log(LOG_LEVEL_ERROR, lsErrorMsg, 'Unitize');
			return FAILURE;
	end;

	procedure Unitize_Proc(i_start_date1 in varchar2,
		i_end_date1 in varchar2,
		i_start_date2 in varchar2,
		i_end_date2 in varchar2)
	is
		pdStartDt1 date;
		pdEndDt1 date;
		pdStartDt2 date;
		pdEndDt2 date;
		lsErrorMsg varchar2(255);
		lnParentKey number;
		v_loop_count number;
		v_counter number := 0;
		v_progress number := 0.0;
		v_return_code number;
	begin
		log(LOG_LEVEL_INFO, 'Start unitizing', 'Unitize_Proc');

		lsErrorMsg := '';

		-- get date range
		select to_date(i_start_date1, DATE_FORMAT) into pdStartDt1 from dual;
		select to_date(i_end_date1, DATE_FORMAT) into pdEndDt1 from dual;
		select to_date(i_start_date2, DATE_FORMAT) into pdStartDt2 from dual;
		select to_date(i_end_date2, DATE_FORMAT) into pdEndDt2 from dual;

		delete from load_shape;
		insert into load_shape(OID,BASE_YEAR, UNITIZE_DT, STATUS, PROGRESS) values
    		(LTLF_SEQ.NEXTVAL, TO_NUMBER(TO_CHAR(pdStartDt2,'YYYY')), sysdate, INPROGRESS, 0);
		commit;

		-- get count
		select count(distinct mp_oid) into v_loop_count from analysis_details;

		execute immediate 'truncate table load_shape_details';
		execute immediate 'truncate table load_shape_summary';

		for v_mp_rec in (select distinct mp_oid from analysis_details)
		loop
			log(LOG_LEVEL_DEBUG, 'Unitizing: MP_OID = ' || v_mp_rec.mp_oid, 'Unitize_Proc');
			v_return_code := Unitize_MP(pdStartDt1, pdEndDt1, pdStartDt2, pdEndDt2, v_mp_rec.mp_oid, lsErrorMsg);
			if v_return_code <> SUCCESS then
				raise_application_error(UNITIZE_ERR_NO, lsErrorMsg);
			end if;

			v_counter := v_counter + 1;
			v_progress := round(v_counter * 100 / v_loop_count, 1);
			if v_progress = 100 and v_counter <> v_loop_count then
				v_progress := 99.9;
			end if;

			-- update progress
	    	update load_shape set PROGRESS = v_progress, STATUS = INPROGRESS;
			commit;
		end loop;

		-- update status and count
    	update load_shape set PROGRESS = 100, STATUS = READY;
		commit;

		log(LOG_LEVEL_INFO, 'End unitizing', 'Unitize_Proc');
	EXCEPTION
		when UNITIZE_FAILURE then
			NULL;	-- error has been handled in the Unitize_MP function
		WHEN OTHERS THEN
			lsErrorMsg := to_char(sqlcode) || ': ' || sqlerrm;
			dbms_output.put_line(lsErrorMsg);
			log(LOG_LEVEL_ERROR, lsErrorMsg, 'Unitize_Proc');
			-- update status
	    	update load_shape set STATUS = FAILED;
			commit;
			-- cleanup
			execute immediate 'truncate table load_shape_details';
			execute immediate 'truncate table load_shape_summary';
	end;

	procedure CopyMPFromTASMo
	as
		v_now date;
		v_errorMsg varchar2(255);
	begin
		v_errorMsg := '';

		log(LOG_LEVEL_INFO, 'Start copying...', 'CopyMPFromTASMo');
		select sysdate into v_now from dual;

		-- populate MEASUREMENT_POINT_LTLF table with data from MEASUREMENT_POINT table in TASMo
		insert into MEASUREMENT_POINT (TAS_MEASURE_POINT_ID, CREATED_DATE)
			select new_mp.*, v_now from
				(select MEASUREMENT_POINT_ID from TAS_MEASUREMENT_POINT_V
				minus
				select TAS_MEASURE_POINT_ID from MEASUREMENT_POINT) new_mp;

		-- find new/modified records based on MEASUREMENT_POINT_ID in TASMo table
		for v_new_mp_rec in (
			SELECT
				mp_ltlf.oid,
				mp_tas.MEASUREMENT_POINT_ID,
				mp_tas.MP_ID,
				mp_tas.ZONE_CODE,
				mp_tas.MEASUREMENT_POINT_TYPE_CODE,
				mp_tas.MEASUREMENT_POINT_DESCR,
				mp_tas.MEASUREMENT_POINT_EFF,
				mp_tas.MEASUREMENT_POINT_EXP,
				mp_tas.PROGRAM_IN,
				mp_tas.PROJECT_IN,
				mp_tas.INCL_IN_POD_LSB,
				mp_id_ltlf.mp_name,
				mp_id_ltlf.rowid
			FROM
				TAS_MEASUREMENT_POINT_V mp_tas,
				MEASUREMENT_POINT mp_ltlf,
				MEASUREMENT_POINT_DETAILS mp_id_ltlf
			WHERE
				mp_ltlf.oid = mp_id_ltlf.MP_OID(+)
				AND mp_id_ltlf.EXPIRY_DATE IS NULL	-- active one
				AND mp_tas.MEASUREMENT_POINT_ID = mp_ltlf.TAS_MEASURE_POINT_ID
				AND (mp_id_ltlf.mp_name/*new*/ IS NULL OR mp_tas.MP_ID <> mp_id_ltlf.mp_name/*renamed*/)
		)
		loop
			insert into MEASUREMENT_POINT_DETAILS
				(MP_OID,
				CREATE_DATE,
				EXPIRY_DATE,
				MP_NAME,
				AREA_CODE,
				MEASUREMENT_POINT_TYPE_CODE,
				MEASUREMENT_POINT_DESCR,
				MEASUREMENT_POINT_EFF,
				MEASUREMENT_POINT_EXP,
				PROGRAM_IN,
				PROJECT_IN,
				INCL_IN_POD_LSB)
			values
				(v_new_mp_rec.oid,
				v_now,
				NULL,
				v_new_mp_rec.MP_ID,
				v_new_mp_rec.ZONE_CODE,
				v_new_mp_rec.MEASUREMENT_POINT_TYPE_CODE,
				v_new_mp_rec.MEASUREMENT_POINT_DESCR,
				v_new_mp_rec.MEASUREMENT_POINT_EFF,
				v_new_mp_rec.MEASUREMENT_POINT_EXP,
				v_new_mp_rec.PROGRAM_IN,
				v_new_mp_rec.PROJECT_IN,
				v_new_mp_rec.INCL_IN_POD_LSB);

			if v_new_mp_rec.mp_name IS NOT NULL then	-- renamed, change expiry date
				update MEASUREMENT_POINT_DETAILS
					set EXPIRY_DATE = v_now
				where rowid = v_new_mp_rec.rowid;
			end if;
		end loop;

		commit;
		log(LOG_LEVEL_INFO, 'End copying', 'CopyMPFromTASMo');
	EXCEPTION
		WHEN OTHERS THEN
			v_errorMsg := to_char(sqlcode) || ': ' || sqlerrm;
			rollback;
			dbms_output.put_line(v_errorMsg);
			log(LOG_LEVEL_ERROR, v_errorMsg, 'CopyMPFromTASMo');
	end;

	--Initialization
	begin
		SetLogLevel();
end pkg_LTLF;
/
show errors;
