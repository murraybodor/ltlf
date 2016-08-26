create or replace view analysis_details_v as
	select oid,
		mp_oid,
		cal_day_date,
		cal_hour_ending,
		audit_datetime,
		audit_userid,
		nvl(mw_override, mw) AS mw
	from analysis_details;
