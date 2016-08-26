drop table LOAD_SHAPE_DETAILS
;

-- Create table
create table LOAD_SHAPE_DETAILS
(
  OID			 		NUMBER NOT NULL,
  MP_OID            	NUMBER not null,
  LOAD_SHAPE_SUMM_OID 	NUMBER not null,
  BASE_YEAR		   		NUMBER(4) not null,
  BASE_DAY		   		NUMBER(3) not null,
  BASE_HOUR_END	   		NUMBER(2) not null,
  UNIT_VALUE       		NUMBER(7,6),
  UNIT_VALUE_OVERRIDE   NUMBER(7,6),
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

alter table LOAD_SHAPE_DETAILS
  add constraint LOAD_SHAPE_DETAILS_PK primary key (OID)
  using index
  tablespace LTLF_INDX;

CREATE INDEX LOAD_SHAPE_DETAILS_IDX_1 ON LOAD_SHAPE_DETAILS(MP_OID, BASE_YEAR, BASE_DAY)
      TABLESPACE LTLF_INDX;

CREATE OR REPLACE TRIGGER LOAD_SHAPE_DTL_BEFORE_INSERT 

BEFORE INSERT
    ON LOAD_SHAPE_DETAILS
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
