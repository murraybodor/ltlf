drop table MEASUREMENT_POINT_DETAILS;

create table MEASUREMENT_POINT_DETAILS
(
  OID             				NUMBER not null,
  MP_OID				     	NUMBER not null,
  CREATE_DATE					DATE,
  EXPIRY_DATE					DATE,
  MP_NAME						VARCHAR(16),
  AREA_CODE						VARCHAR(4),
  MEASUREMENT_POINT_TYPE_CODE	VARCHAR(4),
  MEASUREMENT_POINT_DESCR		VARCHAR(50),
  MEASUREMENT_POINT_EFF			DATE,
  MEASUREMENT_POINT_EXP			DATE,
  PROGRAM_IN					NUMBER(10),
  PROJECT_IN					NUMBER(5),
  INCL_IN_POD_LSB				CHAR(1)
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

alter table MEASUREMENT_POINT_DETAILS
  add constraint MEASUREMENT_POINT_DTL_PK primary key (OID)
  using index
  tablespace LTLF_INDX;

CREATE INDEX MP_DETAILS_IDX2 ON MEASUREMENT_POINT_DETAILS(MP_OID, CREATE_DATE, EXPIRY_DATE, MP_NAME)
  TABLESPACE LTLF_INDX;

CREATE OR REPLACE TRIGGER MEAS_PT_DTL_BEF_INS 

BEFORE INSERT
    ON MEASUREMENT_POINT_DETAILS
    FOR EACH ROW

DECLARE
   nOID number;
BEGIN
  IF :new.oid IS NULL THEN
     select LTLF_SEQ.NEXTVAL into nOID from dual;
     :new.oid := nOID;
  END IF;
END;
/