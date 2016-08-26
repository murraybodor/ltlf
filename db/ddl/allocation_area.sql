drop table ALLOCATION_AREA
;

-- Create table
create table ALLOCATION_AREA
(
  OID 			   NUMBER not null,
  ALLOC_FY_OID	   NUMBER not null,
  AREA_CODE        NUMBER not null,
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

alter table ALLOCATION_AREA
  add constraint ALLOCATION_AREA_PK primary key (OID)
  using index
  tablespace LTLF_INDX;

CREATE INDEX ALLOC_AREA_ALLOC_FY_OID_IDX ON ALLOCATION_AREA(ALLOC_FY_OID)
  TABLESPACE LTLF_INDX;

-- Create/Recreate primary, unique and foreign key constraints 
CREATE OR REPLACE TRIGGER ALLOCATION_AREA_BEF_INS 

BEFORE INSERT
    ON ALLOCATION_AREA
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