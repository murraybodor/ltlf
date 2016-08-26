----------------------------------------------------
-- Export file for user LTLF                      --
-- Created by dthachuk on 2008-04-03, 12:34:24 PM --
----------------------------------------------------

spool export.log

prompt
prompt Creating table META_VERSION
prompt ===========================
prompt
create table META_VERSION
(
  VERSION_ID   NUMBER(10) not null,
  VERSION_NAME VARCHAR2(32),
  VERSION_DESC VARCHAR2(2048),
  CREATED_DT   DATE,
  UPDATED_DT   DATE,
  STATUS       VARCHAR2(16)
)
tablespace LTLF_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 1M
    minextents 1
    maxextents unlimited
  );
alter table META_VERSION
  add constraint META_VERSION_PK primary key (VERSION_ID);
create unique index META_VERSION_I1 on META_VERSION (VERSION_ID)
  tablespace LTLF_INDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 1M
    minextents 1
    maxextents unlimited
  );
create unique index META_VERSION_I2 on META_VERSION (VERSION_NAME)
  tablespace LTLF_INDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table COMBINED
prompt =======================
prompt
create table COMBINED
(
  VERSION_ID                  NUMBER(10) not null,
  MEASURE_POINT_ID            NUMBER(10),
  MP_ID                       VARCHAR2(20) not null,
  AREA_CODE                   VARCHAR2(50),
  MEASUREMENT_POINT_TYPE_CODE VARCHAR2(12),
  CONNECTION_TYPE             VARCHAR2(1),
  INCL_IN_POD_LSB             CHAR(1),
  CATEGORY                    VARCHAR2(10),
  CAL_DAY_DATE                DATE not null,
  CAL_HOUR_ENDING             VARCHAR2(3) not null,
  CAL_YEAR                    NUMBER(4),
  CAL_MONTH_NUMBER            NUMBER(2),
  CAL_MONTH_SHORT_NAME        VARCHAR2(3),
  TWO_SEASON_NAME             VARCHAR2(8),
  TWO_SEASON_YEAR             NUMBER(4),
  FOUR_SEASON_NAME            VARCHAR2(8),
  FOUR_SEASON_YEAR            NUMBER(4),
  MW                          NUMBER(12,6),
  MW_MAX                      NUMBER,
  MW_MIN                      NUMBER,
  MVAR                        NUMBER(12,6),
  MVAR_MAX                    NUMBER,
  MVAR_MIN                    NUMBER
)
partition by range (VERSION_ID)
(
  partition PARTITION_BEFORE values less than (1)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_01 values less than (2)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_02 values less than (3)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_03 values less than (4)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_04 values less than (5)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_05 values less than (6)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_06 values less than (7)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_07 values less than (8)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_08 values less than (9)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_09 values less than (10)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_10 values less than (11)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_AFTER values less than (11000)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    )
);
alter table COMBINED
  add constraint COMBINED_FK foreign key (VERSION_ID)
  references META_VERSION (VERSION_ID);
create unique index COMBINED_I2 on COMBINED (VERSION_ID, MP_ID, CAL_DAY_DATE, CAL_HOUR_ENDING);

prompt
prompt Creating table COMBINED_NOPART
prompt ==============================
prompt
create table COMBINED_NOPART
(
  VERSION_ID                  NUMBER(10) not null,
  MEASURE_POINT_ID            NUMBER(10),
  MP_ID                       VARCHAR2(20) not null,
  AREA_CODE                   VARCHAR2(50),
  MEASUREMENT_POINT_TYPE_CODE VARCHAR2(12),
  CONNECTION_TYPE             VARCHAR2(1),
  INCL_IN_POD_LSB             CHAR(1),
  CATEGORY                    VARCHAR2(10),
  CAL_DAY_DATE                DATE not null,
  CAL_HOUR_ENDING             VARCHAR2(3) not null,
  CAL_YEAR                    NUMBER(4),
  CAL_MONTH_NUMBER            NUMBER(2),
  CAL_MONTH_SHORT_NAME        VARCHAR2(3),
  TWO_SEASON_NAME             VARCHAR2(8),
  TWO_SEASON_YEAR             NUMBER(4),
  FOUR_SEASON_NAME            VARCHAR2(8),
  FOUR_SEASON_YEAR            NUMBER(4),
  MW                          NUMBER(12,6),
  MW_MAX                      NUMBER,
  MW_MIN                      NUMBER,
  MVAR                        NUMBER(12,6),
  MVAR_MAX                    NUMBER,
  MVAR_MIN                    NUMBER
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
alter table COMBINED_NOPART
  add constraint COMBINED_NOPART_PK primary key (VERSION_ID, MP_ID, CAL_DAY_DATE, CAL_HOUR_ENDING);
alter table COMBINED_NOPART
  add constraint COMBINED_NOPART_FK foreign key (VERSION_ID)
  references META_VERSION (VERSION_ID);
create unique index COMBINED_NOPART_I1 on COMBINED_NOPART (VERSION_ID, MP_ID, CAL_DAY_DATE, CAL_HOUR_ENDING)
  tablespace LTLF_INDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table COMBINED_PART
prompt ============================
prompt
create table COMBINED_PART
(
  VERSION_ID                  NUMBER(10) not null,
  MEASURE_POINT_ID            NUMBER(10),
  MP_ID                       VARCHAR2(20) not null,
  AREA_CODE                   VARCHAR2(50),
  MEASUREMENT_POINT_TYPE_CODE VARCHAR2(12),
  CONNECTION_TYPE             VARCHAR2(1),
  INCL_IN_POD_LSB             CHAR(1),
  CATEGORY                    VARCHAR2(10),
  CAL_DAY_DATE                DATE not null,
  CAL_HOUR_ENDING             VARCHAR2(3) not null,
  CAL_YEAR                    NUMBER(4),
  CAL_MONTH_NUMBER            NUMBER(2),
  CAL_MONTH_SHORT_NAME        VARCHAR2(3),
  TWO_SEASON_NAME             VARCHAR2(8),
  TWO_SEASON_YEAR             NUMBER(4),
  FOUR_SEASON_NAME            VARCHAR2(8),
  FOUR_SEASON_YEAR            NUMBER(4),
  MW                          NUMBER(12,6),
  MW_MAX                      NUMBER,
  MW_MIN                      NUMBER,
  MVAR                        NUMBER(12,6),
  MVAR_MAX                    NUMBER,
  MVAR_MIN                    NUMBER
)
partition by range (VERSION_ID)
(
  partition PARTITION_BEFORE values less than (1)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_01 values less than (2)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_02 values less than (3)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_03 values less than (4)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_04 values less than (5)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_05 values less than (6)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_06 values less than (7)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_07 values less than (8)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_08 values less than (9)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_09 values less than (10)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_10 values less than (11)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_AFTER values less than (11000)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    )
);
alter table COMBINED_PART
  add constraint COMBINED_PART_FK foreign key (VERSION_ID)
  references META_VERSION (VERSION_ID);
create unique index COMBINED_PART_I2 on COMBINED_PART (VERSION_ID, MP_ID, CAL_DAY_DATE, CAL_HOUR_ENDING);

prompt
prompt Creating table MEASURE_POINT_D
prompt ==============================
prompt
create table MEASURE_POINT_D
(
  MEASURE_POINT_WK            NUMBER(10) not null,
  MEASURE_POINT_ID            NUMBER(10),
  ZONE_CODE                   VARCHAR2(10),
  MP_ID                       VARCHAR2(20) not null,
  CATEGORY                    VARCHAR2(10) default 'GRID',
  INCL_IN_POD_LSB             CHAR(1),
  MEASUREMENT_POINT_TYPE_CODE VARCHAR2(12),
  CONNECTION_TYPE             VARCHAR2(1),
  FORECAST_GROUP              VARCHAR2(10),
  AREA_CODE                   VARCHAR2(50),
  EFFECTIVE_DT                DATE,
  EXPIRY_DT                   DATE,
  TAS_DESCRIPTION             VARCHAR2(50),
  ZONE_TYPE_ID                NUMBER(10),
  FUEL_TYPE                   VARCHAR2(255),
  MPID_MCR                    NUMBER,
  CORP_DESCRIPTION            VARCHAR2(50)
)
tablespace LTLF_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 1M
    minextents 1
    maxextents unlimited
  );
comment on table MEASURE_POINT_D
  is 'Measurement Point dimension table';
alter table MEASURE_POINT_D
  add constraint MEASURE_POINT_PK primary key (MEASURE_POINT_WK);
create unique index MEASURE_POINT_00_I on MEASURE_POINT_D (MEASURE_POINT_WK)
  tablespace LTLF_INDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 1M
    minextents 1
    maxextents unlimited
  );
create unique index MEASURE_POINT_01_I on MEASURE_POINT_D (MEASURE_POINT_ID)
  tablespace LTLF_INDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 1M
    minextents 1
    maxextents unlimited
  );
create unique index MEASURE_POINT_02_I on MEASURE_POINT_D (MP_ID)
  tablespace LTLF_INDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 1M
    minextents 1
    maxextents unlimited
  );
create index MEASURE_POINT_03_I on MEASURE_POINT_D (CATEGORY)
  tablespace LTLF_INDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 1M
    minextents 1
    maxextents unlimited
  );
create bitmap index MEASURE_POINT_04_I on MEASURE_POINT_D (MEASUREMENT_POINT_TYPE_CODE)
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

prompt
prompt Creating table META_MAIN
prompt ========================
prompt
create table META_MAIN
(
  START_DT        DATE,
  END_DT          DATE,
  MIN_DT          DATE,
  MAX_DT          DATE,
  CREATED_DT      DATE,
  UPDATED_DT      DATE,
  STATUS          VARCHAR2(16),
  COUNT_INITIAL   NUMBER,
  COUNT_INC_TOTAL NUMBER,
  COUNT_INC_LAST  NUMBER,
  INCREMENT_TOTAL NUMBER
)
tablespace LTLF_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table MODIFIED
prompt =======================
prompt
create table MODIFIED
(
  VERSION_ID                  NUMBER(10) not null,
  MEASURE_POINT_ID            NUMBER(10),
  MP_ID                       VARCHAR2(20) not null,
  AREA_CODE                   VARCHAR2(50),
  MEASUREMENT_POINT_TYPE_CODE VARCHAR2(12),
  CONNECTION_TYPE             VARCHAR2(1),
  INCL_IN_POD_LSB             CHAR(1),
  CATEGORY                    VARCHAR2(10),
  CAL_DAY_DATE                DATE not null,
  CAL_HOUR_ENDING             VARCHAR2(3) not null,
  CAL_YEAR                    NUMBER(4),
  CAL_MONTH_NUMBER            NUMBER(2),
  CAL_MONTH_SHORT_NAME        VARCHAR2(3),
  TWO_SEASON_NAME             VARCHAR2(8),
  TWO_SEASON_YEAR             NUMBER(4),
  FOUR_SEASON_NAME            VARCHAR2(8),
  FOUR_SEASON_YEAR            NUMBER(4),
  MW                          NUMBER(12,6),
  MW_MAX                      NUMBER,
  MW_MIN                      NUMBER,
  MW_SUGGESTED                NUMBER(12,6),
  MW_MAX_SUGGESTED            NUMBER,
  MW_MIN_SUGGESTED            NUMBER,
  MVAR                        NUMBER(12,6),
  MVAR_MAX                    NUMBER,
  MVAR_MIN                    NUMBER,
  MVAR_SUGGESTED              NUMBER(12,6),
  MVAR_MAX_SUGGESTED          NUMBER,
  MVAR_MIN_SUGGESTED          NUMBER
)
partition by range (VERSION_ID)
(
  partition PARTITION_BEFORE values less than (1)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_01 values less than (2)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_02 values less than (3)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_03 values less than (4)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_04 values less than (5)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_05 values less than (6)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_06 values less than (7)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_07 values less than (8)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_08 values less than (9)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_09 values less than (10)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_10 values less than (11)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_AFTER values less than (11000)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    )
);
alter table MODIFIED
  add constraint MODIFIED_FK foreign key (VERSION_ID)
  references META_VERSION (VERSION_ID);
create unique index MODIFIED_I2 on MODIFIED (VERSION_ID, MP_ID, CAL_DAY_DATE, CAL_HOUR_ENDING);

prompt
prompt Creating table MODIFIED_NOPART
prompt ==============================
prompt
create table MODIFIED_NOPART
(
  VERSION_ID                  NUMBER(10) not null,
  MEASURE_POINT_ID            NUMBER(10),
  MP_ID                       VARCHAR2(20) not null,
  AREA_CODE                   VARCHAR2(50),
  MEASUREMENT_POINT_TYPE_CODE VARCHAR2(12),
  CONNECTION_TYPE             VARCHAR2(1),
  INCL_IN_POD_LSB             CHAR(1),
  CATEGORY                    VARCHAR2(10),
  CAL_DAY_DATE                DATE not null,
  CAL_HOUR_ENDING             VARCHAR2(3) not null,
  CAL_YEAR                    NUMBER(4),
  CAL_MONTH_NUMBER            NUMBER(2),
  CAL_MONTH_SHORT_NAME        VARCHAR2(3),
  TWO_SEASON_NAME             VARCHAR2(8),
  TWO_SEASON_YEAR             NUMBER(4),
  FOUR_SEASON_NAME            VARCHAR2(8),
  FOUR_SEASON_YEAR            NUMBER(4),
  MW                          NUMBER(12,6),
  MW_MAX                      NUMBER,
  MW_MIN                      NUMBER,
  MW_SUGGESTED                NUMBER(12,6),
  MW_MAX_SUGGESTED            NUMBER,
  MW_MIN_SUGGESTED            NUMBER,
  MVAR                        NUMBER(12,6),
  MVAR_MAX                    NUMBER,
  MVAR_MIN                    NUMBER,
  MVAR_SUGGESTED              NUMBER(12,6),
  MVAR_MAX_SUGGESTED          NUMBER,
  MVAR_MIN_SUGGESTED          NUMBER
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
alter table MODIFIED_NOPART
  add constraint MODIFIED_NOPART_PK primary key (VERSION_ID, MP_ID, CAL_DAY_DATE, CAL_HOUR_ENDING);
alter table MODIFIED_NOPART
  add constraint MODIFIED_NOPART_FK foreign key (VERSION_ID)
  references META_VERSION (VERSION_ID);
create unique index MODIFIED_NOPART_I1 on MODIFIED_NOPART (VERSION_ID, MP_ID, CAL_DAY_DATE, CAL_HOUR_ENDING)
  tablespace LTLF_INDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table MODIFIED_PART
prompt ============================
prompt
create table MODIFIED_PART
(
  VERSION_ID                  NUMBER(10) not null,
  MEASURE_POINT_ID            NUMBER(10),
  MP_ID                       VARCHAR2(20) not null,
  AREA_CODE                   VARCHAR2(50),
  MEASUREMENT_POINT_TYPE_CODE VARCHAR2(12),
  CONNECTION_TYPE             VARCHAR2(1),
  INCL_IN_POD_LSB             CHAR(1),
  CATEGORY                    VARCHAR2(10),
  CAL_DAY_DATE                DATE not null,
  CAL_HOUR_ENDING             VARCHAR2(3) not null,
  CAL_YEAR                    NUMBER(4),
  CAL_MONTH_NUMBER            NUMBER(2),
  CAL_MONTH_SHORT_NAME        VARCHAR2(3),
  TWO_SEASON_NAME             VARCHAR2(8),
  TWO_SEASON_YEAR             NUMBER(4),
  FOUR_SEASON_NAME            VARCHAR2(8),
  FOUR_SEASON_YEAR            NUMBER(4),
  MW                          NUMBER(12,6),
  MW_MAX                      NUMBER,
  MW_MIN                      NUMBER,
  MW_SUGGESTED                NUMBER(12,6),
  MW_MAX_SUGGESTED            NUMBER,
  MW_MIN_SUGGESTED            NUMBER,
  MVAR                        NUMBER(12,6),
  MVAR_MAX                    NUMBER,
  MVAR_MIN                    NUMBER,
  MVAR_SUGGESTED              NUMBER(12,6),
  MVAR_MAX_SUGGESTED          NUMBER,
  MVAR_MIN_SUGGESTED          NUMBER
)
partition by range (VERSION_ID)
(
  partition PARTITION_BEFORE values less than (1)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_01 values less than (2)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_02 values less than (3)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_03 values less than (4)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_04 values less than (5)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_05 values less than (6)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_06 values less than (7)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_07 values less than (8)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_08 values less than (9)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_09 values less than (10)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_10 values less than (11)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    ),
  partition PARTITION_AFTER values less than (11000)
    tablespace LTLF_DATA
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 10M
      minextents 1
      maxextents unlimited
    )
);
alter table MODIFIED_PART
  add constraint MODIFIED_PART_FK foreign key (VERSION_ID)
  references META_VERSION (VERSION_ID);
create unique index MODIFIED_PART_I2 on MODIFIED_PART (VERSION_ID, MP_ID, CAL_DAY_DATE, CAL_HOUR_ENDING);

prompt
prompt Creating table STAGING
prompt ======================
prompt
create table STAGING
(
  MEASURE_POINT_ID            NUMBER(10),
  MP_ID                       VARCHAR2(20) not null,
  AREA_CODE                   VARCHAR2(50),
  MEASUREMENT_POINT_TYPE_CODE VARCHAR2(12),
  CONNECTION_TYPE             VARCHAR2(1),
  INCL_IN_POD_LSB             CHAR(1),
  CATEGORY                    VARCHAR2(10),
  CAL_DAY_DATE                DATE not null,
  CAL_HOUR_ENDING             VARCHAR2(3) not null,
  CAL_YEAR                    NUMBER(4),
  CAL_MONTH_NUMBER            NUMBER(2),
  CAL_MONTH_SHORT_NAME        VARCHAR2(3),
  TWO_SEASON_NAME             VARCHAR2(8),
  TWO_SEASON_YEAR             NUMBER(4),
  FOUR_SEASON_NAME            VARCHAR2(8),
  FOUR_SEASON_YEAR            NUMBER(4),
  MW                          NUMBER(12,6),
  MW_MAX                      NUMBER,
  MW_MIN                      NUMBER,
  MVAR                        NUMBER(12,6),
  MVAR_MAX                    NUMBER,
  MVAR_MIN                    NUMBER
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
  add constraint STAGING_PK primary key (MP_ID, CAL_DAY_DATE, CAL_HOUR_ENDING);
create unique index STAGING_I1 on STAGING (MP_ID, CAL_DAY_DATE, CAL_HOUR_ENDING)
  tablespace LTLF_INDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table STAGING_NOGAP
prompt ============================
prompt
create table STAGING_NOGAP
(
  MP_ID           VARCHAR2(20) not null,
  CAL_DAY_DATE    DATE not null,
  CAL_HOUR_ENDING VARCHAR2(3) not null
)
tablespace LTLF_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 42M
    minextents 1
    maxextents unlimited
  );
create unique index STAGING_NOGAP_I1 on STAGING_NOGAP (MP_ID, CAL_DAY_DATE, CAL_HOUR_ENDING)
  tablespace LTLF_INDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table TIME_D
prompt =====================
prompt
create table TIME_D
(
  CAL_DAY_DATE            DATE not null,
  CAL_DAY_HOURS           NUMBER(2),
  CAL_DAY_NAME            VARCHAR2(10),
  CAL_DAY_NUMBER_IN_MONTH NUMBER(2),
  CAL_DAY_NUMBER_IN_WEEK  NUMBER(1),
  CAL_DAY_SHORT_NAME      VARCHAR2(4),
  CAL_GMT_HOUR_BEGIN_DATE DATE not null,
  CAL_GMT_HOUR_END_DATE   DATE not null,
  CAL_HOUR_ENDING         VARCHAR2(3) not null,
  CAL_TIME_ZONE           VARCHAR2(3) not null,
  TIME_WK                 NUMBER(10) not null,
  CAL_MONTH_DAYS          NUMBER(2),
  CAL_MONTH_DESC          VARCHAR2(7) not null,
  CAL_MONTH_NAME          VARCHAR2(10),
  CAL_MONTH_NUMBER        NUMBER(2) not null,
  CAL_MONTH_SHORT_NAME    VARCHAR2(3),
  CAL_YEAR                NUMBER(4) not null,
  CAL_YEAR_DAYS           NUMBER(3),
  SETTLE_PERIOD_ID        NUMBER(9) not null,
  CAL_HOUR_ENDING_EXCHG   NUMBER(2) not null,
  ON_PEAK_FLAG            VARCHAR2(1) not null,
  CAL_WEEK_BEGIN_DATE     DATE not null,
  CAL_MONTH_DATE          DATE,
  CAL_HOUR_BEGIN_DATE     DATE,
  TWO_SEASON_NAME         VARCHAR2(8),
  TWO_SEASON_YEAR         NUMBER(4),
  FOUR_SEASON_NAME        VARCHAR2(8),
  FOUR_SEASON_YEAR        NUMBER(4)
)
tablespace LTLF_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 42M
    minextents 1
    maxextents unlimited
  );
alter table TIME_D
  add constraint TIME_PK primary key (TIME_WK);
create unique index TIME_00_I on TIME_D (TIME_WK)
  tablespace LTLF_INDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 1M
    minextents 1
    maxextents unlimited
  );
create unique index TIME_01_I on TIME_D (SETTLE_PERIOD_ID)
  tablespace LTLF_INDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 1M
    minextents 1
    maxextents unlimited
  );
create bitmap index TIME_02_I on TIME_D (CAL_MONTH_DESC)
  tablespace LTLF_INDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 1M
    minextents 1
    maxextents unlimited
  );
create unique index TIME_03_I on TIME_D (CAL_DAY_DATE, CAL_HOUR_ENDING_EXCHG)
  tablespace LTLF_INDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 1M
    minextents 1
    maxextents unlimited
  );
create bitmap index TIME_04_I on TIME_D (ON_PEAK_FLAG)
  tablespace LTLF_INDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 1M
    minextents 1
    maxextents unlimited
  );
create bitmap index TIME_05_I on TIME_D (CAL_WEEK_BEGIN_DATE)
  tablespace LTLF_INDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 1M
    minextents 1
    maxextents unlimited
  );
create bitmap index TIME_06_I on TIME_D (CAL_YEAR)
  tablespace LTLF_INDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 1M
    minextents 1
    maxextents unlimited
  );
create unique index TIME_07_I on TIME_D (CAL_GMT_HOUR_BEGIN_DATE)
  tablespace LTLF_INDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 1M
    minextents 1
    maxextents unlimited
  );
create bitmap index TIME_08_I on TIME_D (CAL_MONTH_DATE)
  tablespace LTLF_INDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 1M
    minextents 1
    maxextents unlimited
  );
create index TIME_09_I on TIME_D (CAL_HOUR_BEGIN_DATE)
  tablespace LTLF_INDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 1M
    minextents 1
    maxextents unlimited
  );
create index TIME_10_I on TIME_D (CAL_DAY_DATE)
  tablespace LTLF_INDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 1M
    minextents 1
    maxextents unlimited
  );
create bitmap index TIME_11_I on TIME_D (CAL_HOUR_ENDING)
  tablespace LTLF_INDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table TIME_NOGAP
prompt =========================
prompt
create table TIME_NOGAP
(
  CAL_DAY_DATE    DATE not null,
  CAL_HOUR_ENDING VARCHAR2(3) not null
)
tablespace LTLF_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 42M
    minextents 1
    maxextents unlimited
  );
create unique index TIME_NOGAP_I1 on TIME_NOGAP (CAL_DAY_DATE, CAL_HOUR_ENDING)
  tablespace LTLF_INDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating package LOAD_STAGING
prompt =============================
prompt
create or replace package load_staging
as

