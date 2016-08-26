drop table ANALYSIS;

-- Create table
create table ANALYSIS
(
  OID             NUMBER not null,
  CREATED_DT      DATE,
  IMPORT_START_DT DATE,
  IMPORT_END_DT   DATE,
  STATUS          VARCHAR2(20),
  PROGRESS        NUMBER(4,1),
  TOTAL_COUNT     NUMBER(20)
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

alter table ANALYSIS
  add constraint ANALYSIS_PK primary key (OID)
  using index
  tablespace LTLF_INDX;
