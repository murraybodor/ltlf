drop table ANALYSIS_SUMMARY
;

-- Create table
create table ANALYSIS_SUMMARY
(
  OID NUMBER not null,
  ANALYSIS_OID NUMBER not null, 
  MP_OID     NUMBER not null,
  GAP_COUNT NUMBER,
  STATUS    VARCHAR2(30),
  READY		VARCHAR2(1),
  PREV_YEAR_LOAD_FACTOR   NUMBER(5,2),
  BASE_YEAR_LOAD_FACTOR   NUMBER(5,2),
  AUDIT_DATETIME  DATE,
  AUDIT_USERID    VARCHAR2(20)
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

alter table ANALYSIS_SUMMARY
  add constraint ANALYSIS_SUMMARY_PK primary key (OID)
  using index
  tablespace LTLF_INDX;

CREATE INDEX ANALYSIS_SUMMARY_IDX_1 ON ANALYSIS_SUMMARY(ANALYSIS_OID)
      TABLESPACE LTLF_INDX;

CREATE INDEX ANALYSIS_SUMMARY_IDX_2 ON ANALYSIS_SUMMARY(MP_OID)
      TABLESPACE LTLF_INDX;

CREATE OR REPLACE TRIGGER ANALYSIS_SUMMARY_BEFORE_INSERT 

BEFORE INSERT
    ON analysis_summary
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
