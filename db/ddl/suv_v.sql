create or replace view SUV_V as
select am.oid, nvl(sum(lsd.unit_value), 0) as suv
from allocation_mp am, calendar cal, load_shape_details_v lsd
where cal.base_day = lsd.base_day
	and lsd.base_year = am.base_year
	and lsd.mp_oid = am.mp_oid
	and cal.forecast_date between am.begin_date and am.end_date
	and am.base_year = cal.base_year
group by am.oid;
