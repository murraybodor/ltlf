
drop table MEASUREMENT_POINT;
drop synonym MEASUREMENT_POINT;

-- Create table
create table MEASUREMENT_POINT
(
  OID             		NUMBER not null,
  TAS_MEASURE_POINT_ID	NUMBER not null,
  CREATED_DATE      	DATE
)
tablespace LTLF_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

alter table MEASUREMENT_POINT
  add constraint MEASUREMENT_POINT_PK primary key (OID)
  using index
  tablespace LTLF_INDX;

CREATE OR REPLACE TRIGGER MEASUREMENT_POINT_BEF_INS 

BEFORE INSERT
    ON MEASUREMENT_POINT
    FOR EACH ROW

DECLARE
   nOID number;
BEGIN

  IF :new.oid IS NULL THEN
     select LTLF_SEQ.NEXTVAL into nOID from dual;
     :new.oid := nOID;
  END IF;
END;
/
