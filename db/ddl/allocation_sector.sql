drop table ALLOCATION_SECTOR
;

-- Create table
create table ALLOCATION_SECTOR
(
  OID 			   NUMBER not null,
  ALLOC_FY_OID     NUMBER,
  ALLOC_AREA_OID   NUMBER,
  SECTOR_TYPE      VARCHAR2(12),
  ALLOC_PERCENT    NUMBER(6,3),
  ALLOC_ENERGY     NUMBER(12,3),
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

alter table ALLOCATION_SECTOR
  add constraint ALLOCATION_SECTOR_PK primary key (OID)
  using index
  tablespace LTLF_INDX;

CREATE INDEX ALLOC_SEC_ALLOC_FY_OID_IDX ON ALLOCATION_SECTOR(ALLOC_FY_OID)
  TABLESPACE LTLF_INDX;

CREATE INDEX ALLOC_SECTOR_IDX2 ON ALLOCATION_SECTOR(ALLOC_AREA_OID, SECTOR_TYPE)
  TABLESPACE LTLF_INDX;

-- Create/Recreate primary, unique and foreign key constraints 
CREATE OR REPLACE TRIGGER ALLOC_SECTOR_BEF_INS 

BEFORE INSERT
    ON ALLOCATION_SECTOR
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