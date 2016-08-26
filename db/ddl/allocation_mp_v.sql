-- Create view
create or replace view ALLOCATION_MP_V AS
( SELECT 
  MP.OID 			   ,
  MP.ALLOC_AREA_OID   ,
  MP.MP_OID           ,
  MP.PEAK_FACTOR      ,
  MP.ALLOC_PERCENT    ,
  MP.ALLOC_ENERGY     ,
  MP.BASE_YEAR        ,
  MP.BEGIN_DATE       ,
  MP.END_DATE         ,
  MP.STATUS      	   ,
  MP.AUDIT_DATETIME   ,
  MP.AUDIT_USERID     ,
  S.SUV 
FROM ALLOCATION_MP MP,
     SUV_V S
WHERE
	S.OID(+) = MP.OID
)
/