drop synonym AREA;
drop synonym AREAS;
drop view AREA_V;

create or replace synonym AREA
  for CMEUL.IHFC_SYS_AREAS_V@DWHP.AESO;

create or replace view AREA_V
 as (SELECT AREA_CODE, 
			AREA_NAME
	FROM AREA
);

