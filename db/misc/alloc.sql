truncate table allocation;
truncate table allocation_fcast_yr;
truncate table allocation_sector;
truncate table allocation_area;
truncate table allocation_mp;

insert into allocation (BASE_YEAR, VERSION_NUM, START_DATE, END_YEAR, DESCRIPTION, STATUS)
values (2007, 1, sysdate, 2030, 'test', 'N');

insert into allocation_fcast_yr (ALLOCATION_OID, FORECAST_YEAR, STATUS)
values (101481, 2008, 'N');
insert into allocation_fcast_yr (ALLOCATION_OID, FORECAST_YEAR, STATUS)
values (101481, 2009, 'N');
insert into allocation_fcast_yr (ALLOCATION_OID, FORECAST_YEAR, STATUS)
values (101481, 2010, 'N');
insert into allocation_fcast_yr (ALLOCATION_OID, FORECAST_YEAR, STATUS)
values (101481, 2011, 'N');
insert into allocation_fcast_yr (ALLOCATION_OID, FORECAST_YEAR, STATUS)
values (101481, 2012, 'N');
insert into allocation_fcast_yr (ALLOCATION_OID, FORECAST_YEAR, STATUS)
values (101481, 2013, 'N');
insert into allocation_fcast_yr (ALLOCATION_OID, FORECAST_YEAR, STATUS)
values (101481, 2014, 'N');
insert into allocation_fcast_yr (ALLOCATION_OID, FORECAST_YEAR, STATUS)
values (101481, 2015, 'N');
insert into allocation_fcast_yr (ALLOCATION_OID, FORECAST_YEAR, STATUS)
values (101481, 2016, 'N');
insert into allocation_fcast_yr (ALLOCATION_OID, FORECAST_YEAR, STATUS)
values (101481, 2017, 'N');
insert into allocation_fcast_yr (ALLOCATION_OID, FORECAST_YEAR, STATUS)
values (101481, 2018, 'N');
insert into allocation_fcast_yr (ALLOCATION_OID, FORECAST_YEAR, STATUS)
values (101481, 2019, 'N');
insert into allocation_fcast_yr (ALLOCATION_OID, FORECAST_YEAR, STATUS)
values (101481, 2020, 'N');
insert into allocation_fcast_yr (ALLOCATION_OID, FORECAST_YEAR, STATUS)
values (101481, 2021, 'N');
insert into allocation_fcast_yr (ALLOCATION_OID, FORECAST_YEAR, STATUS)
values (101481, 2022, 'N');
insert into allocation_fcast_yr (ALLOCATION_OID, FORECAST_YEAR, STATUS)
values (101481, 2023, 'N');
insert into allocation_fcast_yr (ALLOCATION_OID, FORECAST_YEAR, STATUS)
values (101481, 2024, 'N');
insert into allocation_fcast_yr (ALLOCATION_OID, FORECAST_YEAR, STATUS)
values (101481, 2025, 'N');
insert into allocation_fcast_yr (ALLOCATION_OID, FORECAST_YEAR, STATUS)
values (101481, 2026, 'N');
insert into allocation_fcast_yr (ALLOCATION_OID, FORECAST_YEAR, STATUS)
values (101481, 2027, 'N');
insert into allocation_fcast_yr (ALLOCATION_OID, FORECAST_YEAR, STATUS)
values (101481, 2028, 'N');
insert into allocation_fcast_yr (ALLOCATION_OID, FORECAST_YEAR, STATUS)
values (101481, 2029, 'N');
insert into allocation_fcast_yr (ALLOCATION_OID, FORECAST_YEAR, STATUS)
values (101481, 2030, 'N');

insert into allocation_sector (ALLOC_FY_OID, SECTOR_TYPE, ALLOC_ENERGY)
values (101482, 'RES', 35000);
insert into allocation_sector (ALLOC_FY_OID, SECTOR_TYPE, ALLOC_ENERGY)
values (101482, 'IND', 22000);
insert into allocation_sector (ALLOC_FY_OID, SECTOR_TYPE, ALLOC_ENERGY)
values (101482, 'COMM', 17000);

insert into allocation_sector (ALLOC_FY_OID, SECTOR_TYPE, ALLOC_ENERGY)
values (101483, 'RES', 36000);
insert into allocation_sector (ALLOC_FY_OID, SECTOR_TYPE, ALLOC_ENERGY)
values (101483, 'IND', 23000);
insert into allocation_sector (ALLOC_FY_OID, SECTOR_TYPE, ALLOC_ENERGY)
values (101483, 'COMM', 18000);

