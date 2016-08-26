drop table ALLOCATION
;

-- Create table
create table ALLOCATION
(
  OID 			   NUMBER not null,
  BASE_YEAR        NUMBER(4),
  VERSION_NUM      NUMBER(4),
  START_DATE  	   DATE,
  END_YEAR         NUMBER(4),
  DESCRIPTION      VARCHAR2(400),
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

alter table ALLOCATION
  add constraint ALLOCATION_PK primary key (OID)
  using index
  tablespace LTLF_INDX;

-- Create/Recreate primary, unique and foreign key constraints 
CREATE OR REPLACE TRIGGER ALLOCATION_BEFORE_INSERT 

BEFORE INSERT
    ON ALLOCATION
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