/***********************************************************************
   CREATED:        Dec/24/2007
   AUTHOR:         ANDREW LAPIDES
***********************************************************************/

   SUCCESS                    CONSTANT  NUMBER := 0;
   FAILURE                    CONSTANT  NUMBER := -1;
   INPROGRESS                 CONSTANT  VARCHAR2(16) := 'INPROGRESS';
   READY                      CONSTANT  VARCHAR2(16) := 'READY';
   DEBUG                      CONSTANT  NUMBER := 0;

   VERSION_MAX                CONSTANT  NUMBER := 10;

	  TYPE datepoint_date_tabtype IS TABLE OF date INDEX BY BINARY_INTEGER;

	  TYPE datepoint_he_tabtype IS TABLE OF varchar2(3) INDEX BY BINARY_INTEGER;

   function LoadFull(pdStartDt in date,
                     pdEndDt in date,
					 psError out varchar2, pnRowCount out number) return number;

   function LoadInc(psError out varchar2) return number;

   function GapAnalysis(psName in string,psDesc in string,psError out varchar2) return number;

   function GapAnalysisSetSuggested(psName in string,
            psTargetMP_ID in varchar2,
			pdTargetDtStart in date,pdTargetDtEnd in date,
			psTargetCalHourEndingStart in varchar2,psTargetCalHourEndingEnd in varchar2,
            psSourceTable in varchar2,
			piSourceVersionID in number,
            psSourceMP_ID in varchar2,
            pdSourceDtStart in date,
			psSourceCalHourEndingStart in varchar2,
			psError out varchar2) return number;

   function GapAnalysisAcceptSuggested(psName in string,
            psTargetMP_ID in varchar2,
			pdTargetDtStart in date,pdTargetDtEnd in date,
			psTargetCalHourEndingStart in varchar2,psTargetCalHourEndingEnd in varchar2,
			psError out varchar2) return number;

   function GapValidate(psName in string,
            psSourceTable in varchar2,
            psTargetMP_ID in varchar2,
			pdTargetDtStart in date,pdTargetDtEnd in date,
			psTargetCalHourEndingStart in varchar2,psTargetCalHourEndingEnd in varchar2,
			piValid out number,
			psError out varchar2) return number;

   function PurgeVersionName(psName in string,psError out varchar2) return number;

   function PurgeVersionID(piVersionID in number,psError out varchar2) return number;

   function PurgeVersionAll(psError out varchar2) return number;

   function CopyVersionName(psNameSrc in string,psNameDest in string,
                            psDescDest in string,psError out varchar2) return number;
   function IntersectModifiedStaging(psNameSrc in string,psNameDest in string,
                            psDescDest in string,psError out varchar2) return number;
   function PopulateCombined(psNameSrc in string,psError out varchar2) return number;

   function AverageItMP(psNameSrc in string,psNameDest in string,
                      psDescDest in string,
					  psSourceTable in varchar2,psDestTable in varchar2,
					  psMP_ID in varchar2,
					  piWindowWidthHalf in number,
					  piSuggestedIndSrc in number,
					  piSuggestedIndDest in number,
					  psError out varchar2) return number;
   function MinusItMP(psNameSrc in string,psNameSrc1 in string,
                      psNameDest in string,
                      psDescDest in string,
					  psSourceTable in varchar2,psDestTable in varchar2,
					  psMP_ID in varchar2,
					  piSuggestedIndSrc in number,
					  piSuggestedIndDest in number,
					  psError out varchar2) return number;
   function DerivItMP(psNameSrc in string,psNameDest in string,
                      psDescDest in string,
					  psSourceTable in varchar2,psDestTable in varchar2,
					  psMP_ID in varchar2,
					  pfThreshold in number,
					  piSuggestedIndSrc in number,
					  piSuggestedIndDest in number,
					  psError out varchar2) return number;
   function RemovePeaksMP(psNameSrc in string,psNameSrc1 in string,
                      psNameDest in string,
                      psDescDest in string,
					  psSourceTable in varchar2,psDestTable in varchar2,
					  psMP_ID in varchar2,
					  psWhatToProcess in varchar2,
					  piSuggestedIndSrc in number,
					  piSuggestedIndDest in number,
					  piTolerance in number,
					  psError out varchar2) return number;
end load_staging;
/

prompt
prompt Creating procedure DTLOAD_FULL
prompt ==============================
prompt
create or replace procedure dtLOAD_FULL(pstartDate in Date, pendDate in Date, rValue out VARCHAR) is
begin
  select 'proc works' into rValue from dual;
end dtLOAD_FULL;
/

prompt
prompt Creating package body LOAD_STAGING
prompt ==================================
prompt
create or replace package body load_staging as

---------------------  LOCAL FUNCTIONS BEGIN -----------------------
   function GetVersionID(psName in string,psError out varchar2) return number is
