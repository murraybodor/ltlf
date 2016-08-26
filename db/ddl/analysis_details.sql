drop table ANALYSIS_DETAILS
;

-- Create table
create table ANALYSIS_DETAILS
(
  OID 			   NUMBER not null,
  MP_OID           NUMBER not null,
  CAL_DAY_DATE     DATE not null,
  CAL_HOUR_ENDING  NUMBER(2) not null,
  MW               NUMBER(12,6),
  MW_OVERRIDE      NUMBER(12,6),
  AUDIT_DATETIME   DATE,
  AUDIT_USERID     VARCHAR2(20)
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

alter table ANALYSIS_DETAILS
  add constraint ANALYSIS_DETAILS_PK primary key (OID)
  using index
  tablespace LTLF_INDX;

CREATE INDEX ANALYSIS_DETAILS_IDX_1 ON ANALYSIS_DETAILS(MP_OID, CAL_DAY_DATE, CAL_HOUR_ENDING)
      TABLESPACE LTLF_INDX;

CREATE INDEX ANALYSIS_DETAILS_CAL_DAY_IDX ON ANALYSIS_DETAILS(CAL_DAY_DATE)
  TABLESPACE LTLF_INDX;

-- Create/Recreate primary, unique and foreign key constraints 
CREATE OR REPLACE TRIGGER ANALYSIS_DTLS_BEFORE_INSERT 

BEFORE INSERT
    ON ANALYSIS_DETAILS
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