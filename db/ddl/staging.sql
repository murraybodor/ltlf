drop table STAGING cascade constraints
;

prompt
prompt Creating table STAGING
prompt ======================
prompt
create table STAGING
(
  MEASURE_POINT_ID            NUMBER(10),
  MP_NAME                     VARCHAR2(20) not null,
  CAL_DAY_DATE                DATE not null,
  CAL_HOUR_ENDING             NUMBER(2) not null,
  MW                          NUMBER(12,6)
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
alter table STAGING
  add constraint STAGING_PK primary key (MP_NAME, CAL_DAY_DATE, CAL_HOUR_ENDING);

