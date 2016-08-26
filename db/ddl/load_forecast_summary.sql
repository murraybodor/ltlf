drop table LOAD_FORECAST_SUMMARY;

-- Create table
create table LOAD_FORECAST_SUMMARY
(
  OID					NUMBER not null,
  BASE_YEAR				NUMBER(4) not null,
  VERSION_NUM 	   		NUMBER(4) not null,
  COMMENTS				VARCHAR2(400),
  CREATE_DATE			DATE not null,
  PUBLISH_DATE			DATE,
  AUDIT_DATETIME   		DATE,
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

alter table LOAD_FORECAST_SUMMARY
  add constraint LOAD_FORECAST_SUMMARY_PK primary key (OID)
  using index
  tablespace LTLF_INDX;

CREATE INDEX LOAD_FORECAST_SUMMARY_IDX_1 ON LOAD_FORECAST_SUMMARY(BASE_YEAR)
      TABLESPACE LTLF_INDX;

CREATE OR REPLACE TRIGGER LOAD_FCAST_SUMM_BEFORE_INSERT 

BEFORE INSERT
    ON LOAD_FORECAST_SUMMARY
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