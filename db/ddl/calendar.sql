drop table CALENDAR
;

-- Create table
create table CALENDAR
(
  OID 				NUMBER not null,
  TIME_WHS_KEY 		NUMBER, 
  FORECAST_DATE   	DATE,
  BASE_YEAR	      	NUMBER(4),
  BASE_DAY        	NUMBER(3),
  AUDIT_DATETIME  	DATE,
  AUDIT_USERID    	VARCHAR2(20)
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

alter table CALENDAR
  add constraint CALENDAR_PK primary key (OID)
  using index
  tablespace LTLF_INDX;

CREATE INDEX CALENDAR_IDX_1 ON CALENDAR(FORECAST_DATE, BASE_YEAR, BASE_DAY)
      TABLESPACE LTLF_INDX;

CREATE OR REPLACE TRIGGER CALENDAR_BEFORE_INSERT 

BEFORE INSERT
    ON CALENDAR
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
