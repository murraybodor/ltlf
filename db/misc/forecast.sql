/*
 * Script to create forecast
 *
 * unitized value at current time1
 * times
 * forecast date lookup time1 (base year) -> future time2 (forecast year)
 * growth factor version for future time2
 *
 */
select (t40.unitized_value * nvl(t10.load_factor, 1)) load_mw,
       t40.measure_point_wk,
       t20.future_time_wk time_wk,
       42 version_wk
  from (select t2.cal_day_date start_date,
               t3.cal_day_date end_date,
               t1.load_factor
          from ppoa.growth_factor_t t1, time_d t2, time_d t3
         where t1.time_wk = t2.time_wk
           and t1.end_time_wk = t3.time_wk
           and version_wk = 42
           and measure_point_wk = 3207) t10,
       ppoa.forecast_date_lookup_2007_t t20,
       ppoa.time_d t30,
       ppoa.ltlf_unitized_value_t t40
 where t20.future_time_wk = t30.time_wk
   and t20.current_time_wk = t40.time_wk
   and t30.cal_day_date >= t10.start_date
   and t30.cal_day_date < t10.end_date
   and t20.base_year = '&BASE_YEAR'
   and t20.cal_year = '&FORECAST_YEAR'
   and measure_point_wk = 3207
 order by time_wk

