drop table ALLOCATION_FCAST_YR
;

-- Create table
create table ALLOCATION_FCAST_YR
(
  OID 			   NUMBER not null,
  ALLOCATION_OID   NUMBER not null,
  FORECAST_YEAR    NUMBER(4),
  STATUS      	   VARCHAR2(8),
  AUDIT_DATETIME   DATE not null,
  AUDIT_USERID     VARCHAR2(20) not null
)
tablespace LTLF_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 100M
    minextents 1
    maxextents unlimited
  );

alter table ALLOCATION_FCAST_YR
  add constraint ALLOCATION_FCAST_YR_PK primary key (OID)
  using index
  tablespace LTLF_INDX;

CREATE INDEX ALLOC_FCAST_YR_ALLOC_OID_IDX ON ALLOCATION_FCAST_YR(ALLOCATION_OID)
  TABLESPACE LTLF_INDX;

-- Create/Recreate primary, unique and foreign key constraints 
CREATE OR REPLACE TRIGGER ALLOCATION_FCAST_YR_BEF_INS 

BEFORE INSERT
    ON ALLOCATION_FCAST_YR
    FOR EACH ROW

DECLARE
   nOID number;
   nAUDIT_DATETIME DATE;
BEGIN

  IF :new.oid IS NULL THEN
     select LTLF_SEQ.NEXTVAL into nOID from dual;
     :new.oid := nOID;
  END IF;

   IF :new.AUDIT_DATETIME IS NULL THEN
     select SYSDATE into nAUDIT_DATETIME from dual;
     :new.AUDIT_DATETIME := nAUDIT_DATETIME;
  END IF;

  IF :new.AUDIT_USERID IS NULL THEN
     :new.AUDIT_USERID := 'sys';
  END IF;

END;
/