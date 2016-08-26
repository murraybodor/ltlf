create or replace view load_shape_details_v as
	select OID,
		MP_OID,
		LOAD_SHAPE_SUMM_OID,
		BASE_YEAR,
		BASE_DAY,
		BASE_HOUR_END,
		nvl(UNIT_VALUE_OVERRIDE, UNIT_VALUE) AS UNIT_VALUE,
		AUDIT_DATETIME,
		AUDIT_USERID
	from load_shape_details;
