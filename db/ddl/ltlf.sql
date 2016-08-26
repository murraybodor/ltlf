--
-- LTLF application DDL
--

-- DBLINKS required:
-- TASx
-- DWHx
 


-- sequences, tables & views
@ltlf_seq.sql
@tas_measurement_point_v.sql
@measurement_point.sql
@measurement_point_details.sql
@area_v.sql
@codes.sql
@error_log.sql
@analysis.sql
@analysis_summary.sql
@analysis_details.sql
@analysis_details_v.sql
@staging.sql
@load_shape.sql
@load_shape_summary.sql
@load_shape_details.sql
@load_shape_details_v.sql

@calendar.sql
@allocation.sql
@allocation_comments.sql
@allocation_fcast_year.sql
@allocation_area.sql
@allocation_mp.sql
@allocation_sector.sql
@suv_v.sql

@load_forecast_summary.sql
@load_forecast_details.sql
@load_forecast_sd.sql

-- packages & procedures
@load_staging.sql

-- initial data population
@data.sql

