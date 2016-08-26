/*
 * Script to create unitized values
 * 
 * t100 is current year period
 * t110 is prior year period
 * 
 */ 
select t100.measure_point_wk,
       t100.time_wk,
       case
         when t110.unit is null then
          t100.unit
         else
          ((t100.unit * nvl(t100.split, 50) / 100) +
          (t110.unit * (100 - nvl(t100.split, 50)) / 100))
       end avg_unit
  from (select t10.time_wk,
               t10.measure_point_wk,
               t10.split,
               t10.sum_load_mw,
               t10.max_sum_load_mw,
               decode(t10.max_sum_load_mw,
                      0,
                      0,
                      (t10.sum_load_mw / t10.max_sum_load_mw)) unit,
               rank_sum_load_mw
          from ( -- current year is Q1
                select /*+  index(t2) PARALLEL(t2,1,8) PARALLEL(t1,1,8) */
                 t2.time_wk,
                  t3.measure_point_wk,
                  t4.split,
                  sum(t1.LOAD_MW) sum_load_mw,
                  max(sum(t1.LOAD_MW)) over(partition by t3.measure_point_wk) as max_sum_load_mw,
                  row_number() over(partition by t3.measure_point_wk order by sum(t1.LOAD_MW) desc) as rank_sum_load_mw
                  from CORP_HIST_LTLF_FIX_V         t1,
                        time_d                       t2,
                        measure_point_d              t3,
                        PROJECT_AREA_MEASURE_POINT_T t4
                 where t1.time_wk = t2.time_wk
                   and t1.measure_point_wk = t4.measure_point_wk
                   and t1.measure_point_wk = t3.measure_point_wk
                   and t4.project_area = 'Forecast'
                   and t2.cal_day_date = '2006-01-01'
                   and t2.cal_day_date < '2007-01-01'
                   and t3.measurement_point_type_code = 'DEM'
                 group by t2.time_wk, t3.measure_point_wk, t4.split) t10) t100,
       (select t10.time_wk,
               t10.measure_point_wk,
               t10.split,
               t10.sum_load_mw,
               t10.max_sum_load_mw,
               decode(t10.max_sum_load_mw,
                      0,
                      0,
                      (t10.sum_load_mw / t10.max_sum_load_mw)) unit,
               rank_sum_load_mw
          from (
                -- prior year
                select /*+  index(t2) PARALLEL(t2,1,8) PARALLEL(t1,1,8) */
                 t2.time_wk,
                  t3.measure_point_wk,
                  t4.split,
                  sum(t1.LOAD_MW) sum_load_mw,
                  max(sum(t1.LOAD_MW)) over(partition by t3.measure_point_wk) as max_sum_load_mw,
                  row_number() over(partition by t3.measure_point_wk order by sum(t1.LOAD_MW) desc) as rank_sum_load_mw
                  from CORP_HIST_LTLF_FIX_V         t1,
                        time_d                       t2,
                        measure_point_d              t3,
                        PROJECT_AREA_MEASURE_POINT_T t4
                 where t1.time_wk = t2.time_wk
                   and t1.measure_point_wk = t4.measure_point_wk
                   and t1.measure_point_wk = t3.measure_point_wk
                   and t4.project_area = 'Forecast'
                   and t2.cal_day_date = '2005-01-01'
                   and t2.cal_day_date < '2006-01-01'
                   and t3.measurement_point_type_code = 'DEM'
                 group by t2.time_wk, t3.measure_point_wk, t4.split) t10) t110
 where t100.rank_sum_load_mw = t110.rank_sum_load_mw(+)
   and t100.measure_point_wk = t110.measure_point_wk(+)