insert into allocation_sector (ALLOC_FY_OID, SECTOR_TYPE, ALLOC_ENERGY)
values (101484, 'RES', 37000);
insert into allocation_sector (ALLOC_FY_OID, SECTOR_TYPE, ALLOC_ENERGY)
values (101484, 'IND', 24000);
insert into allocation_sector (ALLOC_FY_OID, SECTOR_TYPE, ALLOC_ENERGY)
values (101484, 'COMM', 19000);

insert into allocation_area (ALLOC_FY_OID, AREA_CODE, STATUS)
values (101482, 6, 'N');
insert into allocation_area (ALLOC_FY_OID, AREA_CODE, STATUS)
values (101482, 8, 'N');
insert into allocation_area (ALLOC_FY_OID, AREA_CODE, STATUS)
values (101482, 17, 'N');

insert into allocation_area (ALLOC_FY_OID, AREA_CODE, STATUS)
values (101483, 6, 'N');
insert into allocation_area (ALLOC_FY_OID, AREA_CODE, STATUS)
values (101483, 8, 'N');
insert into allocation_area (ALLOC_FY_OID, AREA_CODE, STATUS)
values (101483, 17, 'N');

insert into allocation_area (ALLOC_FY_OID, AREA_CODE, STATUS)
values (101484, 6, 'N');
insert into allocation_area (ALLOC_FY_OID, AREA_CODE, STATUS)
values (101484, 8, 'N');
insert into allocation_area (ALLOC_FY_OID, AREA_CODE, STATUS)
values (101484, 17, 'N');


insert into allocation_sector (ALLOC_AREA_OID, SECTOR_TYPE, ALLOC_ENERGY)
values (101514, 'RES', 7000);
insert into allocation_sector (ALLOC_AREA_OID, SECTOR_TYPE, ALLOC_ENERGY)
values (101514, 'IND', 4000);
insert into allocation_sector (ALLOC_AREA_OID, SECTOR_TYPE, ALLOC_ENERGY)
values (101514, 'COM', 9000);

insert into allocation_sector (ALLOC_AREA_OID, SECTOR_TYPE, ALLOC_ENERGY)
values (101515, 'RES', 7100);
insert into allocation_sector (ALLOC_AREA_OID, SECTOR_TYPE, ALLOC_ENERGY)
values (101515, 'IND', 4100);
insert into allocation_sector (ALLOC_AREA_OID, SECTOR_TYPE, ALLOC_ENERGY)
values (101515, 'COM', 9100);

insert into allocation_sector (ALLOC_AREA_OID, SECTOR_TYPE, ALLOC_ENERGY)
values (101516, 'RES', 7200);
insert into allocation_sector (ALLOC_AREA_OID, SECTOR_TYPE, ALLOC_ENERGY)
values (101516, 'IND', 4200);
insert into allocation_sector (ALLOC_AREA_OID, SECTOR_TYPE, ALLOC_ENERGY)
values (101516, 'COM', 9200);

insert into allocation_sector (ALLOC_AREA_OID, SECTOR_TYPE, ALLOC_ENERGY)
values (101517, 'RES', 7300);
insert into allocation_sector (ALLOC_AREA_OID, SECTOR_TYPE, ALLOC_ENERGY)
values (101517, 'IND', 4300);
insert into allocation_sector (ALLOC_AREA_OID, SECTOR_TYPE, ALLOC_ENERGY)
values (101517, 'COM', 9300);

insert into allocation_sector (ALLOC_AREA_OID, SECTOR_TYPE, ALLOC_ENERGY)
values (101518, 'RES', 7400);
insert into allocation_sector (ALLOC_AREA_OID, SECTOR_TYPE, ALLOC_ENERGY)
values (101518, 'IND', 4400);
insert into allocation_sector (ALLOC_AREA_OID, SECTOR_TYPE, ALLOC_ENERGY)
values (101518, 'COM', 9400);

insert into allocation_sector (ALLOC_AREA_OID, SECTOR_TYPE, ALLOC_ENERGY)
values (101519, 'RES', 7500);
insert into allocation_sector (ALLOC_AREA_OID, SECTOR_TYPE, ALLOC_ENERGY)
values (101519, 'IND', 4500);
insert into allocation_sector (ALLOC_AREA_OID, SECTOR_TYPE, ALLOC_ENERGY)
values (101519, 'COM', 9500);

commit;

