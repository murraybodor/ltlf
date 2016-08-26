drop table LOAD_FORECAST_DETAILS;

-- Create table
create table LOAD_FORECAST_DETAILS
(
  OID					NUMBER not null,
  MP_OID            	NUMBER not null,
  FORECAST_DATE			DATE not null,
  FORECAST_HOUR_END     NUMBER(2) not null,
  FORECAST_VALUE       	NUMBER(12,3) not null,
  AUDIT_DATETIME   	    DATE,
  AUDIT_USERID     		VARCHAR2(20)
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

alter table LOAD_FORECAST_DETAILS
  add constraint LOAD_FORECAST_DETAILS_PK primary key (OID)
  using index
  tablespace LTLF_INDX;

CREATE INDEX LOAD_FORECAST_DETAILS_IDX_1 ON LOAD_FORECAST_DETAILS(MP_OID, FORECAST_DATE)
      TABLESPACE LTLF_INDX;

CREATE OR REPLACE TRIGGER LOAD_FCAST_DTL_BEFORE_INSERT 

BEFORE INSERT
    ON LOAD_FORECAST_DETAILS
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