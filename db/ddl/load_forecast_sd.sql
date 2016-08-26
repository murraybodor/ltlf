drop table LOAD_FORECAST_SD;

-- Create table
create table LOAD_FORECAST_SD
(
  OID					NUMBER not null,
  LFS_OID          		NUMBER not null,
  LFD_OID          		NUMBER not null
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

alter table LOAD_FORECAST_SD
  add constraint LOAD_FORECAST_SD_PK primary key (OID)
  using index
  tablespace LTLF_INDX;

CREATE INDEX LOAD_FORECAST_SD_IDX_1 ON LOAD_FORECAST_SD(LFS_OID, LFD_OID)
      TABLESPACE LTLF_INDX;

CREATE OR REPLACE TRIGGER LOAD_FCAST_SD_BEFORE_INSERT 

BEFORE INSERT
    ON LOAD_FORECAST_SD
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