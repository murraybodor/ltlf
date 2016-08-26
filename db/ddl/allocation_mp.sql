drop table ALLOCATION_MP
;

-- Create table
create table ALLOCATION_MP
(
  OID 			   NUMBER not null,
  ALLOC_AREA_OID   NUMBER not null,
  MP_OID           NUMBER not null,
  PEAK_FACTOR      NUMBER(12,3),
  ALLOC_PERCENT    NUMBER(6,3),
  ALLOC_ENERGY     NUMBER(12,3),
  BASE_YEAR        NUMBER(4),
  BEGIN_DATE       DATE,
  END_DATE         DATE,
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

alter table ALLOCATION_MP
  add constraint ALLOCATION_MP_PK primary key (OID)
  using index
  tablespace LTLF_INDX;

CREATE INDEX ALLOC_MP_ALLOC_AREA_OID_IDX ON ALLOCATION_MP(ALLOC_AREA_OID)
  TABLESPACE LTLF_INDX;

CREATE INDEX ALLOC_MP_IDX_2 ON ALLOCATION_MP(MP_OID, BASE_YEAR, BEGIN_DATE, END_DATE)
  TABLESPACE LTLF_INDX;

-- Create/Recreate primary, unique and foreign key constraints 
CREATE OR REPLACE TRIGGER ALLOCATION_MP_BEF_INS 

BEFORE INSERT
    ON ALLOCATION_MP
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