/**************************************************************
   THIS WILL accept specified MODIFIED version (version NAME is provided)
   AND IT WILL RETURN THE version_id

   IF NO RECORD EXISTS IT WILL RETURN -1 AND psError will be set appropriatry
**************************************************************/
	  liVersionID number;

   begin

	  psError:='';

	  select nvl(version_id,-1)
	        into liVersionID
		    from meta_version
		    where version_name=psName;

	  return liVersionID;
   Exception
      WHEN OTHERS THEN
	     if to_char(sqlcode)=100 then
			psError:='No record exists for version '''||psName||'''';
            return FAILURE;
		 else
            psError:=to_char(sqlcode)||': '||sqlerrm;
            dbms_output.put_line(psError);
		    return FAILURE;
		 end if;
   end;

   function GetNextVersionID(psName in string,psError out varchar2) return number is
/**************************************************************
   THIS WILL accept specified version NAME
   It will check if this name is occupied, also, if the max threshold is
   exceeded, and then it will fetch the next available version_id and return it.

   IF this is impossible IT WILL RETURN -1 AND psError will be set appropriatry
**************************************************************/
	  liVersionID number;
	  liCount number;

   begin
	  psError:='';

	  liVersionID:=GetVersionID(psName,psError);

	  psError:='';

	  if -1<liVersionID then
	     psError:='The version with this name already exists. Purge it first';
         dbms_output.put_line(psError);
		 return FAILURE;
	  end if;

	  /* check against count of existing records in META_VERSION */
      select count(*)
         into liCount
		 from meta_version;

	  if VERSION_MAX <= liCount then
         psError:='The limit of versions is set to '||VERSION_MAX||' and it is exceeded';
         dbms_output.put_line(psError);
	     return FAILURE;
	  end if;

	  -- obtain liVersionID
	  if liCount=0 then
	     /* no versions currently, starting with fresh version */
		 liVersionID:=1;
	  else
         select max(VERSION_ID)+1
            into liVersionID
		    from meta_version;
      end if;

	  return liVersionID;
   Exception
      WHEN OTHERS THEN
         psError:=to_char(sqlcode)||': '||sqlerrm;
         dbms_output.put_line(psError);
   end;





   function GetNextDTPoint(psSourceTable in varchar2,
			piSourceVersionID in number,
            psSourceMP_ID in varchar2,
            pdCalDayDate in date,
			psCalHourEnding in varchar2,
			pdCalDayDateNext out date,
			psCalHourEndingNext out varchar2,
			pfMW out number,
			pfMVAR out number,
			pfMW_MIN out number,
			pfMVAR_MIN out number,
			pfMW_MAX out number,
			pfMVAR_MAX out number,
			psError out varchar2) return number is
/**************************************************************
   THIS WILL FIND THE NEXT TIME POINT AND return CAL_DAY_DATE
   and CAL_HOUR_ENDING and all MWsfor this point

   IF NO RECORD EXISTS IT WILL RETURN -1 AND psError will be set appropriatry
**************************************************************/
	  lsErrorMsg varchar2(256);
	  liVersionID number;
	  lsSQLSource varchar2(4096);
	  lsCalHourEndingNext varchar2(3);
	  ldCalDayDateNext date;
	  liCount number;

      TYPE cv_typ IS REF CURSOR;
      cv cv_typ;
      --v_id employee.employee_id%TYPE;
      --v_nm employee.last_name%TYPE;
   begin

	  lsErrorMsg:='';

	  /*
	  dbms_output.put_line('source table: '||psSourceTable);
	  dbms_output.put_line('psSourceMP_ID: '||psSourceMP_ID);
	  dbms_output.put_line('pdCalDayDate: '||to_char(pdCalDayDate,'DD-MON-RRRR'));
	  dbms_output.put_line('psCalHourEnding: '||psCalHourEnding);
	  */

	  -- first find next stamp from time dimension
	  -- first date itself
	  -- first try to find the next record within same date
      select min(cal_hour_ending),count(*)
            into lsCalHourEndingNext,liCount
	  from time_d  --ppoa.time_d
	  where pdCalDayDate=cal_day_date
		    and psCalHourEnding<cal_hour_ending;

	  -- if we came here, this is the record
      ldCalDayDateNext:=pdCalDayDate;

	  if liCount=0 then
         ldCalDayDateNext:=pdCalDayDate+1;
         lsCalHourEndingNext:='01';
	  end if;

	  /*
	  dbms_output.put_line('After block');
	  dbms_output.put_line('ldCalDayDateNext: '||to_char(ldCalDayDateNext,'DD-MON-RRRR'));
	  dbms_output.put_line('lsCalHourEndingNext: '||lsCalHourEndingNext);
	  */

	  -- so at this moment we found the next timestamp from the time dimension
	  -- make sure the record is there in the SOURCE table

	  lsSQLSource:='select CAL_DAY_DATE,CAL_HOUR_ENDING ';
	  lsSQLSource:=lsSQLSource||',MW,MVAR,MW_MIN,MVAR_MIN,MW_MAX,MVAR_MAX ';
	  lsSQLSource:=lsSQLSource||'from '||psSourceTable||' ';
	  lsSQLSource:=lsSQLSource||'where MP_ID=:lsMP_ID ';
	  lsSQLSource:=lsSQLSource||'and :pdCalDayDateNext=CAL_DAY_DATE ';
	  lsSQLSource:=lsSQLSource||'and :psCalHourEndingNext=CAL_HOUR_ENDING ';

	  if -1<piSourceVersionID then
	     lsSQLSource:=lsSQLSource||'and version_id='||to_char(piSourceVersionID) ||' ';
	  end if;

      OPEN cv FOR lsSQLSource
	     USING  psSourceMP_ID,ldCalDayDateNext,lsCalHourEndingNext;
      LOOP
         FETCH cv INTO pdCalDayDateNext,psCalHourEndingNext,
		               pfMW,pfMVAR,pfMW_MIN,pfMVAR_MIN,pfMW_MAX,pfMVAR_MAX;
         EXIT WHEN cv%NOTFOUND;
      END LOOP;
      CLOSE cv;

	  if pdCalDayDateNext is null then
      	 psError:='No next timepoint exists in the table '||psSourceTable||' for MP_ID '||psSourceMP_ID
		           ||', CAL_DAY_DATE='||to_char(ldCalDayDateNext,'DD-MON-RRRR')
				   ||', CAL_HOUR_ENDING='||to_char(lsCalHourEndingNext);
      	 return FAILURE;
	  end if;

	  /*
      dbms_output.put_line('    Returning pfMW: '||pfMW);
      dbms_output.put_line('              pfMVAR: '||pfMVAR);
	  dbms_output.put_line('              pfMW_MIN: '||pfMW_MIN);
	  dbms_output.put_line('              pfMVAR_MIN: '||pfMVAR_MIN);
	  dbms_output.put_line('              pfMW_MAX: '||pfMW_MAX);
	  dbms_output.put_line('              pfMVAR_MAX: '||pfMVAR_MAX);
	  */

	  return SUCCESS;

   Exception
      WHEN OTHERS THEN
         lsErrorMsg:=to_char(sqlcode)||': '||sqlerrm;
         dbms_output.put_line(lsErrorMsg);
         psError:=lsErrorMsg;
         return FAILURE;
   end;


   function GetPrevDTPoint(psSourceTable in varchar2,
			piSourceVersionID in number,
            psSourceMP_ID in varchar2,
			piSuggestedInd in number,
            pdCalDayDate in date,
			psCalHourEnding in varchar2,
			pdCalDayDatePrev out date,
			psCalHourEndingPrev out varchar2,
			pfMW out number,
			pfMVAR out number,
			pfMW_MIN out number,
			pfMVAR_MIN out number,
			pfMW_MAX out number,
			pfMVAR_MAX out number,
			psError out varchar2) return number is
/**************************************************************
   THIS WILL FIND THE PREV TIME POINT AND return CAL_DAY_DATE
   and CAL_HOUR_ENDING and all MWsfor this point

   IF NO RECORD EXISTS IT WILL RETURN -1 AND psError will be set appropriatry
**************************************************************/
	  lsErrorMsg varchar2(256);
	  liVersionID number;
	  lsSQLSource varchar2(4096);
	  lsCalHourEndingPrev varchar2(3);
	  ldCalDayDatePrev date;
	  liCount number;

      TYPE cv_typ IS REF CURSOR;
      cv cv_typ;

   begin

	  lsErrorMsg:='';

	  /*
	  dbms_output.put_line('source table: '||psSourceTable);
	  dbms_output.put_line('psSourceMP_ID: '||psSourceMP_ID);
	  dbms_output.put_line('pdCalDayDate: '||to_char(pdCalDayDate,'DD-MON-RRRR'));
	  dbms_output.put_line('psCalHourEnding: '||psCalHourEnding);
	  */

	  -- first find prev stamp from time dimension
	  -- first date itself
	  -- first try to find the prev record within same date
      select max(cal_hour_ending),count(*)
            into lsCalHourEndingPrev,liCount
	  from time_d   --ppoa.time_d
	  where pdCalDayDate=cal_day_date
		    and cal_hour_ending<psCalHourEnding;

	  -- if we came here, this is the record
      ldCalDayDatePrev:=pdCalDayDate;

	  if liCount=0 then
         ldCalDayDatePrev:=pdCalDayDate-1;
         lsCalHourEndingPrev:='24';
	  end if;

	  /*
	  dbms_output.put_line('After block');
	  dbms_output.put_line('ldCalDayDatePrev: '||to_char(ldCalDayDatePrev,'DD-MON-RRRR'));
	  dbms_output.put_line('lsCalHourEndingPrev: '||lsCalHourEndingPrev);
	  */

	  -- so at this moment we found the Prev timestamp from the time dimension
	  -- make sure the record is there in the SOURCE table

	  lsSQLSource:='select CAL_DAY_DATE,CAL_HOUR_ENDING ';

	  if piSuggestedInd=0 then
   	     lsSQLSource:=lsSQLSource||',MW,MVAR,MW_MIN,MVAR_MIN,MW_MAX,MVAR_MAX ';
	  else
   	     lsSQLSource:=lsSQLSource||',MW_SUGGESTED,MVAR_SUGGESTED,MW_MIN_SUGGESTED,MVAR_MIN_SUGGESTED,MW_MAX_SUGGESTED,MVAR_MAX_SUGGESTED ';
	  end if;

	  lsSQLSource:=lsSQLSource||'from '||psSourceTable||' ';
	  lsSQLSource:=lsSQLSource||'where MP_ID=:lsMP_ID ';
	  lsSQLSource:=lsSQLSource||'and :pdCalDayDatePrev=CAL_DAY_DATE ';
	  lsSQLSource:=lsSQLSource||'and :psCalHourEndingPrev=CAL_HOUR_ENDING ';

	  if -1<piSourceVersionID then
	     lsSQLSource:=lsSQLSource||'and version_id='||to_char(piSourceVersionID) ||' ';
	  end if;

      OPEN cv FOR lsSQLSource
	     USING  psSourceMP_ID,ldCalDayDatePrev,lsCalHourEndingPrev;
      LOOP
         FETCH cv INTO pdCalDayDatePrev,psCalHourEndingPrev,
		               pfMW,pfMVAR,pfMW_MIN,pfMVAR_MIN,pfMW_MAX,pfMVAR_MAX;
         EXIT WHEN cv%NOTFOUND;
      END LOOP;
      CLOSE cv;

	  if pdCalDayDatePrev is null then
      	 psError:='No Prev timepoint exists in the table '||psSourceTable||' for MP_ID '||psSourceMP_ID
		           ||', CAL_DAY_DATE='||to_char(ldCalDayDatePrev,'DD-MON-RRRR')
				   ||', CAL_HOUR_ENDING='||to_char(lsCalHourEndingPrev);
      	 return FAILURE;
	  end if;

	  /*
      dbms_output.put_line('    Returning pfMW: '||pfMW);
      dbms_output.put_line('              pfMVAR: '||pfMVAR);
	  dbms_output.put_line('              pfMW_MIN: '||pfMW_MIN);
	  dbms_output.put_line('              pfMVAR_MIN: '||pfMVAR_MIN);
	  dbms_output.put_line('              pfMW_MAX: '||pfMW_MAX);
	  dbms_output.put_line('              pfMVAR_MAX: '||pfMVAR_MAX);
	  */

	  return SUCCESS;

   Exception
      WHEN OTHERS THEN
         lsErrorMsg:=to_char(sqlcode)||': '||sqlerrm;
         dbms_output.put_line(lsErrorMsg);
         psError:=lsErrorMsg;
         return FAILURE;
   end;



   function GetMWsForDTPoint(psSourceTable in varchar2,
			piSourceVersionID in number,
            psSourceMP_ID in varchar2,
			piSuggestedInd in number,
            pdCalDayDate in date,
			psCalHourEnding in varchar2,
			pfMW out number,
			pfMVAR out number,
			pfMW_MIN out number,
			pfMVAR_MIN out number,
			pfMW_MAX out number,
			pfMVAR_MAX out number,
			psError out varchar2) return number is
/**************************************************************
   THIS WILL ACCEPT THE TIME POINT AND return all MWs for this point

   IF NO RECORD EXISTS IT WILL RETURN -1 AND psError will be set appropriatry
**************************************************************/
	  lsErrorMsg varchar2(256);
	  liVersionID number;
	  lsSQLSource varchar2(4096);
	  lsCalHourEndingNext varchar2(3);
	  ldCalDayDateNext date;
	  liCount number;

      TYPE cv_typ IS REF CURSOR;
      cv cv_typ;
   begin

	  lsErrorMsg:='';

	  -- so at this moment we have the timestamp from the time dimension
	  -- make sure the record is there in the SOURCE table

	  lsSQLSource:='select  ';

	  if piSuggestedInd=0 then
   	     lsSQLSource:=lsSQLSource||'MW,MVAR,MW_MIN,MVAR_MIN,MW_MAX,MVAR_MAX ';
	  else
   	     lsSQLSource:=lsSQLSource||'MW_SUGGESTED,MVAR_SUGGESTED,MW_MIN_SUGGESTED,MVAR_MIN_SUGGESTED,MW_MAX_SUGGESTED,MVAR_MAX_SUGGESTED ';
	  end if;

	  lsSQLSource:=lsSQLSource||'from '||psSourceTable||' ';
	  lsSQLSource:=lsSQLSource||'where MP_ID=:lsMP_ID ';
	  lsSQLSource:=lsSQLSource||'and :pdCalDayDateNext=CAL_DAY_DATE ';
	  lsSQLSource:=lsSQLSource||'and :psCalHourEndingNext=CAL_HOUR_ENDING ';

	  if -1<piSourceVersionID then
	     lsSQLSource:=lsSQLSource||'and version_id='||to_char(piSourceVersionID) ||' ';
	  end if;

	  liCount:=0;

      OPEN cv FOR lsSQLSource
	     USING  psSourceMP_ID,pdCalDayDate,psCalHourEnding;
      LOOP
         FETCH cv INTO pfMW,pfMVAR,pfMW_MIN,pfMVAR_MIN,pfMW_MAX,pfMVAR_MAX;
         EXIT WHEN cv%NOTFOUND;

		 liCount:=liCount+1;
		 /*
         dbms_output.put_line('    Returning pfMW: '||pfMW);
	     dbms_output.put_line('              pfMVAR: '||pfMVAR);
	     dbms_output.put_line('              pfMW_MIN: '||pfMW_MIN);
	     dbms_output.put_line('              pfMVAR_MIN: '||pfMVAR_MIN);
	     dbms_output.put_line('              pfMW_MAX: '||pfMW_MAX);
	     dbms_output.put_line('              pfMVAR_MAX: '||pfMVAR_MAX);
		 */
      END LOOP;
      CLOSE cv;

	  if liCount=0 then
      	 psError:='No timepoint exists in the table '||psSourceTable||' for MP_ID '||psSourceMP_ID
		           ||', CAL_DAY_DATE='||to_char(pdCalDayDate,'DD-MON-RRRR')
				   ||', CAL_HOUR_ENDING='||to_char(psCalHourEnding);
      	 return FAILURE;
	  end if;

      return SUCCESS;

   Exception
      WHEN OTHERS THEN
         lsErrorMsg:=to_char(sqlcode)||': '||sqlerrm;
         dbms_output.put_line(lsErrorMsg);
         psError:=lsErrorMsg;
         return FAILURE;
   end;


   function GetExistsDTPoint(psSourceTable in varchar2,
			piSourceVersionID in number,
            psSourceMP_ID in varchar2,
            pdCalDayDate in date,
			psCalHourEnding in varchar2,
			piExists out number,
			psError out varchar2) return number is
/**************************************************************
   THIS WILL ACCEPT THE TIME POINT AND check if it exists
   in psSourceTable

   IF NO RECORD EXISTS IT WILL RETURN -1 AND psError will be set appropriatry
**************************************************************/
	  lsErrorMsg varchar2(256);
	  liVersionID number;
	  lsSQLSource varchar2(4096);
	  lsCalHourEndingNext varchar2(3);
	  ldCalDayDateNext date;
	  liCount number;
	  liExists number;

      TYPE cv_typ IS REF CURSOR;
      cv cv_typ;
      --v_id employee.employee_id%TYPE;
      --v_nm employee.last_name%TYPE;
   begin

	  lsErrorMsg:='';

	  -- so at this moment we have the timestamp from the time dimension
	  -- make sure the record is there in the SOURCE table

	  lsSQLSource:='select 1 ';
	  lsSQLSource:=lsSQLSource||'from '||psSourceTable||' ';
	  lsSQLSource:=lsSQLSource||'where MP_ID=:lsMP_ID ';
	  lsSQLSource:=lsSQLSource||'and :pdCalDayDateNext=CAL_DAY_DATE ';
	  lsSQLSource:=lsSQLSource||'and :psCalHourEndingNext=CAL_HOUR_ENDING ';

	  if -1<piSourceVersionID then
	     lsSQLSource:=lsSQLSource||'and version_id='||to_char(piSourceVersionID) ||' ';
	  end if;

	  liCount:=0;

      OPEN cv FOR lsSQLSource
	     USING  psSourceMP_ID,pdCalDayDate,psCalHourEnding;
      LOOP
         FETCH cv INTO liExists;
         EXIT WHEN cv%NOTFOUND;

		 liCount:=liCount+1;
      END LOOP;
      CLOSE cv;

	  if liCount=0 then
      	 psError:='No timepoint exists in the table '||psSourceTable||' for MP_ID '||psSourceMP_ID
		           ||', CAL_DAY_DATE='||to_char(pdCalDayDate,'DD-MON-RRRR')
				   ||', CAL_HOUR_ENDING='||to_char(psCalHourEnding);
		 piExists:=0;
      	 return SUCCESS;
	  end if;

	  piExists:=1;
      return SUCCESS;

   Exception
      WHEN OTHERS THEN
         lsErrorMsg:=to_char(sqlcode)||': '||sqlerrm;
         dbms_output.put_line(lsErrorMsg);
         psError:=lsErrorMsg;
         return FAILURE;
   end;









   function RemPeakProcessSeq(psDestTable in varchar2,
                      piVersionID in number,
					  psMP_ID in varchar2,
					  piSuggestedIndDest in number,
					  psWhatToProcess in varchar2,
					  piTolerance in number,
                      pdCalDayDateStart in date,
 			          psCalHourEndingStart in varchar2,
                      pdCalDayDateEnd in date,
 			          psCalHourEndingEnd in varchar2,
                	  piSubSeqStart1 in number,
                  	  piSubSeqEnd1 in number,
                	  piSubSeqStart2 in number,
                  	  piSubSeqEnd2 in number,
                      pzCalDayDate_table in datepoint_date_tabtype,
                      pzCalHourEnding_table in datepoint_he_tabtype,
					  psError out varchar2) return number is
/**************************************************************
   THIS WILL try to create the brand new version identified by
   Destination version name/desc. It will make usual checks if version name
   is already used and if the limit of versions had been exceeded
   If everything fine it will take the data from the source table for given
   MP_ID and propagate it to the destination table.
   The parameter liSuggestedInd will tell if
   the real MW/MVAR has to be modified or SUGGESTED values

    The propagation to the destination table will be governed by the  psNameSrc1
	version which contains the tresholded derivatives. If this derivative is 0 then
	the source data will be propagated as is. For continious sets of 1 for derivatives
	it will be linearly interpolated.
 **************************************************************/
	  lsErrorMsg varchar2(256);
	  liReturn number;
	  lsSQLDest varchar2(4096);

	  liSubSeqLength1 number;
	  liSubSeqLength2 number;

	  li number;
	  li1 number;
	  lf number;
	  lfDelta number;

      ldCalDayDate date;
	  lsCalHourEnding varchar2(3);

      ldCalDayDateInterpStart date;
	  lsCalHourEndingInterpStart varchar2(3);

	  lfMW1 number;
	  lfMVAR1 number;
	  lfMW_MIN1 number;
	  lfMVAR_MIN1 number;
	  lfMW_MAX1 number;
	  lfMVAR_MAX1 number;

	  lfMW2 number;
	  lfMVAR2 number;
	  lfMW_MIN2 number;
	  lfMVAR_MIN2 number;
	  lfMW_MAX2 number;
	  lfMVAR_MAX2 number;



   begin

	  -- prepare the sql to populate interpolated value
	  lsSQLDest:='update '||psDestTable||' ';

	  if piSuggestedIndDest=0 then
   	     if psWhatToProcess='MW' then
            lsSQLDest:=lsSQLDest||'set mw=:lf ';
	     elsif psWhatToProcess='MVAR' then
            lsSQLDest:=lsSQLDest||'set mvar=:lf ';
	     elsif psWhatToProcess='MW_MIN' then
            lsSQLDest:=lsSQLDest||'set mw_min=:lf ';
	     elsif psWhatToProcess='MW_MAX' then
            lsSQLDest:=lsSQLDest||'set mw_max=:lf ';
	     elsif psWhatToProcess='MVAR_MIN' then
            lsSQLDest:=lsSQLDest||'set mvar_min=:lf ';
	     elsif psWhatToProcess='MVAR_MAX' then
            lsSQLDest:=lsSQLDest||'set mvar_max=:lf ';
	     end if;
	  else
   	     if psWhatToProcess='MW' then
            lsSQLDest:=lsSQLDest||'set mw_suggested=:lf ';
	     elsif psWhatToProcess='MVAR' then
            lsSQLDest:=lsSQLDest||'set mvar_suggested=:lf ';
	     elsif psWhatToProcess='MW_MIN' then
            lsSQLDest:=lsSQLDest||'set mw_min_suggested=:lf ';
	     elsif psWhatToProcess='MW_MAX' then
            lsSQLDest:=lsSQLDest||'set mw_max_suggested=:lf ';
	     elsif psWhatToProcess='MVAR_MIN' then
            lsSQLDest:=lsSQLDest||'set mvar_min_suggested=:lf ';
	     elsif psWhatToProcess='MVAR_MAX' then
            lsSQLDest:=lsSQLDest||'set mvar_max_suggested=:lf ';
	     end if;
	  end if;

	  lsSQLDest:=lsSQLDest||'where MP_ID=:lsMP_ID ';

	  if -1<piVersionID then
	     lsSQLDest:=lsSQLDest||'and version_id=:liVersionID ';
	  end if;

	  lsSQLDest:=lsSQLDest||'and CAL_DAY_DATE=:ldCAL_DAY_DATE ';
	  lsSQLDest:=lsSQLDest||'and CAL_HOUR_ENDING=:lsCAL_HOUR_ENDING ';

      -- process two subsequences
	  liSubSeqLength1:=piSubSeqEnd1-piSubSeqStart1+1;
	  liSubSeqLength2:=piSubSeqEnd2-piSubSeqStart2+1;

      --dbms_output.put_line('INSIDE!!!!!!!!!!!!!!!!!');

      --dbms_output.put_line('liSubSeqLength1: '|| to_char(liSubSeqLength1));
      --dbms_output.put_line('liSubSeqLength2: '|| to_char(liSubSeqLength2));

      if abs(liSubSeqLength1-liSubSeqLength2)<=piTolerance then
		 -- yes: two subsequences, almost identical in length
		 -- (within tolerance), extrapolate
		 -- first start point

         liReturn:=GetPrevDTPoint(psDestTable,
                                  piVersionID,
                                  psMP_ID,
								  piSuggestedIndDest,
                                  pdCalDayDateStart,
 			                      psCalHourEndingStart,
                                  ldCalDayDateInterpStart,
			                      lsCalHourEndingInterpStart,
			                      lfMW1,lfMVAR1,
								  lfMW_MIN1,lfMVAR_MIN1,
								  lfMW_MAX1,lfMVAR_MAX1,
 			                      psError);

    --dbms_output.put_line('Prev lf: '|| to_char(lfMW1));
         if liReturn=FAILURE then
            dbms_output.put_line(psError);
            rollback;
			return FAILURE;
         end if;

         -- then last point
         liReturn:=GetMWsForDTPoint(psDestTable,
                       		        piVersionID,
                                    psMP_ID,
									piSuggestedIndDest,
                                    pdCalDayDateEnd,
 			                        psCalHourEndingEnd,
			                        lfMW2,lfMVAR2,
									lfMW_MIN2,lfMVAR_MIN2,
									lfMW_MAX2,lfMVAR_MAX2,
 			                        psError);

  --dbms_output.put_line('Last lf: '|| to_char(lfMW2));
		 if liReturn=FAILURE then
            dbms_output.put_line(psError);
            rollback;
			return FAILURE;
		 end if;

         if psWhatToProcess='MW' then
            lfDelta:=lfMW2-lfMW1;
			lf:=lfMW1;
         elsif psWhatToProcess='MVAR' then
            lfDelta:=lfMVAR2-lfMVAR1;
		    lf:=lfMVAR1;
 	     elsif psWhatToProcess='MW_MIN' then
            lfDelta:=lfMW_MIN2-lfMW_MIN1;
			lf:=lfMW_MIN1;
	     elsif psWhatToProcess='MW_MAX' then
            lfDelta:=lfMW_MAX2-lfMW_MAX1;
		    lf:=lfMW_MAX1;
	     elsif psWhatToProcess='MVAR_MIN' then
            lfDelta:=lfMVAR_MIN2-lfMVAR_MIN1;
		    lf:=lfMVAR_MIN1;
	     elsif psWhatToProcess='MVAR_MAX' then
            lfDelta:=lfMVAR_MAX2-lfMVAR_MAX1;
		    lf:=lfMVAR_MAX1;
	     end if;

		 lfDelta:=lfDelta/(liSubSeqLength1+liSubSeqLength2);

		 li1:=piSubSeqStart1;
		 while li1<piSubSeqEnd2 loop
            lf:=lf+lfDelta;

		    -- populate the destination record
      	    execute immediate lsSQLDest
		                using lf,psMP_ID,piVersionID,
                              pzCalDayDate_table(li1),
                              pzCalHourEnding_table(li1);
--      ldCalDayDate,
--	  lsCalHourEnding;

			li1:=li1+1;
		end loop;
      end if;

	  return SUCCESS;

   EXCEPTION
      WHEN OTHERS THEN
         lsErrorMsg:=to_char(sqlcode)||': '||sqlerrm;
         dbms_output.put_line(lsErrorMsg);
         rollback;
		 psError:=lsErrorMsg;
		 return FAILURE;
   end;


















---------------------  LOCAL FUNCTIONS END -------------------------


   function LoadFull(pdStartDt in date,
                    pdEndDt in date, psError out varchar2,pnRowCount out number) return number is
/**************************************************************
   THIS WILL START A FRESH:
      * cleaning COMBINED, MODIFIED, STAGING, META_MAIN and META_VERSION
	  * then it will set a new META_MAIN record with staTus PROCESSING
	  * then it will insert into STAGING the subset of data from major star schema
	  * then it will update a META_MAIN record with STATUS and counts
**************************************************************/
	  liReturn number;
      liCount number;
	  ldSys date;
	  lsErrorMsg varchar2(256);

   begin

      liCount :=0;
	  lsErrorMsg:='';

      select sysdate into ldSys from dual;
      dbms_output.put_line('Starting: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));

      dbms_output.put_line('pdEndDt: '||to_char(pdEndDt,'DD-MON-RRRR HH24:MI:SS'));

      delete from STAGING;

	  delete from META_MAIN;

      delete from MODIFIED;

      delete from COMBINED;

	  delete from META_VERSION;

	  insert into META_MAIN(START_DT,END_DT,CREATED_DT,UPDATED_DT,STATUS,INCREMENT_TOTAL)
	  values (pdStartDt,pdEndDt,sysdate,sysdate,INPROGRESS,0);

	  commit;

      -- now fetch     
	  insert into STAGING(MEASURE_POINT_ID,MP_ID, 
	     --AREA_CODE,
         --CONNECTION_TYPE,
		 --MEASUREMENT_POINT_TYPE_CODE,
		 --INCL_IN_POD_LSB,
		 --CATEGORY, 
         CAL_DAY_DATE,CAL_HOUR_ENDING, 
		 --CAL_YEAR,
		 --CAL_MONTH_NUMBER,CAL_MONTH_SHORT_NAME,TWO_SEASON_NAME,
         --TWO_SEASON_YEAR,FOUR_SEASON_NAME,FOUR_SEASON_YEAR, 		 
         MW,MW_MAX,MW_MIN,
		 MVAR,MVAR_MAX,MVAR_MIN)	
	  select 
	     d.measurement_point_id,
		 mp.MEASUREMENT_POINT_NAME,
		 --mp.planning_area,
		 --mp.connection_level, 
		 t.local_day, 
		 t.local_hr_ending,
         sum(d.mwh), 
		 4*max(d.mwh),
		 4*min(d.mwh), 
		 sum(d.mvarh), 
		 4*max(d.mvarh), 
         4*min(d.mvarh)   
      from 
	       hub.dsm d, 
	       hub.application_time t,
		   hub.measurement_point mp 
	  where t.time_id = d.time_id
  		    and pdStartDt<=t.time_id
		    and t.time_id<pdEndDt
			and d.MEASUREMENT_POINT_ID=mp.MEASUREMENT_POINT_ID
            and d.version_stop_date_time = to_date('9999-12-31','yyyy.mm-dd')
            --and t.time_id >= to_date('2007-01-01','yyyy-mm-dd')
			--and d.record_type = 'LOD'
      group by d.measurement_point_id, mp.MEASUREMENT_POINT_NAME,t.local_day, t.local_hr_ending;

	  -- now convert hour ending from '1' to '01'
      update staging
      set cal_hour_ending='0'||cal_hour_ending
      where length(cal_hour_ending)=1;
	  
	  -- now bring the rest of time_dimension fields
      update staging
      set (CAL_YEAR,
	       CAL_MONTH_NUMBER,CAL_MONTH_SHORT_NAME,TWO_SEASON_NAME,
           TWO_SEASON_YEAR,FOUR_SEASON_NAME,FOUR_SEASON_YEAR)= 		 
      (select  t.cal_year,
               t.cal_month_number,
               t.cal_month_short_name,
               t.two_season_name,
               t.two_season_year,
               t.four_season_name,
               t.four_season_year
      from time_d t
      where t.cal_day_date=staging.cal_day_date
            and t.cal_hour_ending=staging.cal_hour_ending);
			
	  -- now bring the rest of measurement_point fields		
      update staging s
      set (
  	     AREA_CODE,
         CONNECTION_TYPE)=
      (select  
		 mp.planning_area,
		 mp.connection_level 
      from
	   		   hub.measurement_point mp 
      where s.MEASURE_POINT_ID=mp.MEASUREMENT_POINT_ID);
	  
	  
	  /*
	  insert into STAGING(MEASURE_POINT_ID,MP_ID,AREA_CODE,MEASUREMENT_POINT_TYPE_CODE,
         CONNECTION_TYPE,INCL_IN_POD_LSB,CATEGORY,
         CAL_DAY_DATE,CAL_HOUR_ENDING,CAL_YEAR,
		 CAL_MONTH_NUMBER,CAL_MONTH_SHORT_NAME,TWO_SEASON_NAME,
         TWO_SEASON_YEAR,FOUR_SEASON_NAME,FOUR_SEASON_YEAR,
         MW,MVAR,MW_MAX,MW_MIN,
         MVAR_MAX,MVAR_MIN)
	  SELECT
        /*+ index(f corp_hist_ltlf_00_i) index(t time_00_i) */
		/*
         m.measure_point_id,
         m.mp_id,
         m.area_code,
         m.measurement_point_type_code,
         m.connection_type,
         m.incl_in_pod_lsb,
         m.category,
         t.cal_day_date,
         t.cal_hour_ending,
         t.cal_year,
         t.cal_month_number,
         t.cal_month_short_name,
         t.two_season_name,
         t.two_season_year,
         t.four_season_name,
         t.four_season_year,
         f.load_mw,
         f.load_mvar,
         f.max_mwh,
         f.min_mwh,
         f.max_mvar,
         f.min_mvar
      FROM
         ppoa.corp_hist_ltlf_f f,
         ppoa.time_d t        ,
         ppoa.measure_point_d m
      WHERE t.time_wk = f.time_wk
         and m.MEASURE_POINT_WK = f.measure_point_wk
         and f.version_wk = 21
         and (t.cal_day_date >= m.effective_dt or m.effective_dt is null)
         and (t.cal_day_date <= m.expiry_dt or expiry_dt is null)
		 and m.measurement_point_type_code='DEM'
		 and m.incl_in_pod_lsb='Y'
		 and pdStartDt<=t.cal_day_date
		 and t.cal_day_date<pdEndDt;
		 */

	  liCount:=SQL%ROWCOUNT;

	  update META_MAIN
	     set UPDATED_DT=sysdate
		     ,STATUS=READY
			 ,COUNT_INITIAL=liCount
			 ,COUNT_INC_TOTAL=0
			 ,INCREMENT_TOTAL=0;

	  update META_MAIN
	     set MIN_DT=(select min(cal_day_date) from staging),
	         MAX_DT=(select max(cal_day_date) from staging);
	pnRowCount:=liCount;
      commit;

      select sysdate into ldSys from dual;
      dbms_output.put_line('     '||to_char(liCount)||' CORP_HIST_LTLF_F records have been processed');
      dbms_output.put_line('Ending: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));
	  return SUCCESS;

   EXCEPTION
      WHEN OTHERS THEN
         lsErrorMsg:=to_char(sqlcode)||': '||sqlerrm;
         dbms_output.put_line(lsErrorMsg);
         rollback;
		 psError:=lsErrorMsg;
	     return FAILURE;
   end;


   function LoadInc(psError out varchar2) return number is
/**************************************************************
   THIS WILL DO an INCREMENTAL LOAD,
   MEANING: user did a FULL load sometime ago (a month..)
   DURING THIS TIME THE STAR SCHEMA HAS BEEN CHANGED
   THOSE CHANGES HAVE TO BE MOVED INTO THE STAGING table
   MODIFIED and COMBINED won't be affected

   THE FOLLOWING IS DONE
      * start and end date and the date of last load are retrieved
	    from META_MAIN (user don't have to worry about those,
	    user just says, whatever query I executed first time, I want it again;
		hence the query where clause is stored in META_MAIN
	  * then it will update META_MAIN record with staTus PROCESSING
	  * then it will open cursor against major STAR schema with those 3 dates
	    incorporated into where clause
	  * for every cursor row it will try first to update the record and if fails,
	    then insert a record
	  * then it will update a META_MAIN record with STATUS and counts, and last updated date
**************************************************************/
      liReturn number;
      liCount number;
      liCountInsert number;
      liCountUpdate number;
	  ldSys date;
	  lsErrorMsg varchar2(256);
	  ldStartDt date;
	  ldEndDt date;
	  ldUpdatedDt date;

      lsSQLInsert varchar2(4096);
      lsSQLUpdate varchar2(4096);

	  liMeasurePointID number(10);
	  lsMPID varchar2(20);
      lsAreaCode varchar2(50);
      lsMeasurementPointTypeCode varchar2(12);
      lsConnectionType varchar2(1);
      lsInclInPodLSB char(1);
      lsCategory varchar2(10);
      ldCalDayDate date;
      lsCalHourEnding varchar2(3);
      liCalYear number(4);
      liCalMonthNumber number(2);
      lsCalMonthShortName varchar2(3);
      lsTwoSeasonName varchar2(8);
      liTwoSeasonYear number(4);
      lsFourSeasonName varchar2(8);
      liFourSeasonYear number(4);
      lfMW number;
      lfMVAR number;
      lfMHMax number;
      lfMHMin number;
      lfMVARMax number;
      lfMVARMin number;
/*
      CURSOR inc(ldStartDt in date,ldEndDt in date,ldUpdatedDt in date) IS
	  SELECT
	  */
        /*+ index(f corp_hist_ltlf_00_i) index(t time_00_i) */
		/*
         m.measure_point_id,
         m.mp_id,
         m.area_code,
         m.measurement_point_type_code,
         m.connection_type,
         m.incl_in_pod_lsb,
         m.category,
         t.cal_day_date,
         t.cal_hour_ending,
         t.cal_year,
         t.cal_month_number,
         t.cal_month_short_name,
         t.two_season_name,
         t.two_season_year,
         t.four_season_name,
         t.four_season_year,
         f.load_mw,
         f.load_mvar,
         f.max_mwh,
         f.min_mwh,
         f.max_mvar,
         f.min_mvar
      FROM
         ppoa.corp_hist_ltlf_f f,
         ppoa.time_d t        ,
         ppoa.measure_point_d m
      WHERE t.time_wk = f.time_wk
         and m.MEASURE_POINT_WK = f.measure_point_wk
         and f.version_wk = 21
         and (t.cal_day_date >= m.effective_dt or m.effective_dt is null)
         and (t.cal_day_date <= m.expiry_dt or expiry_dt is null)
		 and m.measurement_point_type_code='DEM'
		 and m.incl_in_pod_lsb='Y'
		 and ldStartDt<=t.cal_day_date
		 and t.cal_day_date<=ldEndDt
		 and ldUpdatedDt<=f.updated_dt;
*/
   begin

      liCount :=0;
      liCountInsert :=0;
      liCountUpdate :=0;
	  lsErrorMsg:='';

      select sysdate into ldSys from dual;
      dbms_output.put_line('Starting: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));

	  /*  first get parameters from META_MAIN */
	  select start_dt,end_dt,updated_dt
	     into ldStartDt,ldEndDt,ldUpdatedDt
		 from META_MAIN;

	  /*  then set META_MAIN into PROCESSING */
	  update META_MAIN
	     set STATUS=INPROGRESS;

	  commit;

	  /* prepare INSETRT and UPDATE SQL with bind vars */
      lsSQLUpdate:='update STAGING ';
	  lsSQLUpdate:=lsSQLUpdate || 'set MEASURE_POINT_ID=:piMeasurePointID, ';
	  lsSQLUpdate:=lsSQLUpdate || 'AREA_CODE=:psAreaCode, ';
      lsSQLUpdate:=lsSQLUpdate || 'MEASUREMENT_POINT_TYPE_CODE=:psMeasurementPointTypeCode, ';
      lsSQLUpdate:=lsSQLUpdate || 'CONNECTION_TYPE=:psConnectionType, ';
      lsSQLUpdate:=lsSQLUpdate || 'INCL_IN_POD_LSB=:psInclInPodLSB, ';
      lsSQLUpdate:=lsSQLUpdate || 'CATEGORY=:psCategory, ';
      lsSQLUpdate:=lsSQLUpdate || 'CAL_YEAR=:piCalYear, ';
      lsSQLUpdate:=lsSQLUpdate || 'CAL_MONTH_NUMBER=:piCalMonthNumber, ';
      lsSQLUpdate:=lsSQLUpdate || 'CAL_MONTH_SHORT_NAME=:psCalMonthShortName, ';
      lsSQLUpdate:=lsSQLUpdate || 'TWO_SEASON_NAME=:psTwoSeasonName, ';
      lsSQLUpdate:=lsSQLUpdate || 'TWO_SEASON_YEAR=:piTwoSeasonYear, ';
      lsSQLUpdate:=lsSQLUpdate || 'FOUR_SEASON_NAME=:psFourSeasonName, ';
      lsSQLUpdate:=lsSQLUpdate || 'FOUR_SEASON_YEAR=:piFourSeasonYear, ';
      lsSQLUpdate:=lsSQLUpdate || 'MW=:pfMW, ';
      lsSQLUpdate:=lsSQLUpdate || 'MVAR=:pfMVAR, ';
      lsSQLUpdate:=lsSQLUpdate || 'MW_MAX=:pfMHMax, ';
      lsSQLUpdate:=lsSQLUpdate || 'MW_MIN=:pfMHMin, ';
      lsSQLUpdate:=lsSQLUpdate || 'MVAR_MAX=:pfMVARMax, ';
      lsSQLUpdate:=lsSQLUpdate || 'MVAR_MIN=:pfMVARMin ';
      lsSQLUpdate:=lsSQLUpdate || 'where MP_ID=:piMPID ';
      lsSQLUpdate:=lsSQLUpdate || 'and CAL_DAY_DATE=:pdCalDayDate ';
      lsSQLUpdate:=lsSQLUpdate || 'and CAL_HOUR_ENDING=:psCalHourEnding ';

      lsSQLInsert:='insert into STAGING ';
	  lsSQLInsert:=lsSQLInsert || '(MEASURE_POINT_ID,';
      lsSQLInsert:=lsSQLInsert || 'MP_ID,';
	  lsSQLInsert:=lsSQLInsert || 'AREA_CODE,';
      lsSQLInsert:=lsSQLInsert || 'MEASUREMENT_POINT_TYPE_CODE,';
      lsSQLInsert:=lsSQLInsert || 'CONNECTION_TYPE,';
      lsSQLInsert:=lsSQLInsert || 'INCL_IN_POD_LSB,';
      lsSQLInsert:=lsSQLInsert || 'CATEGORY,';
      lsSQLInsert:=lsSQLInsert || 'CAL_DAY_DATE,';
      lsSQLInsert:=lsSQLInsert || 'CAL_HOUR_ENDING,';
      lsSQLInsert:=lsSQLInsert || 'CAL_YEAR,';
      lsSQLInsert:=lsSQLInsert || 'CAL_MONTH_NUMBER,';
      lsSQLInsert:=lsSQLInsert || 'CAL_MONTH_SHORT_NAME,';
      lsSQLInsert:=lsSQLInsert || 'TWO_SEASON_NAME,';
      lsSQLInsert:=lsSQLInsert || 'TWO_SEASON_YEAR,';
      lsSQLInsert:=lsSQLInsert || 'FOUR_SEASON_NAME,';
      lsSQLInsert:=lsSQLInsert || 'FOUR_SEASON_YEAR,';
      lsSQLInsert:=lsSQLInsert || 'MW,';
      lsSQLInsert:=lsSQLInsert || 'MVAR,';
      lsSQLInsert:=lsSQLInsert || 'MW_MAX,';
      lsSQLInsert:=lsSQLInsert || 'MW_MIN,';
      lsSQLInsert:=lsSQLInsert || 'MVAR_MAX,';
      lsSQLInsert:=lsSQLInsert || 'MVAR_MIN) values(';

	  lsSQLInsert:=lsSQLInsert || ':piMeasurePointID,';
      lsSQLInsert:=lsSQLInsert || ':piMPID,';
	  lsSQLInsert:=lsSQLInsert || ':psAreaCode,';
      lsSQLInsert:=lsSQLInsert || ':psMeasurementPointTypeCode,';
      lsSQLInsert:=lsSQLInsert || ':psConnectionType,';
      lsSQLInsert:=lsSQLInsert || ':psInclInPodLSB,';
      lsSQLInsert:=lsSQLInsert || ':psCategory,';
      lsSQLInsert:=lsSQLInsert || ':pdCalDayDate,';
      lsSQLInsert:=lsSQLInsert || ':psCalHourEnding,';
      lsSQLInsert:=lsSQLInsert || ':piCalYear,';
      lsSQLInsert:=lsSQLInsert || ':piCalMonthNumber,';
      lsSQLInsert:=lsSQLInsert || ':psCalMonthShortName,';
      lsSQLInsert:=lsSQLInsert || ':psTwoSeasonName,';
      lsSQLInsert:=lsSQLInsert || ':piTwoSeasonYear,';
      lsSQLInsert:=lsSQLInsert || ':psFourSeasonName,';
      lsSQLInsert:=lsSQLInsert || ':piFourSeasonYear,';
      lsSQLInsert:=lsSQLInsert || ':pfMW,';
      lsSQLInsert:=lsSQLInsert || ':pfMVAR,';
      lsSQLInsert:=lsSQLInsert || ':pfMHMax,';
      lsSQLInsert:=lsSQLInsert || ':pfMHMin,';
      lsSQLInsert:=lsSQLInsert || ':pfMVARMax,';
      lsSQLInsert:=lsSQLInsert || ':pfMVARMin';

      lsSQLInsert:=lsSQLInsert || ')';

	  /* open cursor against major STAR schema */
	  /*
      for i IN inc(ldStartDt,ldEndDt,ldUpdatedDt) LOOP
	     -- and process every record 
	     liMeasurePointID:=i.measure_point_id;
		 lsMPID:=i.MP_ID;
         lsAreaCode:=i.area_code;
         lsMeasurementPointTypeCode:=i.measurement_point_type_code;
         lsConnectionType:=i.connection_type;
         lsInclInPodLSB:=i.incl_in_pod_lsb;
         lsCategory:=i.category;
         ldCalDayDate:=i.cal_day_date;
         lsCalHourEnding:=i.cal_hour_ending;
         liCalYear:=i.cal_year;
         liCalMonthNumber:=i.cal_month_number;
         lsCalMonthShortName:=i.cal_month_short_name;
         lsTwoSeasonName:=i.two_season_name;
         liTwoSeasonYear:=i.two_season_year;
         lsFourSeasonName:=i.four_season_name;
         liFourSeasonYear:=i.four_season_year;
         lfMW:=i.load_mw;
         lfMVAR:=i.load_mvar;
         lfMHMax:=i.max_mwh;
         lfMHMin:=i.min_mwh;
         lfMVARMax:=i.max_mvar;
         lfMVARMin:=i.min_mvar;

		 -- after cursor record is fetched try to update 
		 execute immediate lsSQLUpdate
		    using liMeasurePointID,lsAreaCode,lsMeasurementPointTypeCode,
                  lsConnectionType,lsInclInPodLSB,lsCategory,
                  liCalYear,liCalMonthNumber,
                  lsCalMonthShortName,lsTwoSeasonName,
                  liTwoSeasonYear,lsFourSeasonName,liFourSeasonYear,
                  lfMW,lfMVAR,lfMHMax,lfMHMin,lfMVARMax,lfMVARMin,lsMPID,
				  ldCalDayDate,lsCalHourEnding;

         liCount:=SQL%ROWCOUNT;

		 if liCount=0 then
		    --no update happened ?? - insert! 
      		execute immediate lsSQLInsert
		       using liMeasurePointID,lsMPID,lsAreaCode,lsMeasurementPointTypeCode,
                  lsConnectionType,lsInclInPodLSB,lsCategory,
                  ldCalDayDate,lsCalHourEnding,liCalYear,liCalMonthNumber,
                  lsCalMonthShortName,lsTwoSeasonName,
                  liTwoSeasonYear,lsFourSeasonName,liFourSeasonYear,
                  lfMW,lfMVAR,lfMHMax,lfMHMin,lfMVARMax,lfMVARMin;

		    liCountInsert:=liCountInsert+1;
		 else
		    liCountUpdate:=liCountUpdate+1;
		 end if;

      end loop;
*/
	  /* update META_MAIN with status, counts, last updated date */
	  update META_MAIN
	     set UPDATED_DT=sysdate
		     ,STATUS=READY
			 ,COUNT_INC_TOTAL=COUNT_INC_TOTAL+liCountUpdate+liCountInsert
			 ,COUNT_INC_LAST=liCountUpdate+liCountInsert
			 ,INCREMENT_TOTAL=INCREMENT_TOTAL+1;

	  update META_MAIN
	     set MIN_DT=(select min(cal_day_date) from staging),
	         MAX_DT=(select max(cal_day_date) from staging);

      commit;

      select sysdate into ldSys from dual;
      dbms_output.put_line('     '||to_char(liCountUpdate+liCountInsert)||' CORP_HIST_LTLF_F records have been processed');
      dbms_output.put_line('     '||to_char(liCountUpdate)||' updates ');
      dbms_output.put_line('     '||to_char(liCountInsert)||' inserts ');
      dbms_output.put_line('Ending: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));
	  return SUCCESS;

   EXCEPTION
      WHEN OTHERS THEN
         lsErrorMsg:=to_char(sqlcode)||': '||sqlerrm;
         dbms_output.put_line(lsErrorMsg);
         rollback;
		 psError:=lsErrorMsg;
	     return FAILURE;
   end;


   function GapAnalysis(psName in string,psDesc in string,psError out varchar2) return number is
/**************************************************************
   THIS WILL go thru STAGING records trying to identify GAPS
   RESULTS WILL bE LOADED INTO THE MODIFIED table.

   THIS WILL LOAD GAP RECORDS INTO THE FRESH version of MODIFIED table,
   HENCE THE FIRST THING IT DOES, IT TRIES TO AQUIRE THE NEXT VERSION_ID FROM THE
   META_VERSION. SEQUENCE IS NOT USED, SINCE WE LIMIT VERSIONS WITH FAIRLY SMALL
   NUMBER.
**************************************************************/
	  ldSys date;
	  lsErrorMsg varchar2(256);
	  liCount number;
	  liVersionID number;
	  ldStartDt date;
	  ldEndDt date;
	  lsMPID varchar2(20);
      lsSQLInsert varchar2(4096);
      lsSQLUpdate varchar2(4096);

	  CURSOR distinct_MP IS
	  SELECT
         distinct mp_id
      FROM
         staging;

   begin

      liCount :=0;
	  lsErrorMsg:='';

      select sysdate into ldSys from dual;
      dbms_output.put_line('Starting: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));

	  -- obtain available version_id
	  liVersionID:=GetNextVersionID(psName,psError);
	  if FAILURE=liVersionID then
         dbms_output.put_line(psError);
		 return FAILURE;
	  end if;
      dbms_output.put_line('     VersionID: '||to_char(liVersionID));

	  /* first set the META_VERSION record */
	  insert into META_VERSION(VERSION_ID,VERSION_NAME,VERSION_DESC,CREATED_DT,UPDATED_DT,STATUS)
	  values (liVersionID,psName,psDesc,sysdate,sysdate,INPROGRESS);

	  commit;

	  /* now gap analysis itself. First populate TIME_NOGAP that matches user criteria */
	  /* first get parameters from META_MAIN */
	  /* META_MAIN contains two pairs of dates: start_dt,end_dt
	     and min_dt, max_dt
		 Typically those are identical. But it is possible that the first pair
		 will cover the wider interval, than the second.
		 First pair is what user requested, second pair is what really was delivered to her
		 We want to cover only existing records interval    */
	  select min_dt,max_dt
	     into ldStartDt,ldEndDt
		 from META_MAIN;

      /* first PurgeVersion */
	  delete from TIME_NOGAP;

	  delete from STAGING_NOGAP;

	  /* and populate */
	  insert into TIME_NOGAP(cal_day_date,CAL_HOUR_ENDING)
	  select cal_day_date,cal_hour_ending
	     from time_d   --ppoa.time_d
		 where ldStartDt<=cal_day_date
		       and cal_day_date<=ldEndDt;

      commit;

	  /* with TIME_NOGAP populated with every timepoint for
	  specified time interval populate STAGING_NOGAP */
      lsSQLInsert:='insert into STAGING_NOGAP ';
	  lsSQLInsert:=lsSQLInsert || '(MP_ID,';
      lsSQLInsert:=lsSQLInsert || 'CAL_DAY_DATE,';
      lsSQLInsert:=lsSQLInsert || 'CAL_HOUR_ENDING)' ;

      lsSQLInsert:=lsSQLInsert || 'select ' ;
      lsSQLInsert:=lsSQLInsert || ':piMPID,';
      lsSQLInsert:=lsSQLInsert || 'CAL_DAY_DATE,';
      lsSQLInsert:=lsSQLInsert || 'CAL_HOUR_ENDING ' ;

      lsSQLInsert:=lsSQLInsert || 'from TIME_NOGAP ';

	  for d IN distinct_MP LOOP
	     /* and process every distinct MP_ID that is loaded into STAGING  */
		 lsMPID:=d.MP_ID;

         --dbms_output.put_line('mp_id '||lsMPID);

		 /* after cursor record is fetched populate the full block of dates for this MP */
		 execute immediate lsSQLInsert
		    using lsMPID;

         /* dbms_output.put_line('mp_id '||lsMPID||' was added at: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS')); */

      end loop;

	  commit;

      /* EVERY MP_ID in STAGING table has been processed.
	  STAGING_NOGAP contains every MP_ID,time_point from the interval
	  Now time to find what records are missing in STAGING and put those into
	  MODIFIED table */
      lsSQLInsert:='insert into MODIFIED(version_id,MP_ID,CAL_DAY_DATE,CAL_HOUR_ENDING) ';

      lsSQLInsert:=lsSQLInsert || 'select ' ;
      lsSQLInsert:=lsSQLInsert || ':piVersionID,';
      lsSQLInsert:=lsSQLInsert || 'MP_ID,';
      lsSQLInsert:=lsSQLInsert || 'CAL_DAY_DATE,';
      lsSQLInsert:=lsSQLInsert || 'CAL_HOUR_ENDING ' ;
      lsSQLInsert:=lsSQLInsert || 'from STAGING_NOGAP ';

      lsSQLInsert:=lsSQLInsert || 'minus ';

      lsSQLInsert:=lsSQLInsert || 'select ' ;
      lsSQLInsert:=lsSQLInsert || ':piVersionID,';
      lsSQLInsert:=lsSQLInsert || 'MP_ID,';
      lsSQLInsert:=lsSQLInsert || 'CAL_DAY_DATE,';
      lsSQLInsert:=lsSQLInsert || 'CAL_HOUR_ENDING ' ;
      lsSQLInsert:=lsSQLInsert || 'from STAGING ';

      execute immediate lsSQLInsert
		 using liVersionID,liVersionID;

      /* MODIFIED table contains now every missing (MP,time_point)
	     BUT ALL OTHER FIELDS in MODIFIED are not populated.
	     Time now add missing columns in the MODIFIED*/

      lsSQLUpdate:='update MODIFIED m ';
	  lsSQLUpdate:=lsSQLUpdate || 'set ( ';
	  lsSQLUpdate:=lsSQLUpdate || 'm.MEASURE_POINT_ID, ';
	  lsSQLUpdate:=lsSQLUpdate || 'm.AREA_CODE, ';
--      lsSQLUpdate:=lsSQLUpdate || 'm.MEASUREMENT_POINT_TYPE_CODE, ';
      lsSQLUpdate:=lsSQLUpdate || 'm.CONNECTION_TYPE ';
--      lsSQLUpdate:=lsSQLUpdate || 'm.INCL_IN_POD_LSB, ';
--      lsSQLUpdate:=lsSQLUpdate || 'm.CATEGORY ';
      lsSQLUpdate:=lsSQLUpdate || ') ';
      lsSQLUpdate:=lsSQLUpdate || '=(select dm.measurement_point_ID, ';
	  lsSQLUpdate:=lsSQLUpdate || 'dm.planning_area, ';
--      lsSQLUpdate:=lsSQLUpdate || 'dm.MEASUREMENT_POINT_TYPE_CODE, ';
      lsSQLUpdate:=lsSQLUpdate || 'dm.CONNECTION_level ';
--      lsSQLUpdate:=lsSQLUpdate || 'dm.INCL_IN_POD_LSB, ';
--      lsSQLUpdate:=lsSQLUpdate || 'dm.CATEGORY ';
      lsSQLUpdate:=lsSQLUpdate || 'from hub.measurement_point dm ';
      lsSQLUpdate:=lsSQLUpdate || 'where m.MP_ID=dm.MEASUREMENT_POINT_NAME), ';

      lsSQLUpdate:=lsSQLUpdate || '(m.CAL_YEAR, ';
      lsSQLUpdate:=lsSQLUpdate || 'm.CAL_MONTH_NUMBER, ';
      lsSQLUpdate:=lsSQLUpdate || 'm.CAL_MONTH_SHORT_NAME, ';
      lsSQLUpdate:=lsSQLUpdate || 'm.TWO_SEASON_NAME, ';
      lsSQLUpdate:=lsSQLUpdate || 'm.TWO_SEASON_YEAR, ';
      lsSQLUpdate:=lsSQLUpdate || 'm.FOUR_SEASON_NAME, ';
      lsSQLUpdate:=lsSQLUpdate || 'm.FOUR_SEASON_YEAR) ';
      lsSQLUpdate:=lsSQLUpdate || '=(select td.CAL_YEAR, ';
      lsSQLUpdate:=lsSQLUpdate || 'td.CAL_MONTH_NUMBER, ';
      lsSQLUpdate:=lsSQLUpdate || 'td.CAL_MONTH_SHORT_NAME, ';
      lsSQLUpdate:=lsSQLUpdate || 'td.TWO_SEASON_NAME, ';
      lsSQLUpdate:=lsSQLUpdate || 'td.TWO_SEASON_YEAR, ';
      lsSQLUpdate:=lsSQLUpdate || 'td.FOUR_SEASON_NAME, ';
      lsSQLUpdate:=lsSQLUpdate || 'td.FOUR_SEASON_YEAR ';
      lsSQLUpdate:=lsSQLUpdate || 'from  time_d td ';
      lsSQLUpdate:=lsSQLUpdate || 'where m.CAL_DAY_DATE=td.CAL_DAY_DATE ';
      lsSQLUpdate:=lsSQLUpdate || 'and m.CAL_HOUR_ENDING=td.CAL_HOUR_ENDING)' ;

      lsSQLUpdate:=lsSQLUpdate || 'where m.version_id=:piVersionID ';

      execute immediate lsSQLUpdate
		 using liVersionID;

	  /* update META_VERSION with status, last updated date */
	  update META_VERSION
	     set UPDATED_DT=sysdate
		     ,STATUS=READY
		 where version_id=liVersionID;

	  commit;

      select sysdate into ldSys from dual;
      dbms_output.put_line('Ending: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));
	  return SUCCESS;

   EXCEPTION
      WHEN OTHERS THEN
         lsErrorMsg:=to_char(sqlcode)||': '||sqlerrm;
         dbms_output.put_line(lsErrorMsg);
         rollback;
		 psError:=lsErrorMsg;
		 return FAILURE;
   end;



   function GapAnalysisSetSuggested(psName in string,
            psTargetMP_ID in varchar2,
			pdTargetDtStart in date,pdTargetDtEnd in date,
			psTargetCalHourEndingStart in varchar2,psTargetCalHourEndingEnd in varchar2,
            psSourceTable in varchar2,
			piSourceVersionID in number,
            psSourceMP_ID in varchar2,
            pdSourceDtStart in date,
			psSourceCalHourEndingStart in varchar2,
			psError out varchar2) return number is
/**************************************************************
   THIS WILL BE TYPICALLY CALLED AFTER GapAnalysis
   IT WILL TAKE MODIFIED table, VERSION specified  by psName
   AND for records with specified psTargetMP_ID
   BEGINNING FROM pdTargetDTStart/psTargetCalHourEndingStart
   and ending at pdTargetDTEnd/psTargetCalHourEndingEnd
   (block of records in the MODIFIED table)
   it will start setting values,
   the values that it will take from
   psSourceTable table for psSourceMP_ID starting
   at pdSourceDTStart/psSourceCalHourEndingStart
   (source block of data, and we don't have to specify
   pdSourceDTEnd/psTargetCalHourEndingEnd
   because the length of the block is defined via the pdTargetDTStart/End dates)

   Of course many parameters should be later added as defaults:
      pdSourceDtEnd should be automaticaly calculated as the end of the block in MODIFIED
	      - contigious block for specified MP_ID starting at pdSourceDtStart
	  psSourceTable should be defaulted with STAGING
	  psSourceMP_ID should be defaulted with psTargetMP_ID
	  pdSourceDTStart should be defaulted with the first available date for this
	       psTargetMP_ID in the source table after pdTargetDtStart

    In any case it is just record by record block copy from source table to MODIFIED table.
	Open cursor on source, fetch values, update suggested
**************************************************************/
	  ldSys date;
	  lsErrorMsg varchar2(256);
	  liCount number;
	  liVersionID number;
	  ldCalDayDate date;
	  lsCalHourEnding varchar2(3);
	  ldTargetCalDayDate date;
	  lsTargetCalHourEnding varchar2(3);
	  ldSourceCalDayDate date;
	  lsSourceCalHourEnding varchar2(3);
	  liReturn number;
	  li number;
	  liEndOfSource number;

      lsSQLUpdate varchar2(4096);
	  lfMW number;
	  lfMVAR number;
	  lfMW_MIN number;
	  lfMVAR_MIN number;
	  lfMW_MAX number;
	  lfMVAR_MAX number;

      CURSOR target(liVersionID in number,
	                lsTargetMP_ID in varchar2,
	                ldTargetDtStart in date,ldTargetDtEnd in date,
                    lsTargetCalHourEndingStart in varchar2,
					lsTargetCalHourEndingEnd in varchar2) IS
	  SELECT  CAL_DAY_DATE,CAL_HOUR_ENDING
      FROM MODIFIED
      WHERE version_id = liVersionID
         and MP_ID = lsTargetMP_ID
		 and ldTargetDtStart<=CAL_DAY_DATE
		 and CAL_DAY_DATE<=ldTargetDtEnd
         and ((ldTargetDtStart=CAL_DAY_DATE   -- same day as start date, take hour ending into consideration
		           and lsTargetCalHourEndingStart<=CAL_HOUR_ENDING)
			   or
			   (ldTargetDtStart<CAL_DAY_DATE))
         and ((CAL_DAY_DATE=ldTargetDtEnd     -- same day as end date, take hour ending into consideration
		           and CAL_HOUR_ENDING<=lsTargetCalHourEndingEnd)
			   or
			   (CAL_DAY_DATE<ldTargetDtEnd))
      order by CAL_DAY_DATE,CAL_HOUR_ENDING;

   begin

	  lsErrorMsg:='';

      select sysdate into ldSys from dual;
      dbms_output.put_line('Starting: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));

	  -- first obtain version id
      liVersionID:=GetVersionID(psName,psError);
      dbms_output.put_line('     VersionID: '||to_char(liVersionID));

	  if liVersionID<0 then
         psError:='The version identified by "'||psName||'" doesn''t exists ';
	     return FAILURE;
	  end if;

	  lsSQLUpdate:='update MODIFIED ';
	  lsSQLUpdate:=lsSQLUpdate||'set MW_SUGGESTED=:lfMW ';
	  lsSQLUpdate:=lsSQLUpdate||',MVAR_SUGGESTED=:lfMVAR ';
	  lsSQLUpdate:=lsSQLUpdate||',MW_MIN_SUGGESTED=:lfMW_MIN ';
	  lsSQLUpdate:=lsSQLUpdate||',MVAR_MIN_SUGGESTED=:lfMVAR_MIN ';
	  lsSQLUpdate:=lsSQLUpdate||',MW_MAX_SUGGESTED=:lfMW_MAX ';
	  lsSQLUpdate:=lsSQLUpdate||',MVAR_MAX_SUGGESTED=:lfMVAR_MAX ';
	  lsSQLUpdate:=lsSQLUpdate||'where  version_id = :liVersionID ';
	  lsSQLUpdate:=lsSQLUpdate||'and MP_ID=:lsMP_ID ';
	  lsSQLUpdate:=lsSQLUpdate||'and :ldTargetDt=CAL_DAY_DATE ';
	  lsSQLUpdate:=lsSQLUpdate||'and :lsTargetCalHourEnding=CAL_HOUR_ENDING ';

	  -- then start looping through the cursor
	  -- based on the target data
	  ldSourceCalDayDate:=pdSourceDTStart;
	  lsSourceCalHourEnding:=psSourceCalHourEndingStart;
      liReturn:=GetMWsForDTPoint(psSourceTable,
	        piSourceVersionID,
            psSourceMP_ID,
			0,
            ldSourceCalDayDate,
			lsSourceCalHourEnding,
			lfMW,lfMVAR,lfMW_MIN,lfMVAR_MIN,lfMW_MAX,lfMVAR_MAX,
			psError);

      if liReturn=FAILURE then
         return FAILURE;
      end if;

      liEndOfSource:=0;
      liCount :=0;
      for t IN target(liVersionID,
	                psTargetMP_ID,
	                pdTargetDtStart,pdTargetDtEnd,
                    psTargetCalHourEndingStart,
					psTargetCalHourEndingEnd) loop

	     /* and process every target record */
         ldTargetCalDayDate:=t.CAL_DAY_DATE;
	     lsTargetCalHourEnding:=t.CAL_HOUR_ENDING;

		 /*
		 dbms_output.put_line('ldSourceCalDayDate: '||to_char(ldSourceCalDayDate,'DD-MON-RRRR')
		                       ||' lsSourceCalHourEnding '||lsSourceCalHourEnding);
		 dbms_output.put_line('ldTargetCalDayDate: '||to_char(ldTargetCalDayDate,'DD-MON-RRRR')
		                       ||' lsTargetCalHourEnding '||lsTargetCalHourEnding);
		 dbms_output.put_line('lfMW: '||to_char(lfMW));
		 dbms_output.put_line('lfMW_MAX: '||to_char(lfMW_MAX));
		 dbms_output.put_line('lfMW_MIN: '||to_char(lfMW_MIN));
		 */
         if liEndOfSource=1 then
		    -- means that on previous itteration we weren't able
			-- to find next timepoint on the source table
		    return FAILURE;
		 end if;

         -- update the source data using the target data
		 /*
		 dbms_output.put_line('length: '||to_char(length(lsSQLUpdate)));
		 dbms_output.put_line('s: '||substr(lsSQLUpdate,1,250));
		 dbms_output.put_line('s1: '||substr(lsSQLUpdate,250));
		 */
		 EXECUTE IMMEDIATE lsSQLUpdate
		    using lfMW,lfMVAR,lfMW_MIN,lfMVAR_MIN,lfMW_MAX,lfMVAR_MAX,
			      liVersionID, psTargetMP_ID, ldTargetCalDayDate,lsTargetCalHourEnding;

		 li:=sql%rowcount;
		 dbms_output.put_line('Updated: '||to_char(li));

		 -- after processing is done and we ready to move on to the
		 -- next target point in the loop obtain the next source point first
	     ldCalDayDate:=ldSourceCalDayDate;
	     lsCalHourEnding:=lsSourceCalHourEnding;
         liReturn:=GetNextDTPoint(psSourceTable,
		    piSourceVersionID,
            psSourceMP_ID,
            ldCalDayDate,
			lsCalHourEnding,
			ldSourceCalDayDate,
			lsSourceCalHourEnding,
			lfMW,lfMVAR,lfMW_MIN,lfMVAR_MIN,lfMW_MAX,lfMVAR_MAX,
			psError);

		 if liReturn=FAILURE then
		    liEndOfSource:=1;
		 end if;

         liCount:=liCount+1;
	  end loop;

	  commit;

      select sysdate into ldSys from dual;
      dbms_output.put_line('Ending: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));
	  return SUCCESS;

   EXCEPTION
      WHEN OTHERS THEN
         lsErrorMsg:=to_char(sqlcode)||': '||sqlerrm;
         dbms_output.put_line(lsErrorMsg);
         rollback;
		 psError:=lsErrorMsg;
		 return FAILURE;
   end;


   function GapAnalysisAcceptSuggested(psName in string,
            psTargetMP_ID in varchar2,
			pdTargetDtStart in date,pdTargetDtEnd in date,
			psTargetCalHourEndingStart in varchar2,psTargetCalHourEndingEnd in varchar2,
			psError out varchar2) return number is
/**************************************************************
   THIS WILL BE TYPICALLY CALLED AFTER GapAnalysisSuggested
   IT WILL UPDATE MODIFIED table , set MWs columns with MWs_SUGGESTED
   VALUES for THE SPECIFIED MP_ID and SPECIFIED TIME PERID.
   And of course for specified version only
**************************************************************/

	  ldSys date;
	  lsErrorMsg varchar2(256);
	  li number;
	  liVersionID number;
      lsSQLUpdate varchar2(4096);


   begin
	  lsErrorMsg:='';

      select sysdate into ldSys from dual;
      dbms_output.put_line('Starting: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));

	  -- first obtain version id
      liVersionID:=GetVersionID(psName,psError);
      dbms_output.put_line('     VersionID: '||to_char(liVersionID));

	  if liVersionID<0 then
		 psError:='The version identified by "'||psName||'" doesn''t exists ';
	     return FAILURE;
	  end if;

	  lsSQLUpdate:='update MODIFIED ';
	  lsSQLUpdate:=lsSQLUpdate||'set MW=MW_SUGGESTED ';
	  lsSQLUpdate:=lsSQLUpdate||',MVAR=MVAR_SUGGESTED ';
	  lsSQLUpdate:=lsSQLUpdate||',MW_MIN=MW_MIN_SUGGESTED ';
	  lsSQLUpdate:=lsSQLUpdate||',MVAR_MIN=MVAR_MIN_SUGGESTED ';
	  lsSQLUpdate:=lsSQLUpdate||',MW_MAX=MW_MAX_SUGGESTED ';
	  lsSQLUpdate:=lsSQLUpdate||',MVAR_MAX=MVAR_MAX_SUGGESTED ';
	  lsSQLUpdate:=lsSQLUpdate||'where  version_id = :liVersionID ';
	  lsSQLUpdate:=lsSQLUpdate||'and MP_ID=:lsMP_ID ';
	  lsSQLUpdate:=lsSQLUpdate||'and :ldTargetDtStart<=CAL_DAY_DATE ';
	  lsSQLUpdate:=lsSQLUpdate||'and CAL_DAY_DATE<=:ldTargetDtEnd ';

	  lsSQLUpdate:=lsSQLUpdate||'and ((:ldTargetDtStart=CAL_DAY_DATE ';
	       -- same day as start date, take hour ending into consideration
	  lsSQLUpdate:=lsSQLUpdate||'and :lsTargetCalHourEndingStart<=CAL_HOUR_ENDING) ';
	  lsSQLUpdate:=lsSQLUpdate||'or ';
	  lsSQLUpdate:=lsSQLUpdate||'(:ldTargetDtStart<CAL_DAY_DATE)) ';

	  lsSQLUpdate:=lsSQLUpdate||'and ((CAL_DAY_DATE=:ldTargetDtEnd ';
	       -- same day as end date, take hour ending into consideration
	  lsSQLUpdate:=lsSQLUpdate||'and CAL_HOUR_ENDING<=:lsTargetCalHourEndingEnd) ';
	  lsSQLUpdate:=lsSQLUpdate||'or ';
	  lsSQLUpdate:=lsSQLUpdate||'(CAL_DAY_DATE<:ldTargetDtEnd)) ';

      EXECUTE IMMEDIATE lsSQLUpdate
		    using liVersionID,psTargetMP_ID,
			      pdTargetDtStart,pdTargetDtEnd,
				  pdTargetDtStart,psTargetCalHourEndingStart,pdTargetDtStart,
				  pdTargetDtEnd,psTargetCalHourEndingEnd,pdTargetDtEnd;

	  li:=sql%rowcount;
	  dbms_output.put_line('Updated: '||to_char(li));

	  commit;

      select sysdate into ldSys from dual;
      dbms_output.put_line('Ending: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));
	  return SUCCESS;
   EXCEPTION
      WHEN OTHERS THEN
         lsErrorMsg:=to_char(sqlcode)||': '||sqlerrm;
         dbms_output.put_line(lsErrorMsg);
         rollback;
		 psError:=lsErrorMsg;
		 return FAILURE;
   end;



   function GapValidate(psName in string,
            psSourceTable in varchar2,
            psTargetMP_ID in varchar2,
			pdTargetDtStart in date,pdTargetDtEnd in date,
			psTargetCalHourEndingStart in varchar2,psTargetCalHourEndingEnd in varchar2,
			piValid out number,
			psError out varchar2) return number is
/**************************************************************
   THIS WILL BE TYPICALLY CALLED  WHENEVER USER WANTS TO DOUBLECHECK
   THAT SPECIFIED TABLE FOR SPECIFIED VERSION AND MP_ID
   AND FOR SPECIFIED PERIOD HAS NO GAPS.
**************************************************************/
	  ldSys date;
	  lsErrorMsg varchar2(256);
	  li number;
	  liReturn number;
	  liVersionID number;
	  ldTargetCalDayDate date;
	  lsTargetCalHourEnding varchar2(3);
	  liExists number;
      lsSQLUpdate varchar2(4096);
	  lfMW number;
	  lfMW_MAX number;
	  lfMW_MIN number;
	  lfMVAR number;
	  lfMVAR_MAX number;
	  lfMVAR_MIN number;

      CURSOR time_d(ldTargetDtStart in date,ldTargetDtEnd in date,
                    lsTargetCalHourEndingStart in varchar2,
					lsTargetCalHourEndingEnd in varchar2) IS
	  SELECT  CAL_DAY_DATE,CAL_HOUR_ENDING
      FROM time_d   -- ppoa.time_d
      WHERE ldTargetDtStart<=CAL_DAY_DATE
		 and CAL_DAY_DATE<=ldTargetDtEnd
         and ((ldTargetDtStart=CAL_DAY_DATE   -- same day as start date, take hour ending into consideration
		           and lsTargetCalHourEndingStart<=CAL_HOUR_ENDING)
			   or
			   (ldTargetDtStart<CAL_DAY_DATE))
         and ((CAL_DAY_DATE=ldTargetDtEnd     -- same day as end date, take hour ending into consideration
		           and CAL_HOUR_ENDING<=lsTargetCalHourEndingEnd)
			   or
			   (CAL_DAY_DATE<ldTargetDtEnd))
      order by CAL_DAY_DATE,CAL_HOUR_ENDING;

   begin
	  lsErrorMsg:='';

      select sysdate into ldSys from dual;
      dbms_output.put_line('Starting: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));

	  -- first obtain version id.
	  -- but only if psName is not empty
	  -- unlike other procs we allow empty psName but then
	  -- psSourceTable has to be set to STAGING
	  liVersionID:=-1;
	  --dbms_output.put_line('length: '||to_char(length(psName)));
	  if length(psName)=0 or psName is null then
	     if psSourceTable='STAGING' then
		    null;
		 else
		    psError:='The version parameter is left empty, and source table is not STAGING. Can''t proceed...';
	        return FAILURE;
		 end if;
	  else
         liVersionID:=GetVersionID(psName,psError);
         dbms_output.put_line('     VersionID: '||to_char(liVersionID));

	     if liVersionID<0 then
		    psError:='The version identified by "'||psName||'" doesn''t exists ';
	        return FAILURE;
	     end if;
	  end if;

	  -- so here we either have liVersionID set or it is -1
	  -- if it is set we can use psSourceTable pointing to versioned table
	  -- such as MODIFIED, COMBINED
	  -- if it is -1 we are sure that psSourceTable is set to STAGING

	  -- drive through the time dimension time points
      for t IN time_d(pdTargetDtStart,pdTargetDtEnd,
                    psTargetCalHourEndingStart,
					psTargetCalHourEndingEnd) loop

         ldTargetCalDayDate:=t.CAL_DAY_DATE;
	     lsTargetCalHourEnding:=t.CAL_HOUR_ENDING;

		 /*
		 dbms_output.put_line('ldTargetCalDayDate= '||to_char(ldTargetCalDayDate,'DD-MON-RRRR'));
		 dbms_output.put_line('lsTargetCalHourEnding= '||lsTargetCalHourEnding);
		 */

		 -- time point is obtained
		 -- check if it exists in the table
         liReturn:=GetExistsDTPoint(psSourceTable,
	        liVersionID,
            psTargetMP_ID,
            ldTargetCalDayDate,
			lsTargetCalHourEnding,liExists,
			psError);

	     if liReturn=FAILURE then
		    return FAILURE;
		 end if;

		 if liExists=0 then
		    piValid:=0;
		    return SUCCESS;
		 end if;

         -- point exists. but check that numeric values are set		 
         liReturn:=GetMWsForDTPoint(psSourceTable,
                       		        liVersionID,
                                    psTargetMP_ID,
									0,
                                    ldTargetCalDayDate,
 			                        lsTargetCalHourEnding,
			                        lfMW,lfMVAR,
									lfMW_MIN,lfMVAR_MIN,
									lfMW_MAX,lfMVAR_MAX,
 			                        psError);

		 if liReturn=FAILURE then
		    piValid:=0;
		    return SUCCESS;
		 end if;

/*		 
dbms_output.put_line('ldTargetCalDayDate: '||to_char(ldTargetCalDayDate,'dd-mon-rrrr'));
dbms_output.put_line('lsTargetCalHourEnding: '||lsTargetCalHourEnding);
if lfMW=null then
   dbms_output.put_line('=null');
elsif lfMW is null then
   dbms_output.put_line('isnull');
else
   dbms_output.put_line('lfMW: '||to_char(lfMW));
end if;
*/

         if lfMW is null or lfMW_MAX is null or lfMW_MIN is null
		    or  lfMVAR is null or lfMVAR_MAX is null or lfMVAR_MIN is null then

		    piValid:=0;
			
			psError:='Some of MW/MVAR values are null for '||to_char(ldTargetCalDayDate,'DD-MON-RRRR')||' hour ending '||lsTargetCalHourEnding;
		    return SUCCESS;
			
		 end if;

	  end loop;

      piValid:=1;

      select sysdate into ldSys from dual;
      dbms_output.put_line('Ending: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));
	  return SUCCESS;
   EXCEPTION
      WHEN OTHERS THEN
         lsErrorMsg:=to_char(sqlcode)||': '||sqlerrm;
         dbms_output.put_line(lsErrorMsg);
         rollback;
		 psError:=lsErrorMsg;
		 return FAILURE;
   end;

   function CopyVersionName(psNameSrc in string,psNameDest in string,
                            psDescDest in string,psError out varchar2) return number is
/**************************************************************
   THIS WILL try to create the brand new version identified by
   Destination version name/desc. It will make usual checks if version name
   is already used and if the limit of versions had been exceeded
   If everything fine it will copy both MODIFIED and COMBINED to this
   new version ID
**************************************************************/
	  ldSys date;
	  lsErrorMsg varchar2(256);
	  liVersionIDSrc number;
	  liVersionIDDest number;
	  liReturn number;
	  liCount number;

   begin
	  lsErrorMsg:='';

      select sysdate into ldSys from dual;
      dbms_output.put_line('Starting: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));

	  liVersionIDSrc:=GetVersionID(psNameSrc,psError);
      dbms_output.put_line('     VersionIDSrc: '||to_char(liVersionIDSrc));

	  if liVersionIDSrc <0 then
         dbms_output.put_line(psError);
		 return FAILURE;
	  end if;

	  -- obtain available version_id
	  liVersionIDDest:=GetNextVersionID(psNameDest,psError);
	  if FAILURE=liVersionIDDest then
         dbms_output.put_line(psError);
		 return FAILURE;
	  end if;
      dbms_output.put_line('     VersionIDDest: '||to_char(liVersionIDDest));

	  /* first set the META_VERSION record */
	  insert into META_VERSION(VERSION_ID,VERSION_NAME,VERSION_DESC,CREATED_DT,UPDATED_DT,STATUS)
	  values (liVersionIDDest,psNameDest,psDescDest,sysdate,sysdate,INPROGRESS);

	  commit;

	  -- now copy MODIFIED
	  insert into MODIFIED(VERSION_ID,
	       MEASURE_POINT_ID,MP_ID,AREA_CODE,MEASUREMENT_POINT_TYPE_CODE,
		   CONNECTION_TYPE,INCL_IN_POD_LSB,CATEGORY,CAL_DAY_DATE,
		   CAL_HOUR_ENDING,CAL_YEAR,CAL_MONTH_NUMBER,CAL_MONTH_SHORT_NAME,
           TWO_SEASON_NAME,TWO_SEASON_YEAR,FOUR_SEASON_NAME,FOUR_SEASON_YEAR,
           MW,MW_MAX,MW_MIN,MW_SUGGESTED,MW_MAX_SUGGESTED,MW_MIN_SUGGESTED,
           MVAR,MVAR_MAX,MVAR_MIN,MVAR_SUGGESTED,MVAR_MAX_SUGGESTED,MVAR_MIN_SUGGESTED)
	  select liVersionIDDest,
	       MEASURE_POINT_ID,MP_ID,AREA_CODE,MEASUREMENT_POINT_TYPE_CODE,
		   CONNECTION_TYPE,INCL_IN_POD_LSB,CATEGORY,CAL_DAY_DATE,
		   CAL_HOUR_ENDING,CAL_YEAR,CAL_MONTH_NUMBER,CAL_MONTH_SHORT_NAME,
           TWO_SEASON_NAME,TWO_SEASON_YEAR,FOUR_SEASON_NAME,FOUR_SEASON_YEAR,
           MW,MW_MAX,MW_MIN,MW_SUGGESTED,MW_MAX_SUGGESTED,MW_MIN_SUGGESTED,
           MVAR,MVAR_MAX,MVAR_MIN,MVAR_SUGGESTED,MVAR_MAX_SUGGESTED,MVAR_MIN_SUGGESTED
	  from MODIFIED
	  where VERSION_ID=liVersionIDSrc;

	  -- now copy COMBINED
	  insert into COMBINED(VERSION_ID,
	       MEASURE_POINT_ID,MP_ID,AREA_CODE,MEASUREMENT_POINT_TYPE_CODE,
		   CONNECTION_TYPE,INCL_IN_POD_LSB,CATEGORY,CAL_DAY_DATE,
		   CAL_HOUR_ENDING,CAL_YEAR,CAL_MONTH_NUMBER,CAL_MONTH_SHORT_NAME,
           TWO_SEASON_NAME,TWO_SEASON_YEAR,FOUR_SEASON_NAME,FOUR_SEASON_YEAR,
           MW,MW_MAX,MW_MIN,
           MVAR,MVAR_MAX,MVAR_MIN)
	  select liVersionIDDest,
	       MEASURE_POINT_ID,MP_ID,AREA_CODE,MEASUREMENT_POINT_TYPE_CODE,
		   CONNECTION_TYPE,INCL_IN_POD_LSB,CATEGORY,CAL_DAY_DATE,
		   CAL_HOUR_ENDING,CAL_YEAR,CAL_MONTH_NUMBER,CAL_MONTH_SHORT_NAME,
           TWO_SEASON_NAME,TWO_SEASON_YEAR,FOUR_SEASON_NAME,FOUR_SEASON_YEAR,
           MW,MW_MAX,MW_MIN,
           MVAR,MVAR_MAX,MVAR_MIN
	  from COMBINED
	  where VERSION_ID=liVersionIDSrc;

	  /* update META_VERSION with status, last updated date */
	  update META_VERSION
	     set UPDATED_DT=sysdate
		     ,STATUS=READY
		 where version_id=liVersionIDDest;

	  commit;

      select sysdate into ldSys from dual;
      dbms_output.put_line('Ending: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));
	  return SUCCESS;

   EXCEPTION
      WHEN OTHERS THEN
         lsErrorMsg:=to_char(sqlcode)||': '||sqlerrm;
         dbms_output.put_line(lsErrorMsg);
         rollback;
		 psError:=lsErrorMsg;
		 return FAILURE;
   end;



   function PurgeVersionName(psName in string,psError out varchar2) return number is
/**************************************************************
   THIS WILL PurgeVersion a specified MODIFIED version (version NAME is provided)
**************************************************************/
	  ldSys date;
	  lsErrorMsg varchar2(256);
	  liVersionID number;
	  liReturn number;

   begin
	  lsErrorMsg:='';

      select sysdate into ldSys from dual;
      dbms_output.put_line('Starting: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));

	  liVersionID:=GetVersionID(psName,psError);
      dbms_output.put_line('     VersionID: '||to_char(liVersionID));

	  if liVersionID <0 then
         dbms_output.put_line(psError);
		 return FAILURE;
	  else
	     liReturn:=PurgeVersionID(liVersionID,psError);
	  end if;

      select sysdate into ldSys from dual;
      dbms_output.put_line('Ending: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));
	  return SUCCESS;

   EXCEPTION
      WHEN OTHERS THEN
         lsErrorMsg:=to_char(sqlcode)||': '||sqlerrm;
         dbms_output.put_line(lsErrorMsg);
         rollback;
		 psError:=lsErrorMsg;
		 return FAILURE;
   end;






   function PurgeVersionID(piVersionID in number,psError out varchar2) return number is
/**************************************************************
   THIS WILL PurgeVersion a specified MODIFIED version (version ID is provided)
**************************************************************/
      liCount number;
      liCountRecModified number;
      liCountRecCombined number;
	  ldSys date;
	  lsErrorMsg varchar2(256);

   begin

      liCount :=0;
	  lsErrorMsg:='';

      select sysdate into ldSys from dual;
      dbms_output.put_line('Starting: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));

      liCount:=0;
	  liCountRecModified:=0;
	  liCountRecCombined:=0;

      delete from MODIFIED
      where version_id=piVersionID;

	  liCountRecModified:=SQL%ROWCOUNT;

      delete from COMBINED
      where version_id=piVersionID;

	  liCountRecCombined:=SQL%ROWCOUNT;

	  delete from META_VERSION
	  where version_id=piVersionID;

	  liCount:=SQL%ROWCOUNT;

      commit;

      select sysdate into ldSys from dual;
      dbms_output.put_line('     '||to_char(liCount)||' versions have been deleted (from META_VERSION) ');
      dbms_output.put_line('     '||to_char(liCountRecModified)||' records for this versions have been deleted (from MODIFIED) ');
      dbms_output.put_line('     '||to_char(liCountRecCombined)||' records for this versions have been deleted (from COMBINED) ');
      dbms_output.put_line('Ending: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));
	  return SUCCESS;

   EXCEPTION
      WHEN OTHERS THEN
         lsErrorMsg:=to_char(sqlcode)||': '||sqlerrm;
         dbms_output.put_line(lsErrorMsg);
         rollback;
		 psError:=lsErrorMsg;
		 return FAILURE;
   end;

   function PurgeVersionAll(psError out varchar2) return number is
/**************************************************************
   THIS WILL PurgeVersion all MODIFIED version (version ID is provided)
**************************************************************/
	  liVersionID number;
	  lsErrorMsg varchar2(256);
	  ldSys date;
	  liReturn number;

	  CURSOR version IS
	  SELECT
         version_id
      FROM
         meta_version;

   begin

	  lsErrorMsg:='';

      select sysdate into ldSys from dual;
      dbms_output.put_line('Starting: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));

      for v IN version LOOP
	     /* and process every record */
	     liVersionID:=v.version_id;

         liReturn:=PurgeVersionID(liVersionID,lsErrorMsg);
		 if liReturn=FAILURE then
		    psError:=lsErrorMsg;
			return FAILURE;
		 end if;
	  end loop;

      select sysdate into ldSys from dual;
      dbms_output.put_line('Ending: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));
	  return SUCCESS;

   EXCEPTION
      WHEN OTHERS THEN
         lsErrorMsg:=to_char(sqlcode)||': '||sqlerrm;
         dbms_output.put_line(lsErrorMsg);
         rollback;
		 psError:=lsErrorMsg;
		 return FAILURE;
   end;

   function IntersectModifiedStaging(psNameSrc in string,psNameDest in string,
                            psDescDest in string,psError out varchar2) return number is
/**************************************************************
   THIS WILL try to create the brand new version identified by
   Destination version name/desc. It will make usual checks if version name
   is already used and if the limit of versions had been exceeded
   If everything fine it will then run intersect between the specified source version ID
   and STAGING and store the result of this intersection into this new version id (MODIFIED)
**************************************************************/
	  ldSys date;
	  lsErrorMsg varchar2(256);
	  liVersionIDSrc number;
	  liVersionIDDest number;
	  liReturn number;
	  liCount number;

   begin
	  lsErrorMsg:='';

      select sysdate into ldSys from dual;
      dbms_output.put_line('Starting: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));

	  liVersionIDSrc:=GetVersionID(psNameSrc,psError);
      dbms_output.put_line('     VersionIDSrc: '||to_char(liVersionIDSrc));

	  if liVersionIDSrc <0 then
         dbms_output.put_line(psError);
		 return FAILURE;
	  end if;

	  -- obtain available version_id
	  liVersionIDDest:=GetNextVersionID(psNameDest,psError);
	  if FAILURE=liVersionIDDest then
         dbms_output.put_line(psError);
		 return FAILURE;
	  end if;
      dbms_output.put_line('     VersionID: '||to_char(liVersionIDDest));

	  /* first set the META_VERSION record */
	  insert into META_VERSION(VERSION_ID,VERSION_NAME,VERSION_DESC,CREATED_DT,UPDATED_DT,STATUS)
	  values (liVersionIDDest,psNameDest,psDescDest,sysdate,sysdate,INPROGRESS);

	  commit;

	  -- now create the intersect from MODIFIED (source version_id) and STAGING
	  -- and store it into new MODIFIED (dest version id)
	  insert into MODIFIED(VERSION_ID,
	       MP_ID,CAL_DAY_DATE,CAL_HOUR_ENDING)
	  select liVersionIDDest,MP_ID,CAL_DAY_DATE,
		   CAL_HOUR_ENDING from
	  (select MP_ID,CAL_DAY_DATE,CAL_HOUR_ENDING
		   from STAGING
      intersect
	  select MP_ID,CAL_DAY_DATE,CAL_HOUR_ENDING
	  from MODIFIED
	  where VERSION_ID=liVersionIDSrc);


	  /* update META_VERSION with status, last updated date */
	  update META_VERSION
	     set UPDATED_DT=sysdate
		     ,STATUS=READY
		 where version_id=liVersionIDDest;

	  commit;

      select sysdate into ldSys from dual;
      dbms_output.put_line('Ending: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));
	  return SUCCESS;

   EXCEPTION
      WHEN OTHERS THEN
         lsErrorMsg:=to_char(sqlcode)||': '||sqlerrm;
         dbms_output.put_line(lsErrorMsg);
         rollback;
		 psError:=lsErrorMsg;
		 return FAILURE;
   end;


   function PopulateCombined(psNameSrc in string,psError out varchar2) return number is
/**************************************************************
   THIS WILL tpopulate combined from MODIFIED and STAGING
   VERSION NAME is supplied
   COMBINED uses the same version ID

   THIS is typically called after GapAnalysis,GapAnalysisSetSuggested,
   GapAnalysisAcceptSuggested,GapValidate,IntersectModifiedStaging
**************************************************************/
	  ldSys date;
	  lsErrorMsg varchar2(256);
	  liVersionIDSrc number;
	  liReturn number;
	  liCount number;

   begin
	  lsErrorMsg:='';

      select sysdate into ldSys from dual;
      dbms_output.put_line('Starting: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));

	  liVersionIDSrc:=GetVersionID(psNameSrc,psError);
      dbms_output.put_line('     VersionIDSrc: '||to_char(liVersionIDSrc));

	  if liVersionIDSrc <0 then
         dbms_output.put_line(psError);
		 return FAILURE;
	  end if;

	  -- now copy MODIFIED
	  insert into COMBINED(VERSION_ID,
	       MEASURE_POINT_ID,MP_ID,AREA_CODE,MEASUREMENT_POINT_TYPE_CODE,
		   CONNECTION_TYPE,INCL_IN_POD_LSB,CATEGORY,CAL_DAY_DATE,
		   CAL_HOUR_ENDING,CAL_YEAR,CAL_MONTH_NUMBER,CAL_MONTH_SHORT_NAME,
           TWO_SEASON_NAME,TWO_SEASON_YEAR,FOUR_SEASON_NAME,FOUR_SEASON_YEAR,
           MW,MW_MAX,MW_MIN,MVAR,MVAR_MAX,MVAR_MIN)
	  select VERSION_ID,
	       MEASURE_POINT_ID,MP_ID,AREA_CODE,MEASUREMENT_POINT_TYPE_CODE,
		   CONNECTION_TYPE,INCL_IN_POD_LSB,CATEGORY,CAL_DAY_DATE,
		   CAL_HOUR_ENDING,CAL_YEAR,CAL_MONTH_NUMBER,CAL_MONTH_SHORT_NAME,
           TWO_SEASON_NAME,TWO_SEASON_YEAR,FOUR_SEASON_NAME,FOUR_SEASON_YEAR,
           MW,MW_MAX,MW_MIN,MVAR,MVAR_MAX,MVAR_MIN
	  from MODIFIED
	  where VERSION_ID=liVersionIDSrc;

	  -- and STAGING
	  insert into COMBINED(VERSION_ID,
	       MEASURE_POINT_ID,MP_ID,AREA_CODE,MEASUREMENT_POINT_TYPE_CODE,
		   CONNECTION_TYPE,INCL_IN_POD_LSB,CATEGORY,CAL_DAY_DATE,
		   CAL_HOUR_ENDING,CAL_YEAR,CAL_MONTH_NUMBER,CAL_MONTH_SHORT_NAME,
           TWO_SEASON_NAME,TWO_SEASON_YEAR,FOUR_SEASON_NAME,FOUR_SEASON_YEAR,
           MW,MW_MAX,MW_MIN,MVAR,MVAR_MAX,MVAR_MIN)
	  select liVersionIDSrc,
	       MEASURE_POINT_ID,MP_ID,AREA_CODE,MEASUREMENT_POINT_TYPE_CODE,
		   CONNECTION_TYPE,INCL_IN_POD_LSB,CATEGORY,CAL_DAY_DATE,
		   CAL_HOUR_ENDING,CAL_YEAR,CAL_MONTH_NUMBER,CAL_MONTH_SHORT_NAME,
           TWO_SEASON_NAME,TWO_SEASON_YEAR,FOUR_SEASON_NAME,FOUR_SEASON_YEAR,
           MW,MW_MAX,MW_MIN,MVAR,MVAR_MAX,MVAR_MIN
	  from STAGING;

	  commit;

      select sysdate into ldSys from dual;
      dbms_output.put_line('Ending: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));
	  return SUCCESS;

   EXCEPTION
      WHEN OTHERS THEN
         lsErrorMsg:=to_char(sqlcode)||': '||sqlerrm;
         dbms_output.put_line(lsErrorMsg);
         rollback;
		 psError:=lsErrorMsg;
		 return FAILURE;
   end;

   function AverageItMP(psNameSrc in string,psNameDest in string,
                      psDescDest in string,
					  psSourceTable in varchar2,psDestTable in varchar2,
					  psMP_ID in varchar2,
					  piWindowWidthHalf in number,
					  piSuggestedIndSrc in number,
					  piSuggestedIndDest in number,
					  psError out varchar2) return number is
/**************************************************************
   THIS WILL try to create the brand new version identified by
   Destination version name/desc. It will make usual checks if version name
   is already used and if the limit of versions had been exceeded
   If everything fine it will take the data from the source table for given
   MP_ID and and average it with specified window width and put it into
   the destination table. The parameter liSuggestedInd will tell if
   the real MW/MVAR has to be modified or SUGGESTED values
**************************************************************/
	  ldSys date;
	  lsErrorMsg varchar2(256);
	  liVersionIDSrc number;
	  liVersionIDDest number;
	  liReturn number;
	  lsSQLSource varchar2(4096);
	  lsSQLDest varchar2(4096);
	  liCount number;
	  li number;

      ldCalDayDate date;
	  lsCalHourEnding varchar2(3);
	  lfMW number;
	  lfMVAR number;
	  lfMW_MIN number;
	  lfMVAR_MIN number;
	  lfMW_MAX number;
	  lfMVAR_MAX number;

      liMEASURE_POINT_ID             NUMBER(10);
      lsAREA_CODE                    VARCHAR2(50);
      lsMEASUREMENT_POINT_TYPE_CODE  VARCHAR2(12);
      lsCONNECTION_TYPE              VARCHAR2(1);
      lcINCL_IN_POD_LSB              CHAR(1);
      lsCATEGORY                     VARCHAR2(10);
      liCAL_YEAR                     NUMBER(4);
      liCAL_MONTH_NUMBER             NUMBER(2);
      lsCAL_MONTH_SHORT_NAME         VARCHAR2(3);
      lsTWO_SEASON_NAME              VARCHAR2(8);
      liTWO_SEASON_YEAR              NUMBER(4);
      lsFOUR_SEASON_NAME             VARCHAR2(8);
      liFOUR_SEASON_YEAR             NUMBER(4);

      TYPE cv_typ IS REF CURSOR;
      cv cv_typ;

	  TYPE measure_tabtype IS TABLE OF number INDEX BY BINARY_INTEGER;
      mw_table measure_tabtype;
      mvar_table measure_tabtype;
      mw_min_table measure_tabtype;
      mvar_min_table measure_tabtype;
      mw_max_table measure_tabtype;
      mvar_max_table measure_tabtype;

	  TYPE datepoint_date_tabtype IS TABLE OF date INDEX BY BINARY_INTEGER;
      CalDayDate_table datepoint_date_tabtype;

	  TYPE datepoint_he_tabtype IS TABLE OF varchar2(3) INDEX BY BINARY_INTEGER;
	  CalHourEnding_table datepoint_he_tabtype;

	  TYPE MEASURE_POINT_ID_tabtype IS TABLE OF NUMBER(10) INDEX BY BINARY_INTEGER;
      MEASURE_POINT_ID  MEASURE_POINT_ID_tabtype;

	  TYPE AREA_CODE_tabtype IS TABLE OF VARCHAR2(50) INDEX BY BINARY_INTEGER;
	  AREA_CODE AREA_CODE_tabtype;

	  TYPE MEA_POINT_TYPE_CODE_tabtype IS TABLE OF VARCHAR2(12) INDEX BY BINARY_INTEGER;
	  MEASUREMENT_POINT_TYPE_CODE MEA_POINT_TYPE_CODE_tabtype;

	  TYPE CONNECTION_TYPE_tabtype IS TABLE OF VARCHAR2(1) INDEX BY BINARY_INTEGER;
      CONNECTION_TYPE CONNECTION_TYPE_tabtype;

	  TYPE INCL_IN_POD_LSB_tabtype IS TABLE OF CHAR(1) INDEX BY BINARY_INTEGER;
	  INCL_IN_POD_LSB INCL_IN_POD_LSB_tabtype;

	  TYPE CATEGORY_tabtype IS TABLE OF VARCHAR2(10) INDEX BY BINARY_INTEGER;
	  CATEGORY CATEGORY_tabtype;

	  TYPE CAL_YEAR_tabtype IS TABLE OF NUMBER(4) INDEX BY BINARY_INTEGER;
	  CAL_YEAR CAL_YEAR_tabtype;

	  TYPE CAL_MONTH_NUMBER_tabtype IS TABLE OF NUMBER(2) INDEX BY BINARY_INTEGER;
      CAL_MONTH_NUMBER CAL_MONTH_NUMBER_tabtype;

	  TYPE CAL_MONTH_SHORT_NAME_tabtype IS TABLE OF VARCHAR2(3) INDEX BY BINARY_INTEGER;
      CAL_MONTH_SHORT_NAME CAL_MONTH_SHORT_NAME_tabtype;

	  TYPE TWO_SEASON_NAME_tabtype IS TABLE OF VARCHAR2(8) INDEX BY BINARY_INTEGER;
	  TWO_SEASON_NAME TWO_SEASON_NAME_tabtype ;

	  TYPE TWO_SEASON_YEAR_tabtype IS TABLE OF NUMBER(4) INDEX BY BINARY_INTEGER;
      TWO_SEASON_YEAR TWO_SEASON_YEAR_tabtype;

	  TYPE FOUR_SEASON_NAME_tabtype IS TABLE OF VARCHAR2(8) INDEX BY BINARY_INTEGER;
	  FOUR_SEASON_NAME FOUR_SEASON_NAME_tabtype ;

	  TYPE FOUR_SEASON_YEAR_tabtype IS TABLE OF NUMBER(4) INDEX BY BINARY_INTEGER;
      FOUR_SEASON_YEAR FOUR_SEASON_YEAR_tabtype;

   begin
	  lsErrorMsg:='';

      select sysdate into ldSys from dual;
      dbms_output.put_line('Starting: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));

	  if piSuggestedIndSrc=1 then
	     if psSourceTable='MODIFIED' then
            null;
	     else
	        psError:='Wrong combination of input parameters. Suggested Indicator can be set only with Source Table set to MODIFIED.';
            dbms_output.put_line(psError);
		    return FAILURE;
         end if;
	  end if;

	  if piSuggestedIndDest=1 then
	     if psDestTable='MODIFIED' then
	        null;
         else
	        psError:='Wrong combination of input parameters. Suggested Indicator can be set only with Destination Table set to MODIFIED.';
            dbms_output.put_line(psError);
		    return FAILURE;
         end if;
	  end if;

	  liVersionIDSrc:=GetVersionID(psNameSrc,psError);
      dbms_output.put_line('     VersionIDSrc: '||to_char(liVersionIDSrc));

	  if liVersionIDSrc <0 then
         dbms_output.put_line(psError);
		 return FAILURE;
	  end if;

	  -- obtain available version_id
	  liVersionIDDest:=GetNextVersionID(psNameDest,psError);
	  if FAILURE=liVersionIDDest then
         dbms_output.put_line(psError);
		 return FAILURE;
	  end if;
      dbms_output.put_line('     VersionIDDest: '||to_char(liVersionIDDest));

	  /* first set the META_VERSION record */
	  insert into META_VERSION(VERSION_ID,VERSION_NAME,VERSION_DESC,CREATED_DT,UPDATED_DT,STATUS)
	  values (liVersionIDDest,psNameDest,psDescDest,sysdate,sysdate,INPROGRESS);

	  commit;

	  -- prepare the sql to populate average
	  lsSQLDest:='insert into '||psDestTable||'(';
	  if -1<liVersionIDDest then
	     lsSQLDest:=lsSQLDest||'version_id,';
	  end if;
	  lsSQLDest:=lsSQLDest||'MP_ID,CAL_DAY_DATE,CAL_HOUR_ENDING ';

	  lsSQLDest:=lsSQLDest||',MEASURE_POINT_ID';
	  lsSQLDest:=lsSQLDest||',AREA_CODE';
	  lsSQLDest:=lsSQLDest||',MEASUREMENT_POINT_TYPE_CODE';
	  lsSQLDest:=lsSQLDest||',CONNECTION_TYPE';
	  lsSQLDest:=lsSQLDest||',INCL_IN_POD_LSB';
	  lsSQLDest:=lsSQLDest||',CATEGORY';
	  lsSQLDest:=lsSQLDest||',CAL_YEAR';
	  lsSQLDest:=lsSQLDest||',CAL_MONTH_NUMBER';
	  lsSQLDest:=lsSQLDest||',CAL_MONTH_SHORT_NAME';
	  lsSQLDest:=lsSQLDest||',TWO_SEASON_NAME';
	  lsSQLDest:=lsSQLDest||',TWO_SEASON_YEAR';
	  lsSQLDest:=lsSQLDest||',FOUR_SEASON_NAME';
	  lsSQLDest:=lsSQLDest||',FOUR_SEASON_YEAR';

	  if piSuggestedIndDest=0 then
         lsSQLDest:=lsSQLDest||',MW,MVAR';
		 lsSQLDest:=lsSQLDest||',MW_MIN,MVAR_MIN';
		 lsSQLDest:=lsSQLDest||',MW_MAX,MVAR_MAX ';
	  else
         lsSQLDest:=lsSQLDest||',MW_SUGGESTED,MVAR_SUGGESTED';
		 lsSQLDest:=lsSQLDest||',MW_MIN_SUGGESTED,MVAR_MIN_SUGGESTED';
         lsSQLDest:=lsSQLDest||',MW_MAX_SUGGESTED,MVAR_MAX_SUGGESTED ';
	  end if;

	  lsSQLDest:=lsSQLDest||') values(';

	  if -1<liVersionIDDest then
	     lsSQLDest:=lsSQLDest||':liVersionIDDest,';
	  end if;

	  lsSQLDest:=lsSQLDest||':lsMP_ID,:ldCAL_DAY_DATE,:lsCAL_HOUR_ENDING ';

	  lsSQLDest:=lsSQLDest||',:liMEASURE_POINT_ID';
	  lsSQLDest:=lsSQLDest||',:lsAREA_CODE';
	  lsSQLDest:=lsSQLDest||',:lsMEASUREMENT_POINT_TYPE_CODE';
	  lsSQLDest:=lsSQLDest||',:lsCONNECTION_TYPE';
	  lsSQLDest:=lsSQLDest||',:lcINCL_IN_POD_LSB';
	  lsSQLDest:=lsSQLDest||',:lsCATEGORY';
	  lsSQLDest:=lsSQLDest||',:liCAL_YEAR';
	  lsSQLDest:=lsSQLDest||',:liCAL_MONTH_NUMBER';
	  lsSQLDest:=lsSQLDest||',:lsCAL_MONTH_SHORT_NAME';
	  lsSQLDest:=lsSQLDest||',:lsTWO_SEASON_NAME';
	  lsSQLDest:=lsSQLDest||',:liTWO_SEASON_YEAR';
	  lsSQLDest:=lsSQLDest||',:lsFOUR_SEASON_NAME';
	  lsSQLDest:=lsSQLDest||',:liFOUR_SEASON_YEAR';

      lsSQLDest:=lsSQLDest||',:lfMW,:lfMVAR';
      lsSQLDest:=lsSQLDest||',:lfMW_MIN,:lfMVAR_MIN';
	  lsSQLDest:=lsSQLDest||',:lfMW_MAX,:lfMVAR_MAX ';

	  lsSQLDest:=lsSQLDest||') ';

	  -- now start fetching source data
	  lsSQLSource:='select CAL_DAY_DATE,CAL_HOUR_ENDING ';

	  lsSQLSource:=lsSQLSource||',MEASURE_POINT_ID';
	  lsSQLSource:=lsSQLSource||',AREA_CODE';
	  lsSQLSource:=lsSQLSource||',MEASUREMENT_POINT_TYPE_CODE';
	  lsSQLSource:=lsSQLSource||',CONNECTION_TYPE';
	  lsSQLSource:=lsSQLSource||',INCL_IN_POD_LSB';
	  lsSQLSource:=lsSQLSource||',CATEGORY';
	  lsSQLSource:=lsSQLSource||',CAL_YEAR';
	  lsSQLSource:=lsSQLSource||',CAL_MONTH_NUMBER';
	  lsSQLSource:=lsSQLSource||',CAL_MONTH_SHORT_NAME';
	  lsSQLSource:=lsSQLSource||',TWO_SEASON_NAME';
	  lsSQLSource:=lsSQLSource||',TWO_SEASON_YEAR';
	  lsSQLSource:=lsSQLSource||',FOUR_SEASON_NAME';
	  lsSQLSource:=lsSQLSource||',FOUR_SEASON_YEAR';

	  if piSuggestedIndSrc=0 then
         lsSQLSource:=lsSQLSource||',nvl(MW,0),nvl(MVAR,0)';
		 lsSQLSource:=lsSQLSource||',nvl(MW_MIN,0),nvl(MVAR_MIN,0)';
		 lsSQLSource:=lsSQLSource||',nvl(MW_MAX,0),nvl(MVAR_MAX,0) ';
	  else
         lsSQLSource:=lsSQLSource||',nvl(MW_SUGGESTED,0),nvl(MVAR_SUGGESTED,0)';
		 lsSQLSource:=lsSQLSource||',nvl(MW_MIN_SUGGESTED,0),nvl(MVAR_MIN_SUGGESTED,0)';
         lsSQLSource:=lsSQLSource||',nvl(MW_MAX_SUGGESTED,0),nvl(MVAR_MAX_SUGGESTED,0) ';
	  end if;

	  lsSQLSource:=lsSQLSource||'from '||psSourceTable||' ';
	  lsSQLSource:=lsSQLSource||'where MP_ID=:lsMP_ID ';

	  if -1<liVersionIDSrc then
	     lsSQLSource:=lsSQLSource||'and version_id='||to_char(liVersionIDSrc) ||' ';
	  end if;

	  lsSQLSource:=lsSQLSource||'order by 1,2 ';

	  liCount:=0;
      OPEN cv FOR lsSQLSource
	     USING  psMP_ID;
      LOOP
         --dbms_output.put_line('##: '||to_char(liCount));

         FETCH cv INTO ldCalDayDate,lsCalHourEnding,
                       liMEASURE_POINT_ID,
                       lsAREA_CODE,
                       lsMEASUREMENT_POINT_TYPE_CODE,
                       lsCONNECTION_TYPE,
                       lcINCL_IN_POD_LSB,
                       lsCATEGORY,
                       liCAL_YEAR,
                       liCAL_MONTH_NUMBER,
                       lsCAL_MONTH_SHORT_NAME,
                       lsTWO_SEASON_NAME,
                       liTWO_SEASON_YEAR,
                       lsFOUR_SEASON_NAME,
                       liFOUR_SEASON_YEAR,
		               lfMW,lfMVAR,lfMW_MIN,lfMVAR_MIN,lfMW_MAX,lfMVAR_MAX;

         EXIT WHEN cv%NOTFOUND;

		 if liCount<2*piWindowWidthHalf+1 then
		    -- still filling the buffer
            --dbms_output.put_line('Filling buffer '||to_char(2*piWindowWidthHalf+1));

            mw_table(liCount):=lfMW;
            mvar_table(liCount):=lfMVAR;
            mw_min_table(liCount):=lfMW_MIN;
            mvar_min_table(liCount):=lfMVAR_MIN;
            mw_max_table(liCount):=lfMW_MAX;
            mvar_max_table(liCount):=lfMVAR_MAX;

		    CalDayDate_table(liCount):=ldCalDayDate;
            CalHourEnding_table(liCount):=lsCalHourEnding;

            MEASURE_POINT_ID(liCount):=liMEASURE_POINT_ID;
      	    AREA_CODE(liCount):=lsAREA_CODE;
	        MEASUREMENT_POINT_TYPE_CODE(liCount):=lsMEASUREMENT_POINT_TYPE_CODE;
            CONNECTION_TYPE(liCount):=lsCONNECTION_TYPE;
	        INCL_IN_POD_LSB(liCount):=lcINCL_IN_POD_LSB;
      	    CATEGORY(liCount):=lsCATEGORY;
	        CAL_YEAR(liCount):=liCAL_YEAR;
            CAL_MONTH_NUMBER(liCount):=liCAL_MONTH_NUMBER;
            CAL_MONTH_SHORT_NAME(liCount):=lsCAL_MONTH_SHORT_NAME;
	        TWO_SEASON_NAME(liCount):=lsTWO_SEASON_NAME;
            TWO_SEASON_YEAR(liCount):=liTWO_SEASON_YEAR;
	        FOUR_SEASON_NAME(liCount):=lsFOUR_SEASON_NAME;
            FOUR_SEASON_YEAR(liCount):=liFOUR_SEASON_YEAR;

		 else
		    -- buffer is already filled
			-- first shift down
		    li:=0;
			while li<2*piWindowWidthHalf loop
               --dbms_output.put_line('Shifting down '||to_char(li));

               mw_table(li):=mw_table(li+1);
               mvar_table(li):=mvar_table(li+1);
               mw_min_table(li):=mw_min_table(li+1);
               mvar_min_table(li):=mvar_min_table(li+1);
               mw_max_table(li):=mw_max_table(li+1);
               mvar_max_table(li):=mvar_max_table(li+1);

               CalDayDate_table(li):=CalDayDate_table(li+1);
               CalHourEnding_table(li):=CalHourEnding_table(li+1);

               MEASURE_POINT_ID(li):=MEASURE_POINT_ID(li+1);
         	   AREA_CODE(li):=AREA_CODE(li+1);
	           MEASUREMENT_POINT_TYPE_CODE(li):=MEASUREMENT_POINT_TYPE_CODE(li+1);
               CONNECTION_TYPE(li):=CONNECTION_TYPE(li+1);
	           INCL_IN_POD_LSB(li):=INCL_IN_POD_LSB(li+1);
         	   CATEGORY(li):=CATEGORY(li+1);
	           CAL_YEAR(li):=CAL_YEAR(li+1);
               CAL_MONTH_NUMBER(li):=CAL_MONTH_NUMBER(li+1);
               CAL_MONTH_SHORT_NAME(li):=CAL_MONTH_SHORT_NAME(li+1);
      	       TWO_SEASON_NAME(li):=TWO_SEASON_NAME(li+1);
               TWO_SEASON_YEAR(li):=TWO_SEASON_YEAR(li+1);
	           FOUR_SEASON_NAME(li):=FOUR_SEASON_NAME(li+1);
               FOUR_SEASON_YEAR(li):=FOUR_SEASON_YEAR(li+1);

			   li:=li+1;
			end loop;

			-- and then fill the last element of the array
            --dbms_output.put_line('Filling last array element '||to_char(li));

            mw_table(li):=lfMW;
            mvar_table(li):=lfMVAR;
            mw_min_table(li):=lfMW_MIN;
            mvar_min_table(li):=lfMVAR_MIN;
            mw_max_table(li):=lfMW_MAX;
            mvar_max_table(li):=lfMVAR_MAX;

            CalDayDate_table(li):=ldCalDayDate;
            CalHourEnding_table(li):=lsCalHourEnding;

            MEASURE_POINT_ID(li):=liMEASURE_POINT_ID;
      	    AREA_CODE(li):=lsAREA_CODE;
	        MEASUREMENT_POINT_TYPE_CODE(li):=lsMEASUREMENT_POINT_TYPE_CODE;
            CONNECTION_TYPE(li):=lsCONNECTION_TYPE;
	        INCL_IN_POD_LSB(li):=lcINCL_IN_POD_LSB;
      	    CATEGORY(li):=lsCATEGORY;
	        CAL_YEAR(li):=liCAL_YEAR;
            CAL_MONTH_NUMBER(li):=liCAL_MONTH_NUMBER;
            CAL_MONTH_SHORT_NAME(li):=lsCAL_MONTH_SHORT_NAME;
	        TWO_SEASON_NAME(li):=lsTWO_SEASON_NAME;
            TWO_SEASON_YEAR(li):=liTWO_SEASON_YEAR;
	        FOUR_SEASON_NAME(li):=lsFOUR_SEASON_NAME;
            FOUR_SEASON_YEAR(li):=liFOUR_SEASON_YEAR;
		 end if;

		 if 2*piWindowWidthHalf<=liCount then
		    -- we moved half width away from the beginning to start averaging
            --dbms_output.put_line('Averaging '||to_char(liCount));

			lfMW:=0;
			lfMVAR:=0;
			lfMW_MIN:=0;
			lfMVAR_MIN:=0;
			lfMW_MAX:=0;
			lfMVAR_MAX:=0;
		    li:=0;
			while li<2*piWindowWidthHalf+1 loop
               lfMW:=lfMW+mw_table(li);
               lfMVAR:=lfMVAR+mvar_table(li);
               lfMW_MIN:=lfMW_MIN+mw_min_table(li);
               lfMVAR_MIN:=lfMVAR_MIN+mvar_min_table(li);
               lfMW_MAX:=lfMW_MAX+mw_max_table(li);
               lfMVAR_MAX:=lfMVAR_MAX+mvar_max_table(li);

			   li:=li+1;
			end loop;
			li:=2*piWindowWidthHalf+1;
			lfMW:=lfMW/li;
			lfMVAR:=lfMVAR/li;
			lfMW_MIN:=lfMW_MIN/li;
			lfMVAR_MIN:=lfMVAR_MIN/li;
			lfMW_MAX:=lfMW_MAX/li;
			lfMVAR_MAX:=lfMVAR_MAX/li;

            --dbms_output.put_line(substr(lsSQLDest,1,250));
            --dbms_output.put_line(substr(lsSQLDest,251,250));
            --dbms_output.put_line(substr(lsSQLDest,501,250));

			--dbms_output.put_line(liVersionIDDest);
			--dbms_output.put_line(psMP_ID);
            --dbms_output.put_line(to_char(CalDayDate_table(piWindowWidthHalf),'DD-MON-YYYY'));
			--dbms_output.put_line(CalHourEnding_table(piWindowWidthHalf));

			-- all averages are set
			-- populate the destination record
     		execute immediate lsSQLDest
		    using liVersionIDDest,psMP_ID,
                  CalDayDate_table(piWindowWidthHalf),
                  CalHourEnding_table(piWindowWidthHalf),
                  MEASURE_POINT_ID(piWindowWidthHalf),
            	  AREA_CODE(piWindowWidthHalf),
	              MEASUREMENT_POINT_TYPE_CODE(piWindowWidthHalf),
                  CONNECTION_TYPE(piWindowWidthHalf),
      	          INCL_IN_POD_LSB(piWindowWidthHalf),
            	  CATEGORY(piWindowWidthHalf),
	              CAL_YEAR(piWindowWidthHalf),
                  CAL_MONTH_NUMBER(piWindowWidthHalf),
                  CAL_MONTH_SHORT_NAME(piWindowWidthHalf),
	              TWO_SEASON_NAME(piWindowWidthHalf),
                  TWO_SEASON_YEAR(piWindowWidthHalf),
	              FOUR_SEASON_NAME(piWindowWidthHalf),
                  FOUR_SEASON_YEAR(piWindowWidthHalf),
				  lfMW,lfMVAR,lfMW_MIN,lfMVAR_MIN,lfMW_MAX,lfMVAR_MAX;
		 end if;

		 liCount:=liCount+1;

		 --if liCount=5 then
		   -- exit;
		 --end if;
      END LOOP;
      CLOSE cv;


	  /* update META_VERSION with status, last updated date */
	  update META_VERSION
	     set UPDATED_DT=sysdate
		     ,STATUS=READY
		 where version_id=liVersionIDDest;

	  commit;

      select sysdate into ldSys from dual;
      dbms_output.put_line('Ending: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));
	  return SUCCESS;

   EXCEPTION
      WHEN OTHERS THEN
         lsErrorMsg:=to_char(sqlcode)||': '||sqlerrm;
         dbms_output.put_line(lsErrorMsg);
         rollback;
		 psError:=lsErrorMsg;
		 return FAILURE;
   end;

   function MinusItMP(psNameSrc in string,psNameSrc1 in string,
                      psNameDest in string,
                      psDescDest in string,
					  psSourceTable in varchar2,psDestTable in varchar2,
					  psMP_ID in varchar2,
					  piSuggestedIndSrc in number,
					  piSuggestedIndDest in number,
					  psError out varchar2) return number is
/**************************************************************
   THIS WILL try to create the brand new version identified by
   Destination version name/desc. It will make usual checks if version name
   is already used and if the limit of versions had been exceeded
   If everything fine it will take the data from the two source table for given
   MP_ID and substruct it one from another and put it into
   the destination table. The parameter liSuggestedInd will tell if
   the real MW/MVAR has to be substracted or SUGGESTED values
**************************************************************/
	  ldSys date;
	  lsErrorMsg varchar2(256);
	  liVersionIDSrc number;
	  liVersionIDSrc1 number;
	  liVersionIDDest number;
	  liReturn number;
	  lsSQLSource varchar2(4096);
	  lsSQLDest varchar2(4096);
	  liCount number;
	  li number;


   begin
	  lsErrorMsg:='';

      select sysdate into ldSys from dual;
      dbms_output.put_line('Starting: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));

	  if piSuggestedIndSrc=1 then
	     if psSourceTable='MODIFIED' then
            null;
	     else
	        psError:='Wrong combination of input parameters. Suggested Indicator can be set only with Source Table set to MODIFIED.';
            dbms_output.put_line(psError);
		    return FAILURE;
         end if;
	  end if;

	  if piSuggestedIndDest=1 then
	     if psDestTable='MODIFIED' then
	        null;
         else
	        psError:='Wrong combination of input parameters. Suggested Indicator can be set only with Destination Table set to MODIFIED.';
            dbms_output.put_line(psError);
		    return FAILURE;
         end if;
	  end if;

	  liVersionIDSrc:=GetVersionID(psNameSrc,psError);
      dbms_output.put_line('     VersionIDSrc: '||to_char(liVersionIDSrc));

	  if liVersionIDSrc <0 then
         dbms_output.put_line(psError);
		 return FAILURE;
	  end if;

	  liVersionIDSrc1:=GetVersionID(psNameSrc1,psError);
      dbms_output.put_line('     VersionIDSrc1: '||to_char(liVersionIDSrc1));

	  if liVersionIDSrc1 <0 then
         dbms_output.put_line(psError);
		 return FAILURE;
	  end if;

	  -- obtain available version_id
	  liVersionIDDest:=GetNextVersionID(psNameDest,psError);
	  if FAILURE=liVersionIDDest then
         dbms_output.put_line(psError);
		 return FAILURE;
	  end if;
      dbms_output.put_line('     VersionIDDest: '||to_char(liVersionIDDest));

	  /* first set the META_VERSION record */
	  insert into META_VERSION(VERSION_ID,VERSION_NAME,VERSION_DESC,CREATED_DT,UPDATED_DT,STATUS)
	  values (liVersionIDDest,psNameDest,psDescDest,sysdate,sysdate,INPROGRESS);

	  commit;

	  -- prepare the sql to populate substraction result
	  lsSQLDest:='insert into '||psDestTable||'(';
	  if -1<liVersionIDDest then
	     lsSQLDest:=lsSQLDest||'version_id,';
	  end if;
	  lsSQLDest:=lsSQLDest||'MP_ID,CAL_DAY_DATE,CAL_HOUR_ENDING ';

	  lsSQLDest:=lsSQLDest||',MEASURE_POINT_ID';
	  lsSQLDest:=lsSQLDest||',AREA_CODE';
	  lsSQLDest:=lsSQLDest||',MEASUREMENT_POINT_TYPE_CODE';
	  lsSQLDest:=lsSQLDest||',CONNECTION_TYPE';
	  lsSQLDest:=lsSQLDest||',INCL_IN_POD_LSB';
	  lsSQLDest:=lsSQLDest||',CATEGORY';
	  lsSQLDest:=lsSQLDest||',CAL_YEAR';
	  lsSQLDest:=lsSQLDest||',CAL_MONTH_NUMBER';
	  lsSQLDest:=lsSQLDest||',CAL_MONTH_SHORT_NAME';
	  lsSQLDest:=lsSQLDest||',TWO_SEASON_NAME';
	  lsSQLDest:=lsSQLDest||',TWO_SEASON_YEAR';
	  lsSQLDest:=lsSQLDest||',FOUR_SEASON_NAME';
	  lsSQLDest:=lsSQLDest||',FOUR_SEASON_YEAR';

	  if piSuggestedIndDest=0 then
         lsSQLDest:=lsSQLDest||',MW,MVAR';
		 lsSQLDest:=lsSQLDest||',MW_MIN,MVAR_MIN';
		 lsSQLDest:=lsSQLDest||',MW_MAX,MVAR_MAX ';
	  else
         lsSQLDest:=lsSQLDest||',MW_SUGGESTED,MVAR_SUGGESTED';
		 lsSQLDest:=lsSQLDest||',MW_MIN_SUGGESTED,MVAR_MIN_SUGGESTED';
         lsSQLDest:=lsSQLDest||',MW_MAX_SUGGESTED,MVAR_MAX_SUGGESTED ';
	  end if;

	  lsSQLDest:=lsSQLDest||') ';

	  lsSQLDest:=lsSQLDest||'select ';
	  if -1<liVersionIDDest then
	     lsSQLDest:=lsSQLDest||':liVersionIDDest,';
	  end if;

	  lsSQLDest:=lsSQLDest||'s1.MP_ID ';

	  lsSQLDest:=lsSQLDest||',s1.CAL_DAY_DATE,s1.CAL_HOUR_ENDING ';

	  lsSQLDest:=lsSQLDest||',s1.MEASURE_POINT_ID';
	  lsSQLDest:=lsSQLDest||',s1.AREA_CODE';
	  lsSQLDest:=lsSQLDest||',s1.MEASUREMENT_POINT_TYPE_CODE';
	  lsSQLDest:=lsSQLDest||',s1.CONNECTION_TYPE';
	  lsSQLDest:=lsSQLDest||',s1.INCL_IN_POD_LSB';
	  lsSQLDest:=lsSQLDest||',s1.CATEGORY';
	  lsSQLDest:=lsSQLDest||',s1.CAL_YEAR';
	  lsSQLDest:=lsSQLDest||',s1.CAL_MONTH_NUMBER';
	  lsSQLDest:=lsSQLDest||',s1.CAL_MONTH_SHORT_NAME';
	  lsSQLDest:=lsSQLDest||',s1.TWO_SEASON_NAME';
	  lsSQLDest:=lsSQLDest||',s1.TWO_SEASON_YEAR';
	  lsSQLDest:=lsSQLDest||',s1.FOUR_SEASON_NAME';
	  lsSQLDest:=lsSQLDest||',s1.FOUR_SEASON_YEAR';

	  if piSuggestedIndSrc=0 then
         lsSQLDest:=lsSQLDest||',nvl(s1.MW,0)-nvl(s2.MW,0),nvl(s1.MVAR,0)-nvl(s2.MVAR,0)';
		 lsSQLDest:=lsSQLDest||',nvl(s1.MW_MIN,0)-nvl(s2.MW_MIN,0),nvl(s1.MVAR_MIN,0)-nvl(s2.MVAR_MIN,0)';
		 lsSQLDest:=lsSQLDest||',nvl(s1.MW_MAX,0)-nvl(s2.MW_MAX,0),nvl(s1.MVAR_MAX,0)-nvl(s2.MVAR_MAX,0) ';
	  else
         lsSQLDest:=lsSQLDest||',nvl(s1.MW_SUGGESTED,0)-nvl(s2.MW_SUGGESTED,0),nvl(s1.MVAR_SUGGESTED,0)-nvl(s2.MVAR_SUGGESTED,0)';
		 lsSQLDest:=lsSQLDest||',nvl(s1.MW_MIN_SUGGESTED,0)-nvl(s2.MW_MIN_SUGGESTED,0),nvl(s1.MVAR_MIN_SUGGESTED,0)-nvl(s2.MVAR_MIN_SUGGESTED,0)';
         lsSQLDest:=lsSQLDest||',nvl(s1.MW_MAX_SUGGESTED,0)-nvl(s2.MW_MAX_SUGGESTED,0),nvl(s1.MVAR_MAX_SUGGESTED,0)-nvl(s2.MVAR_MAX_SUGGESTED,0) ';
	  end if;

	  lsSQLDest:=lsSQLDest||'from '||psSourceTable||' s1 ';
	  lsSQLDest:=lsSQLDest||', '||psSourceTable||' s2 ';
	  lsSQLDest:=lsSQLDest||'where s1.MP_ID=:lsMP_ID ';

	  if -1<liVersionIDSrc then
	     lsSQLDest:=lsSQLDest||'and s1.version_id='||to_char(liVersionIDSrc) ||' ';
	  end if;

	  if -1<liVersionIDSrc1 then
	     lsSQLDest:=lsSQLDest||'and s2.version_id='||to_char(liVersionIDSrc1) ||' ';
	  end if;

	  lsSQLDest:=lsSQLDest||'and s1.MP_ID=s2.MP_ID ';
	  lsSQLDest:=lsSQLDest||'and s1.CAL_DAY_DATE=s2.CAL_DAY_DATE ';
	  lsSQLDest:=lsSQLDest||'and s1.CAL_HOUR_ENDING=s2.CAL_HOUR_ENDING ';

	  execute immediate lsSQLDest
	     using liVersionIDDest,psMP_ID;

	  /* update META_VERSION with status, last updated date */
	  update META_VERSION
	     set UPDATED_DT=sysdate
		     ,STATUS=READY
		 where version_id=liVersionIDDest;

	  commit;

      select sysdate into ldSys from dual;
      dbms_output.put_line('Ending: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));
	  return SUCCESS;

   EXCEPTION
      WHEN OTHERS THEN
         lsErrorMsg:=to_char(sqlcode)||': '||sqlerrm;
         dbms_output.put_line(lsErrorMsg);
         rollback;
		 psError:=lsErrorMsg;
		 return FAILURE;
   end;

   function DerivItMP(psNameSrc in string,psNameDest in string,
                      psDescDest in string,
					  psSourceTable in varchar2,psDestTable in varchar2,
					  psMP_ID in varchar2,
					  pfThreshold in number,
					  piSuggestedIndSrc in number,
					  piSuggestedIndDest in number,
					  psError out varchar2) return number is
/**************************************************************
   THIS WILL try to create the brand new version identified by
   Destination version name/desc. It will make usual checks if version name
   is already used and if the limit of versions had been exceeded
   If everything fine it will take the data from the source table for given
   MP_ID and calculate the derivative and put it into
   the destination table. The parameter liSuggestedInd will tell if
   the real MW/MVAR has to be modified or SUGGESTED values

   The parameter piThreshold tells the program if it further should
   limit the calculated derivatives. If it is negative, e.g. -1,
   the true derivative will be calculated. If it is positive the
   threshold will be applied to calculated derivative: e.g. if the derivative
   below threshold it is set to 0, otherwise it is set to 1
**************************************************************/
	  ldSys date;
	  lsErrorMsg varchar2(256);
	  liVersionIDSrc number;
	  liVersionIDDest number;
	  liReturn number;
	  lsSQLSource varchar2(4096);
	  lsSQLDest varchar2(4096);
	  liCount number;
	  li number;

      ldCalDayDate date;
	  lsCalHourEnding varchar2(3);
	  lfMW number;
	  lfMVAR number;
	  lfMW_MIN number;
	  lfMVAR_MIN number;
	  lfMW_MAX number;
	  lfMVAR_MAX number;

      liMEASURE_POINT_ID             NUMBER(10);
      lsAREA_CODE                    VARCHAR2(50);
      lsMEASUREMENT_POINT_TYPE_CODE  VARCHAR2(12);
      lsCONNECTION_TYPE              VARCHAR2(1);
      lcINCL_IN_POD_LSB              CHAR(1);
      lsCATEGORY                     VARCHAR2(10);
      liCAL_YEAR                     NUMBER(4);
      liCAL_MONTH_NUMBER             NUMBER(2);
      lsCAL_MONTH_SHORT_NAME         VARCHAR2(3);
      lsTWO_SEASON_NAME              VARCHAR2(8);
      liTWO_SEASON_YEAR              NUMBER(4);
      lsFOUR_SEASON_NAME             VARCHAR2(8);
      liFOUR_SEASON_YEAR             NUMBER(4);

      TYPE cv_typ IS REF CURSOR;
      cv cv_typ;

	  TYPE measure_tabtype IS TABLE OF number INDEX BY BINARY_INTEGER;
      mw_table measure_tabtype;
      mvar_table measure_tabtype;
      mw_min_table measure_tabtype;
      mvar_min_table measure_tabtype;
      mw_max_table measure_tabtype;
      mvar_max_table measure_tabtype;

	  TYPE datepoint_date_tabtype IS TABLE OF date INDEX BY BINARY_INTEGER;
      CalDayDate_table datepoint_date_tabtype;

	  TYPE datepoint_he_tabtype IS TABLE OF varchar2(3) INDEX BY BINARY_INTEGER;
	  CalHourEnding_table datepoint_he_tabtype;

	  TYPE MEASURE_POINT_ID_tabtype IS TABLE OF NUMBER(10) INDEX BY BINARY_INTEGER;
      MEASURE_POINT_ID  MEASURE_POINT_ID_tabtype;

	  TYPE AREA_CODE_tabtype IS TABLE OF VARCHAR2(50) INDEX BY BINARY_INTEGER;
	  AREA_CODE AREA_CODE_tabtype;

	  TYPE MEA_POINT_TYPE_CODE_tabtype IS TABLE OF VARCHAR2(12) INDEX BY BINARY_INTEGER;
	  MEASUREMENT_POINT_TYPE_CODE MEA_POINT_TYPE_CODE_tabtype;

	  TYPE CONNECTION_TYPE_tabtype IS TABLE OF VARCHAR2(1) INDEX BY BINARY_INTEGER;
      CONNECTION_TYPE CONNECTION_TYPE_tabtype;

	  TYPE INCL_IN_POD_LSB_tabtype IS TABLE OF CHAR(1) INDEX BY BINARY_INTEGER;
	  INCL_IN_POD_LSB INCL_IN_POD_LSB_tabtype;

	  TYPE CATEGORY_tabtype IS TABLE OF VARCHAR2(10) INDEX BY BINARY_INTEGER;
	  CATEGORY CATEGORY_tabtype;

	  TYPE CAL_YEAR_tabtype IS TABLE OF NUMBER(4) INDEX BY BINARY_INTEGER;
	  CAL_YEAR CAL_YEAR_tabtype;

	  TYPE CAL_MONTH_NUMBER_tabtype IS TABLE OF NUMBER(2) INDEX BY BINARY_INTEGER;
      CAL_MONTH_NUMBER CAL_MONTH_NUMBER_tabtype;

	  TYPE CAL_MONTH_SHORT_NAME_tabtype IS TABLE OF VARCHAR2(3) INDEX BY BINARY_INTEGER;
      CAL_MONTH_SHORT_NAME CAL_MONTH_SHORT_NAME_tabtype;

	  TYPE TWO_SEASON_NAME_tabtype IS TABLE OF VARCHAR2(8) INDEX BY BINARY_INTEGER;
	  TWO_SEASON_NAME TWO_SEASON_NAME_tabtype ;

	  TYPE TWO_SEASON_YEAR_tabtype IS TABLE OF NUMBER(4) INDEX BY BINARY_INTEGER;
      TWO_SEASON_YEAR TWO_SEASON_YEAR_tabtype;

	  TYPE FOUR_SEASON_NAME_tabtype IS TABLE OF VARCHAR2(8) INDEX BY BINARY_INTEGER;
	  FOUR_SEASON_NAME FOUR_SEASON_NAME_tabtype ;

	  TYPE FOUR_SEASON_YEAR_tabtype IS TABLE OF NUMBER(4) INDEX BY BINARY_INTEGER;
      FOUR_SEASON_YEAR FOUR_SEASON_YEAR_tabtype;

   begin
	  lsErrorMsg:='';

      select sysdate into ldSys from dual;
      dbms_output.put_line('Starting: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));

	  if piSuggestedIndSrc=1 then
	     if psSourceTable='MODIFIED' then
            null;
	     else
	        psError:='Wrong combination of input parameters. Suggested Indicator can be set only with Source Table set to MODIFIED.';
            dbms_output.put_line(psError);
		    return FAILURE;
         end if;
	  end if;

	  if piSuggestedIndDest=1 then
	     if psDestTable='MODIFIED' then
	        null;
         else
	        psError:='Wrong combination of input parameters. Suggested Indicator can be set only with Destination Table set to MODIFIED.';
            dbms_output.put_line(psError);
		    return FAILURE;
         end if;
	  end if;

	  liVersionIDSrc:=GetVersionID(psNameSrc,psError);
      dbms_output.put_line('     VersionIDSrc: '||to_char(liVersionIDSrc));

	  if liVersionIDSrc <0 then
         dbms_output.put_line(psError);
		 return FAILURE;
	  end if;

	  -- obtain available version_id
	  liVersionIDDest:=GetNextVersionID(psNameDest,psError);
	  if FAILURE=liVersionIDDest then
         dbms_output.put_line(psError);
		 return FAILURE;
	  end if;
      dbms_output.put_line('     VersionIDDest: '||to_char(liVersionIDDest));

	  /* first set the META_VERSION record */
	  insert into META_VERSION(VERSION_ID,VERSION_NAME,VERSION_DESC,CREATED_DT,UPDATED_DT,STATUS)
	  values (liVersionIDDest,psNameDest,psDescDest,sysdate,sysdate,INPROGRESS);

	  commit;

	  -- prepare the sql to populate derivative
	  lsSQLDest:='insert into '||psDestTable||'(';
	  if -1<liVersionIDDest then
	     lsSQLDest:=lsSQLDest||'version_id,';
	  end if;
	  lsSQLDest:=lsSQLDest||'MP_ID,CAL_DAY_DATE,CAL_HOUR_ENDING ';

	  lsSQLDest:=lsSQLDest||',MEASURE_POINT_ID';
	  lsSQLDest:=lsSQLDest||',AREA_CODE';
	  lsSQLDest:=lsSQLDest||',MEASUREMENT_POINT_TYPE_CODE';
	  lsSQLDest:=lsSQLDest||',CONNECTION_TYPE';
	  lsSQLDest:=lsSQLDest||',INCL_IN_POD_LSB';
	  lsSQLDest:=lsSQLDest||',CATEGORY';
	  lsSQLDest:=lsSQLDest||',CAL_YEAR';
	  lsSQLDest:=lsSQLDest||',CAL_MONTH_NUMBER';
	  lsSQLDest:=lsSQLDest||',CAL_MONTH_SHORT_NAME';
	  lsSQLDest:=lsSQLDest||',TWO_SEASON_NAME';
	  lsSQLDest:=lsSQLDest||',TWO_SEASON_YEAR';
	  lsSQLDest:=lsSQLDest||',FOUR_SEASON_NAME';
	  lsSQLDest:=lsSQLDest||',FOUR_SEASON_YEAR';

	  if piSuggestedIndDest=0 then
         lsSQLDest:=lsSQLDest||',MW,MVAR';
		 lsSQLDest:=lsSQLDest||',MW_MIN,MVAR_MIN';
		 lsSQLDest:=lsSQLDest||',MW_MAX,MVAR_MAX ';
	  else
         lsSQLDest:=lsSQLDest||',MW_SUGGESTED,MVAR_SUGGESTED';
		 lsSQLDest:=lsSQLDest||',MW_MIN_SUGGESTED,MVAR_MIN_SUGGESTED';
         lsSQLDest:=lsSQLDest||',MW_MAX_SUGGESTED,MVAR_MAX_SUGGESTED ';
	  end if;

	  lsSQLDest:=lsSQLDest||') values(';

	  if -1<liVersionIDDest then
	     lsSQLDest:=lsSQLDest||':liVersionIDDest,';
	  end if;

	  lsSQLDest:=lsSQLDest||':lsMP_ID,:ldCAL_DAY_DATE,:lsCAL_HOUR_ENDING ';

	  lsSQLDest:=lsSQLDest||',:liMEASURE_POINT_ID';
	  lsSQLDest:=lsSQLDest||',:lsAREA_CODE';
	  lsSQLDest:=lsSQLDest||',:lsMEASUREMENT_POINT_TYPE_CODE';
	  lsSQLDest:=lsSQLDest||',:lsCONNECTION_TYPE';
	  lsSQLDest:=lsSQLDest||',:lcINCL_IN_POD_LSB';
	  lsSQLDest:=lsSQLDest||',:lsCATEGORY';
	  lsSQLDest:=lsSQLDest||',:liCAL_YEAR';
	  lsSQLDest:=lsSQLDest||',:liCAL_MONTH_NUMBER';
	  lsSQLDest:=lsSQLDest||',:lsCAL_MONTH_SHORT_NAME';
	  lsSQLDest:=lsSQLDest||',:lsTWO_SEASON_NAME';
	  lsSQLDest:=lsSQLDest||',:liTWO_SEASON_YEAR';
	  lsSQLDest:=lsSQLDest||',:lsFOUR_SEASON_NAME';
	  lsSQLDest:=lsSQLDest||',:liFOUR_SEASON_YEAR';

      lsSQLDest:=lsSQLDest||',:lfMW,:lfMVAR';
      lsSQLDest:=lsSQLDest||',:lfMW_MIN,:lfMVAR_MIN';
	  lsSQLDest:=lsSQLDest||',:lfMW_MAX,:lfMVAR_MAX ';

	  lsSQLDest:=lsSQLDest||') ';

	  -- now start fetching source data
	  lsSQLSource:='select CAL_DAY_DATE,CAL_HOUR_ENDING ';

	  lsSQLSource:=lsSQLSource||',MEASURE_POINT_ID';
	  lsSQLSource:=lsSQLSource||',AREA_CODE';
	  lsSQLSource:=lsSQLSource||',MEASUREMENT_POINT_TYPE_CODE';
	  lsSQLSource:=lsSQLSource||',CONNECTION_TYPE';
	  lsSQLSource:=lsSQLSource||',INCL_IN_POD_LSB';
	  lsSQLSource:=lsSQLSource||',CATEGORY';
	  lsSQLSource:=lsSQLSource||',CAL_YEAR';
	  lsSQLSource:=lsSQLSource||',CAL_MONTH_NUMBER';
	  lsSQLSource:=lsSQLSource||',CAL_MONTH_SHORT_NAME';
	  lsSQLSource:=lsSQLSource||',TWO_SEASON_NAME';
	  lsSQLSource:=lsSQLSource||',TWO_SEASON_YEAR';
	  lsSQLSource:=lsSQLSource||',FOUR_SEASON_NAME';
	  lsSQLSource:=lsSQLSource||',FOUR_SEASON_YEAR';

	  if piSuggestedIndSrc=0 then
         lsSQLSource:=lsSQLSource||',nvl(MW,0),nvl(MVAR,0)';
		 lsSQLSource:=lsSQLSource||',nvl(MW_MIN,0),nvl(MVAR_MIN,0)';
		 lsSQLSource:=lsSQLSource||',nvl(MW_MAX,0),nvl(MVAR_MAX,0) ';
	  else
         lsSQLSource:=lsSQLSource||',nvl(MW_SUGGESTED,0),nvl(MVAR_SUGGESTED,0)';
		 lsSQLSource:=lsSQLSource||',nvl(MW_MIN_SUGGESTED,0),nvl(MVAR_MIN_SUGGESTED,0)';
         lsSQLSource:=lsSQLSource||',nvl(MW_MAX_SUGGESTED,0),nvl(MVAR_MAX_SUGGESTED,0) ';
	  end if;

	  lsSQLSource:=lsSQLSource||'from '||psSourceTable||' ';
	  lsSQLSource:=lsSQLSource||'where MP_ID=:lsMP_ID ';

	  if -1<liVersionIDSrc then
	     lsSQLSource:=lsSQLSource||'and version_id='||to_char(liVersionIDSrc) ||' ';
	  end if;

	  lsSQLSource:=lsSQLSource||'order by 1,2 ';

	  liCount:=0;
      OPEN cv FOR lsSQLSource
	     USING  psMP_ID;
      LOOP
         --dbms_output.put_line('##: '||to_char(liCount));

         FETCH cv INTO ldCalDayDate,lsCalHourEnding,
                       liMEASURE_POINT_ID,
                       lsAREA_CODE,
                       lsMEASUREMENT_POINT_TYPE_CODE,
                       lsCONNECTION_TYPE,
                       lcINCL_IN_POD_LSB,
                       lsCATEGORY,
                       liCAL_YEAR,
                       liCAL_MONTH_NUMBER,
                       lsCAL_MONTH_SHORT_NAME,
                       lsTWO_SEASON_NAME,
                       liTWO_SEASON_YEAR,
                       lsFOUR_SEASON_NAME,
                       liFOUR_SEASON_YEAR,
		               lfMW,lfMVAR,lfMW_MIN,lfMVAR_MIN,lfMW_MAX,lfMVAR_MAX;

         EXIT WHEN cv%NOTFOUND;

		 if liCount<=1 then
		    -- filling the buffer for the first two records
            --dbms_output.put_line('Filling buffer ');

            mw_table(liCount):=lfMW;
            mvar_table(liCount):=lfMVAR;
            mw_min_table(liCount):=lfMW_MIN;
            mvar_min_table(liCount):=lfMVAR_MIN;
            mw_max_table(liCount):=lfMW_MAX;
            mvar_max_table(liCount):=lfMVAR_MAX;

		    CalDayDate_table(liCount):=ldCalDayDate;
            CalHourEnding_table(liCount):=lsCalHourEnding;

            MEASURE_POINT_ID(liCount):=liMEASURE_POINT_ID;
      	    AREA_CODE(liCount):=lsAREA_CODE;
	        MEASUREMENT_POINT_TYPE_CODE(liCount):=lsMEASUREMENT_POINT_TYPE_CODE;
            CONNECTION_TYPE(liCount):=lsCONNECTION_TYPE;
	        INCL_IN_POD_LSB(liCount):=lcINCL_IN_POD_LSB;
      	    CATEGORY(liCount):=lsCATEGORY;
	        CAL_YEAR(liCount):=liCAL_YEAR;
            CAL_MONTH_NUMBER(liCount):=liCAL_MONTH_NUMBER;
            CAL_MONTH_SHORT_NAME(liCount):=lsCAL_MONTH_SHORT_NAME;
	        TWO_SEASON_NAME(liCount):=lsTWO_SEASON_NAME;
            TWO_SEASON_YEAR(liCount):=liTWO_SEASON_YEAR;
	        FOUR_SEASON_NAME(liCount):=lsFOUR_SEASON_NAME;
            FOUR_SEASON_YEAR(liCount):=liFOUR_SEASON_YEAR;

		 else
		    -- buffer is already filled
			-- first shift down
		    li:=0;
			while li<1 loop
               --dbms_output.put_line('Shifting down '||to_char(li));

               mw_table(li):=mw_table(li+1);
               mvar_table(li):=mvar_table(li+1);
               mw_min_table(li):=mw_min_table(li+1);
               mvar_min_table(li):=mvar_min_table(li+1);
               mw_max_table(li):=mw_max_table(li+1);
               mvar_max_table(li):=mvar_max_table(li+1);

               CalDayDate_table(li):=CalDayDate_table(li+1);
               CalHourEnding_table(li):=CalHourEnding_table(li+1);

               MEASURE_POINT_ID(li):=MEASURE_POINT_ID(li+1);
         	   AREA_CODE(li):=AREA_CODE(li+1);
	           MEASUREMENT_POINT_TYPE_CODE(li):=MEASUREMENT_POINT_TYPE_CODE(li+1);
               CONNECTION_TYPE(li):=CONNECTION_TYPE(li+1);
	           INCL_IN_POD_LSB(li):=INCL_IN_POD_LSB(li+1);
         	   CATEGORY(li):=CATEGORY(li+1);
	           CAL_YEAR(li):=CAL_YEAR(li+1);
               CAL_MONTH_NUMBER(li):=CAL_MONTH_NUMBER(li+1);
               CAL_MONTH_SHORT_NAME(li):=CAL_MONTH_SHORT_NAME(li+1);
      	       TWO_SEASON_NAME(li):=TWO_SEASON_NAME(li+1);
               TWO_SEASON_YEAR(li):=TWO_SEASON_YEAR(li+1);
	           FOUR_SEASON_NAME(li):=FOUR_SEASON_NAME(li+1);
               FOUR_SEASON_YEAR(li):=FOUR_SEASON_YEAR(li+1);

			   li:=li+1;
			end loop;

			-- and then fill the last element of the array
            --dbms_output.put_line('Filling last array element '||to_char(li));

            mw_table(li):=lfMW;
            mvar_table(li):=lfMVAR;
            mw_min_table(li):=lfMW_MIN;
            mvar_min_table(li):=lfMVAR_MIN;
            mw_max_table(li):=lfMW_MAX;
            mvar_max_table(li):=lfMVAR_MAX;

            CalDayDate_table(li):=ldCalDayDate;
            CalHourEnding_table(li):=lsCalHourEnding;

            MEASURE_POINT_ID(li):=liMEASURE_POINT_ID;
      	    AREA_CODE(li):=lsAREA_CODE;
	        MEASUREMENT_POINT_TYPE_CODE(li):=lsMEASUREMENT_POINT_TYPE_CODE;
            CONNECTION_TYPE(li):=lsCONNECTION_TYPE;
	        INCL_IN_POD_LSB(li):=lcINCL_IN_POD_LSB;
      	    CATEGORY(li):=lsCATEGORY;
	        CAL_YEAR(li):=liCAL_YEAR;
            CAL_MONTH_NUMBER(li):=liCAL_MONTH_NUMBER;
            CAL_MONTH_SHORT_NAME(li):=lsCAL_MONTH_SHORT_NAME;
	        TWO_SEASON_NAME(li):=lsTWO_SEASON_NAME;
            TWO_SEASON_YEAR(li):=liTWO_SEASON_YEAR;
	        FOUR_SEASON_NAME(li):=lsFOUR_SEASON_NAME;
            FOUR_SEASON_YEAR(li):=liFOUR_SEASON_YEAR;
		 end if;

		 if 0<liCount then
		    -- to calculate the derivative we need the prev. record to be already fetched
            --dbms_output.put_line('Deriv: '||to_char(liCount));

		    li:=0;

			-- calculate derivative now
            lfMW:=mw_table(li+1)-mw_table(li);
            lfMVAR:=mvar_table(li+1)-mvar_table(li);
            lfMW_MIN:=mw_min_table(li+1)-mw_min_table(li);
            lfMVAR_MIN:=mvar_min_table(li+1)-mvar_min_table(li);
            lfMW_MAX:=mw_max_table(li+1)-mw_max_table(li);
            lfMVAR_MAX:=mvar_max_table(li+1)-mvar_max_table(li);

			if pfThreshold<0 then
			   null;
			else
			   if abs(lfMW)<pfThreshold then
			      lfMW:=0;
			   else
			      if 0<=lfMW then
   			         lfMW:=1;
				  else
   			         lfMW:=-1;
				  end if;
			   end if;

			   if abs(lfMVAR)<pfThreshold then
			      lfMVAR:=0;
			   else
			      if 0<=lfMVAR then
   			         lfMVAR:=1;
				  else
   			         lfMVAR:=-1;
				  end if;
			   end if;

			   if abs(lfMW_MIN)<pfThreshold then
			      lfMW_MIN:=0;
			   else
			      if 0<=lfMW_MIN then
			         lfMW_MIN:=1;
				  else
			         lfMW_MIN:=-1;
				  end if;
			   end if;

			   if abs(lfMVAR_MIN)<pfThreshold then
			      lfMVAR_MIN:=0;
			   else
			      if 0<=lfMVAR_MIN then
			         lfMVAR_MIN:=1;
				  else
			         lfMVAR_MIN:=-1;
				  end if;
			   end if;

			   if abs(lfMW_MAX)<pfThreshold then
			      lfMW_MAX:=0;
			   else
			      if 0<=lfMW_MAX then
			         lfMW_MAX:=1;
				  else
			         lfMW_MAX:=-1;
				  end if;
			   end if;

			   if abs(lfMVAR_MAX)<pfThreshold then
			      lfMVAR_MAX:=0;
			   else
			      if 0<=lfMVAR_MAX then
			         lfMVAR_MAX:=1;
				  else
			         lfMVAR_MAX:=-1;
				  end if;
			   end if;
			end if;

            --dbms_output.put_line(substr(lsSQLDest,1,250));
            --dbms_output.put_line(substr(lsSQLDest,251,250));
            --dbms_output.put_line(substr(lsSQLDest,501,250));

			--dbms_output.put_line(liVersionIDDest);
			--dbms_output.put_line(psMP_ID);
            --dbms_output.put_line(to_char(CalDayDate_table(piWindowWidthHalf),'DD-MON-YYYY'));
			--dbms_output.put_line(CalHourEnding_table(piWindowWidthHalf));

			-- all aderivatives are set
			-- populate the destination record
     		execute immediate lsSQLDest
		    using liVersionIDDest,psMP_ID,
                  CalDayDate_table(1),
                  CalHourEnding_table(1),
                  MEASURE_POINT_ID(1),
            	  AREA_CODE(1),
	              MEASUREMENT_POINT_TYPE_CODE(1),
                  CONNECTION_TYPE(1),
      	          INCL_IN_POD_LSB(1),
            	  CATEGORY(1),
	              CAL_YEAR(1),
                  CAL_MONTH_NUMBER(1),
                  CAL_MONTH_SHORT_NAME(1),
	              TWO_SEASON_NAME(1),
                  TWO_SEASON_YEAR(1),
	              FOUR_SEASON_NAME(1),
                  FOUR_SEASON_YEAR(1),
				  lfMW,lfMVAR,lfMW_MIN,lfMVAR_MIN,lfMW_MAX,lfMVAR_MAX;
		 end if;

		 liCount:=liCount+1;

		 --if liCount=5 then
		   -- exit;
		 --end if;
      END LOOP;
      CLOSE cv;


	  /* update META_VERSION with status, last updated date */
	  update META_VERSION
	     set UPDATED_DT=sysdate
		     ,STATUS=READY
		 where version_id=liVersionIDDest;

	  commit;

      select sysdate into ldSys from dual;
      dbms_output.put_line('Ending: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));
	  return SUCCESS;

   EXCEPTION
      WHEN OTHERS THEN
         lsErrorMsg:=to_char(sqlcode)||': '||sqlerrm;
         dbms_output.put_line(lsErrorMsg);
         rollback;
		 psError:=lsErrorMsg;
		 return FAILURE;
   end;

   function RemovePeaksMP(psNameSrc in string,psNameSrc1 in string,
                      psNameDest in string,
                      psDescDest in string,
					  psSourceTable in varchar2,psDestTable in varchar2,
					  psMP_ID in varchar2,
					  psWhatToProcess in varchar2,
					  piSuggestedIndSrc in number,
					  piSuggestedIndDest in number,
					  piTolerance in number,
					  psError out varchar2) return number is
/**************************************************************
   THIS WILL try to create the brand new version identified by
   Destination version name/desc. It will make usual checks if version name
   is already used and if the limit of versions had been exceeded
   If everything fine it will take the data from the source table for given
   MP_ID and propagate it to the destination table.
   The parameter liSuggestedInd will tell if
   the real MW/MVAR has to be modified or SUGGESTED values

    The propagation to the destination table will be governed by the  psNameSrc1
	version which contains the tresholded derivatives. If this derivative is 0 then
	the source data will be propagated as is. For continious sets of 1 for derivatives
	it will be linearly interpolated.
 **************************************************************/
	  ldSys date;
	  lsErrorMsg varchar2(256);
	  liVersionIDSrc number;
	  liVersionIDSrc1 number;
	  liVersionIDDest number;
	  liReturn number;
	  lsSQLSource varchar2(4096);
	  lsSQLDest varchar2(4096);

	  liCount number;

	  liCountSeq number;
	  liCountSubSeq number;
	  liSequenceHalf number;

	  liSubSeqLength1 number;
	  liSubSeqEnd1 number;
	  liSubSeqStart1 number;
	  liSubSeqLength2 number;
	  liSubSeqEnd2 number;
	  liSubSeqStart2 number;

      liProcessingSubSequence number;

	  li number;
	  li1 number;
	  lf number;
	  lfDelta number;

      ldCalDayDate date;
	  lsCalHourEnding varchar2(3);


      ldCalDayDateInterpStart date;
	  lsCalHourEndingInterpStart varchar2(3);

	  liSign number;

      TYPE cv_typ IS REF CURSOR;
      cv cv_typ;

	  TYPE measure_tabtype IS TABLE OF number INDEX BY BINARY_INTEGER;
      mw_table measure_tabtype;

	  -- this is commented out because this definition is in the package
	  -- for passing this table as parameter
	  --TYPE datepoint_date_tabtype IS TABLE OF date INDEX BY BINARY_INTEGER;
      CalDayDate_table datepoint_date_tabtype;

	  -- this is commented out because this definition is in the package
	  -- for passing this table as parameter
	  --TYPE datepoint_he_tabtype IS TABLE OF varchar2(3) INDEX BY BINARY_INTEGER;
	  CalHourEnding_table datepoint_he_tabtype;

	  TYPE slope_counter_tabtype IS TABLE OF number INDEX BY BINARY_INTEGER;
      slopecounter_table slope_counter_tabtype;

	  TYPE subseq_counter_tabtype IS TABLE OF number INDEX BY BINARY_INTEGER;
      subseq_table subseq_counter_tabtype;

	  TYPE sign_tabtype IS TABLE OF number INDEX BY BINARY_INTEGER;
      sign_table sign_tabtype;

   begin
	  lsErrorMsg:='';

      select sysdate into ldSys from dual;
      dbms_output.put_line('Starting: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));

	  if piSuggestedIndSrc=1 then
	     if psSourceTable='MODIFIED' then
            null;
	     else
	        psError:='Wrong combination of input parameters. Suggested Indicator can be set only with Source Table set to MODIFIED.';
            dbms_output.put_line(psError);
		    return FAILURE;
         end if;
	  end if;

	  if piSuggestedIndDest=1 then
	     if psDestTable='MODIFIED' then
	        null;
         else
	        psError:='Wrong combination of input parameters. Suggested Indicator can be set only with Destination Table set to MODIFIED.';
            dbms_output.put_line(psError);
		    return FAILURE;
         end if;
	  end if;

	  -- further validate input param
	  if psWhatToProcess='MW'
	     or psWhatToProcess='MVAR'
		 or psWhatToProcess='MW_MIN'
		 or psWhatToProcess='MW_MAX'
		 or psWhatToProcess='MVAR_MIN'
		 or psWhatToProcess='MVAR_MAX' then

		 null;
	  else
         psError:='Wrong parameter What to process. Must be either MW,MVAR,MW_MIN,MW_MAX,MVAR_MIN,MVAR_MAX.';
         dbms_output.put_line(psError);
		 return FAILURE;
	  end if;

	  liVersionIDSrc:=GetVersionID(psNameSrc,psError);
      dbms_output.put_line('     VersionIDSrc: '||to_char(liVersionIDSrc));

	  if liVersionIDSrc <0 then
         dbms_output.put_line(psError);
		 return FAILURE;
	  end if;

	  liVersionIDSrc1:=GetVersionID(psNameSrc1,psError);
      dbms_output.put_line('     VersionIDSrc1: '||to_char(liVersionIDSrc1));

	  if liVersionIDSrc1 <0 then
         dbms_output.put_line(psError);
		 return FAILURE;
	  end if;

	  -- obtain available version_id
	  liVersionIDDest:=GetNextVersionID(psNameDest,psError);
	  if FAILURE=liVersionIDDest then
         dbms_output.put_line(psError);
		 return FAILURE;
	  end if;
      dbms_output.put_line('     VersionIDDest: '||to_char(liVersionIDDest));

	  /* first set the META_VERSION record */
	  insert into META_VERSION(VERSION_ID,VERSION_NAME,VERSION_DESC,CREATED_DT,UPDATED_DT,STATUS)
	  values (liVersionIDDest,psNameDest,psDescDest,sysdate,sysdate,INPROGRESS);

	  commit;

	  -- the process of propagation consists of several steps
	  -- first everythiong is copied for this MP_ID from source to Destination
	  -- then cursor is opened against derivative where derivative is set and then
	  -- for those points in the destination the specified variable is interpolated

	  -- first prepare sql to propagate the whole set
	  lsSQLDest:='insert into '||psDestTable||'(';
	  if -1<liVersionIDDest then
	     lsSQLDest:=lsSQLDest||'version_id,';
	  end if;
	  lsSQLDest:=lsSQLDest||'MP_ID,CAL_DAY_DATE,CAL_HOUR_ENDING ';

	  lsSQLDest:=lsSQLDest||',MEASURE_POINT_ID';
	  lsSQLDest:=lsSQLDest||',AREA_CODE';
	  lsSQLDest:=lsSQLDest||',MEASUREMENT_POINT_TYPE_CODE';
	  lsSQLDest:=lsSQLDest||',CONNECTION_TYPE';
	  lsSQLDest:=lsSQLDest||',INCL_IN_POD_LSB';
	  lsSQLDest:=lsSQLDest||',CATEGORY';
	  lsSQLDest:=lsSQLDest||',CAL_YEAR';
	  lsSQLDest:=lsSQLDest||',CAL_MONTH_NUMBER';
	  lsSQLDest:=lsSQLDest||',CAL_MONTH_SHORT_NAME';
	  lsSQLDest:=lsSQLDest||',TWO_SEASON_NAME';
	  lsSQLDest:=lsSQLDest||',TWO_SEASON_YEAR';
	  lsSQLDest:=lsSQLDest||',FOUR_SEASON_NAME';
	  lsSQLDest:=lsSQLDest||',FOUR_SEASON_YEAR';

	  if piSuggestedIndDest=0 then
         lsSQLDest:=lsSQLDest||',MW,MVAR';
		 lsSQLDest:=lsSQLDest||',MW_MIN,MVAR_MIN';
		 lsSQLDest:=lsSQLDest||',MW_MAX,MVAR_MAX ';
	  else
         lsSQLDest:=lsSQLDest||',MW_SUGGESTED,MVAR_SUGGESTED';
		 lsSQLDest:=lsSQLDest||',MW_MIN_SUGGESTED,MVAR_MIN_SUGGESTED';
         lsSQLDest:=lsSQLDest||',MW_MAX_SUGGESTED,MVAR_MAX_SUGGESTED ';
	  end if;

	  lsSQLDest:=lsSQLDest||') ';

	  lsSQLDest:=lsSQLDest||'select ';
	  if -1<liVersionIDDest then
	     lsSQLDest:=lsSQLDest||':liVersionIDDest,';
	  end if;

	  lsSQLDest:=lsSQLDest||'s1.MP_ID ';

	  lsSQLDest:=lsSQLDest||',s1.CAL_DAY_DATE,s1.CAL_HOUR_ENDING ';

	  lsSQLDest:=lsSQLDest||',s1.MEASURE_POINT_ID';
	  lsSQLDest:=lsSQLDest||',s1.AREA_CODE';
	  lsSQLDest:=lsSQLDest||',s1.MEASUREMENT_POINT_TYPE_CODE';
	  lsSQLDest:=lsSQLDest||',s1.CONNECTION_TYPE';
	  lsSQLDest:=lsSQLDest||',s1.INCL_IN_POD_LSB';
	  lsSQLDest:=lsSQLDest||',s1.CATEGORY';
	  lsSQLDest:=lsSQLDest||',s1.CAL_YEAR';
	  lsSQLDest:=lsSQLDest||',s1.CAL_MONTH_NUMBER';
	  lsSQLDest:=lsSQLDest||',s1.CAL_MONTH_SHORT_NAME';
	  lsSQLDest:=lsSQLDest||',s1.TWO_SEASON_NAME';
	  lsSQLDest:=lsSQLDest||',s1.TWO_SEASON_YEAR';
	  lsSQLDest:=lsSQLDest||',s1.FOUR_SEASON_NAME';
	  lsSQLDest:=lsSQLDest||',s1.FOUR_SEASON_YEAR';

	  if piSuggestedIndSrc=0 then
         lsSQLDest:=lsSQLDest||',nvl(s1.MW,0),nvl(s1.MVAR,0)';
		 lsSQLDest:=lsSQLDest||',nvl(s1.MW_MIN,0),nvl(s1.MVAR_MIN,0)';
		 lsSQLDest:=lsSQLDest||',nvl(s1.MW_MAX,0),nvl(s1.MVAR_MAX,0) ';
	  else
         lsSQLDest:=lsSQLDest||',nvl(s1.MW_SUGGESTED,0),nvl(s1.MVAR_SUGGESTED,0)';
		 lsSQLDest:=lsSQLDest||',nvl(s1.MW_MIN_SUGGESTED,0),nvl(s1.MVAR_MIN_SUGGESTED,0)';
         lsSQLDest:=lsSQLDest||',nvl(s1.MW_MAX_SUGGESTED,0),nvl(s1.MVAR_MAX_SUGGESTED,0) ';
	  end if;

	  lsSQLDest:=lsSQLDest||'from '||psSourceTable||' s1 ';
	  lsSQLDest:=lsSQLDest||'where s1.MP_ID=:lsMP_ID ';

	  if -1<liVersionIDSrc then
	     lsSQLDest:=lsSQLDest||'and s1.version_id='||to_char(liVersionIDSrc) ||' ';
	  end if;

	  execute immediate lsSQLDest
	     using liVersionIDDest,psMP_ID;

	  -- so everything now is in destination
	  -- now we have to update in destination what is necessary

	  -- now start fetching derivative data
	  lsSQLSource:='select CAL_DAY_DATE,CAL_HOUR_ENDING ';

	  if piSuggestedIndSrc=0 then
   	     if psWhatToProcess='MW' then
            lsSQLSource:=lsSQLSource||',mw ';
	     elsif psWhatToProcess='MVAR' then
            lsSQLSource:=lsSQLSource||',mvar ';
	     elsif psWhatToProcess='MW_MIN' then
            lsSQLSource:=lsSQLSource||',mw_min ';
	     elsif psWhatToProcess='MW_MAX' then
            lsSQLSource:=lsSQLSource||',mw_max ';
	     elsif psWhatToProcess='MVAR_MIN' then
            lsSQLSource:=lsSQLSource||',mvar_min ';
	     elsif psWhatToProcess='MVAR_MAX' then
            lsSQLSource:=lsSQLSource||',mvar_max ';
	     end if;
	  else
   	     if psWhatToProcess='MW' then
            lsSQLSource:=lsSQLSource||',mw_suggested ';
	     elsif psWhatToProcess='MVAR' then
            lsSQLSource:=lsSQLSource||',mvar_suggested ';
	     elsif psWhatToProcess='MW_MIN' then
            lsSQLSource:=lsSQLSource||',mw_min_suggested ';
	     elsif psWhatToProcess='MW_MAX' then
            lsSQLSource:=lsSQLSource||',mw_max_suggested ';
	     elsif psWhatToProcess='MVAR_MIN' then
            lsSQLSource:=lsSQLSource||',mvar_min_suggested ';
	     elsif psWhatToProcess='MVAR_MAX' then
            lsSQLSource:=lsSQLSource||',mvar_max_suggested ';
	     end if;
	  end if;


	  lsSQLSource:=lsSQLSource||'from '||psSourceTable||' ';
	  lsSQLSource:=lsSQLSource||'where MP_ID=:lsMP_ID ';

	  if -1<liVersionIDSrc then
	     lsSQLSource:=lsSQLSource||'and version_id='||to_char(liVersionIDSrc1) ||' ';
	  end if;
	  lsSQLSource:=lsSQLSource||'order by 1,2 ';

	  -- now start fetching derivative values that are above the threshold,
	  -- i.e. 1 or -1. While fetching we keep track only of those where
	  -- it continiously grows and then decreases (or decreases and the grows)
	  -- i.e. we look for sequences 1,1,1... and then -1,-1,-1
	  -- or -1,-1,-1... and then 1, 1, 1...
	  -- As soon such sequence ends we further make sure that number of 1 is equal to number of -1
	  -- within piTolerance
	  -- If such sequence is discovered it is processed via linear interpolation

	  -- if sequence doesn't satisfy those conditions - nothing is done
	  liCount:=0;
	  liCountSeq:=0;        -- total count within sequence
	  liCountSubSeq:=0;        -- total count within sequence
	  liSequenceHalf:=0;    -- counts the slope. we start with zero. then it becomes 1
	                        -- then the first slope ends, it becomes 2. then if becomes 3
							-- it is abnormality - means froom 1,1,1,.. and -1,-1,-1... we again
							-- have 1,1,1, i.e. we need to process
      liSign:=0;   -- if set shows that within seq we are on positive/negative slope

      --dbms_output.put_line(lsSQLSource);

      OPEN cv FOR lsSQLSource
	     USING  psMP_ID;
      LOOP
         --dbms_output.put_line('##: '||to_char(liCount));

         FETCH cv INTO ldCalDayDate,lsCalHourEnding,lf;

         EXIT WHEN cv%NOTFOUND;

		 if lf=1 or lf=-1 then

            --dbms_output.put_line('lf: '||to_char(lf));

		    -- the area of interpolation
		    if liCountSeq=0 then
			   -- the first possible record in the seq. Set the flag
			   if 0<lf then
    		      liSign:=1;
			   else
    		      liSign:=-1;
			   end if;
			   liSequenceHalf:=0;
			   liCountSubSeq:=0;
			else
			   -- not the first record of the sequence
			   if 0<lf then
			      if liSign=1 then
				     -- same as before, nothing to change just increase s SubCounter
       			     liCountSubSeq:=liCountSubSeq+1;
				  else
				     -- flipping from negative slope to positive
       		         liSign:=1;
        			 liCountSubSeq:=0;
			         liSequenceHalf:=liSequenceHalf+1;
				  end if;
			   else
			      if liSign=-1 then
				     -- same as before, nothing to change just increase s SubCounter
       			     liCountSubSeq:=liCountSubSeq+1;
				  else
				     -- flipping from positive slope to negative
       		         liSign:=-1;
        			 liCountSubSeq:=0;
			         liSequenceHalf:=liSequenceHalf+1;
				  end if;
			   end if;
			end if;

            -- populate next record of array
            --dbms_output.put_line('populating subseq_table(liCountSeq): '||to_char(liCountSubSeq));

			mw_table(liCountSeq):=lf;
            CalDayDate_table(liCountSeq):=ldCalDayDate;
            CalHourEnding_table(liCountSeq):=lsCalHourEnding;
            slopecounter_table(liCountSeq):=liSequenceHalf;
            subseq_table(liCountSeq):=liCountSubSeq;
            sign_table(liCountSeq):=liSign;

			liCountSeq:=liCountSeq+1;
		 else
		    -- here we may need to process the prev. sequence
			-- if such prev. sequence is there

            --dbms_output.put_line('processing...');

		    if 0<mw_table.Count then

               --dbms_output.put_line('Really processing...');

			   -- yes this record with lf=0 ends the sequence
			   -- sequence has to be processed
			   -- process every record from the array doing interpolation

			   -- start looping through PL/SQL tables
   		       li:=0;
			   liSubSeqStart1:=0;
			   liSubSeqEnd1:=0;
			   liSubSeqStart2:=0;
			   liSubSeqEnd2:=0;
			   liProcessingSubSequence:=0;
			   while li<mw_table.Count loop
			      if 0<li then

	                 --dbms_output.put_line('li: '|| to_char(li));
	                 --dbms_output.put_line('subseq_table(li): '|| to_char(subseq_table(li)));

				     if subseq_table(li)=0 then
					    -- start of the new subsequence
						if liProcessingSubSequence=0 then
						   -- the previous subsequence is the first one
						   -- set its end
                 		   liSubSeqEnd1:=li-1;
						   liSubSeqStart2:=li;
                           liProcessingSubSequence:=1;
						else
						   -- the previous subsequence is the second one
						   -- set its end
                 		   liSubSeqEnd2:=li-1;

                           liReturn:=RemPeakProcessSeq(psDestTable,
                                                       liVersionIDDest,
                                           			   psMP_ID,
					                                   piSuggestedIndDest,
					                                   psWhatToProcess,
					                                   piTolerance,
                                                       CalDayDate_table(liSubSeqStart1),
 			                                           CalHourEnding_table(liSubSeqStart1),
                                                       CalDayDate_table(liSubSeqEnd2),
 			                                           CalHourEnding_table(liSubSeqEnd2),
                	                                   liSubSeqStart1,
                  	                                   liSubSeqEnd1,
                	                                   liSubSeqStart2,
                  	                                   liSubSeqEnd2,
                                                       CalDayDate_table,
                                                       CalHourEnding_table,
					                                   psError);

                           if liReturn=FAILURE then
                              dbms_output.put_line(psError);
                              rollback;
	      		              return FAILURE;
		                   end if;

						   -- reset
						   liSubSeqStart1:=li;
                           liProcessingSubSequence:=0;
   					    end if;
					 end if;
				  end if;

   			      li:=li+1;
			   end loop;

			   -- while loop is over we have to do a last check to process the last one (last second half)
               liSubSeqEnd2:=li-1;

               liReturn:=RemPeakProcessSeq(psDestTable,
                      liVersionIDDest,
					  psMP_ID,
					  piSuggestedIndDest,
					  psWhatToProcess,
					  piTolerance,
                      CalDayDate_table(liSubSeqStart1),
 			          CalHourEnding_table(liSubSeqStart1),
                      CalDayDate_table(liSubSeqEnd2),
 			          CalHourEnding_table(liSubSeqEnd2),
                	  liSubSeqStart1,
                  	  liSubSeqEnd1,
                	  liSubSeqStart2,
                  	  liSubSeqEnd2,
                      CalDayDate_table,
                      CalHourEnding_table,
					  psError);

               if liReturn=FAILURE then
                  dbms_output.put_line(psError);
                  rollback;
	      		  return FAILURE;
		       end if;

			   --reset the array
          	   liCountSeq:=0;
          	   liCountSubSeq:=0;
	           liSequenceHalf:=0;
               liSign:=0;

			   mw_table.Delete;
               CalDayDate_table.Delete;
               CalHourEnding_table.Delete;
               slopecounter_table.Delete;
               subseq_table.Delete;
               sign_table.Delete;

            end if;
		 end if;

		 liCount:=liCount+1;

		 --if liCount=5 then
		   -- exit;
		 --end if;
      END LOOP;
      CLOSE cv;


	  /* update META_VERSION with status, last updated date */
	  update META_VERSION
	     set UPDATED_DT=sysdate
		     ,STATUS=READY
		 where version_id=liVersionIDDest;

	  commit;

      select sysdate into ldSys from dual;
      dbms_output.put_line('Ending: '||to_char(ldSys,'DD-MON-RRRR HH24:MI:SS'));
	  return SUCCESS;

   EXCEPTION
      WHEN OTHERS THEN
         lsErrorMsg:=to_char(sqlcode)||': '||sqlerrm;
         dbms_output.put_line(lsErrorMsg);
         rollback;
		 psError:=lsErrorMsg;
		 return FAILURE;
   end;

end load_staging;
/


spool off
