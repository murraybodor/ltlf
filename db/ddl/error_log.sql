drop table ERROR_LOG;

-- Create table
create table ERROR_LOG
(
  TIME_DT DATE default sysdate not null,
  MODULE  VARCHAR2(50),
  ERROR   VARCHAR2(1024) not null
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
-- Add comments to the table 
comment on table ERROR_LOG
  is 'For logging errors, especially for errors in PL/SQL subprograms';
-- Create/Recreate indexes 
create index MODULE_IDX on ERROR_LOG (MODULE)
  tablespace LTLF_INDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index TIME_IDX on ERROR_LOG (TIME_DT)
  tablespace LTLF_INDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
