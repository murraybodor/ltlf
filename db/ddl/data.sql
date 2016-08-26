
insert into CODES_T
( select LTLF_SEQ.NEXTVAL, 'INTERVAL', null, 30, null, 'UI update interval in seconds', 1, null FROM DUAL);

insert into CODES_T
( select LTLF_SEQ.NEXTVAL, 'LOG_LEVEL', null, 3, null, 'Log level used in LTLF package', 1, null FROM DUAL);

--insert into CODES_T
--( select LTLF_SEQ.NEXTVAL, 'ENVIRONMENT', 'DEV', null, null, 'Environment', 1, null FROM DUAL);

insert into CODES_T
( select LTLF_SEQ.NEXTVAL, 'BASE_YEAR', null, 2007, null, 'Base Year', 1, null FROM DUAL);

insert into CODES_T
( select LTLF_SEQ.NEXTVAL, 'RELEASE_ID', '1.0.4.4', null, null, 'Current software release identifier', 1, null FROM DUAL);

insert into CODES_T
( select LTLF_SEQ.NEXTVAL, 'ALLOC_SECTOR', 'RES', null, null, 'Residential', 1, null FROM DUAL);

insert into CODES_T
( select LTLF_SEQ.NEXTVAL, 'ALLOC_SECTOR', 'COM', null, null, 'Commercial', 2, null FROM DUAL);

insert into CODES_T
( select LTLF_SEQ.NEXTVAL, 'ALLOC_SECTOR', 'IND', null, null, 'Industrial', 3, null FROM DUAL);

insert into CODES_T
( select LTLF_SEQ.NEXTVAL, 'ALLOC_SECTOR', 'FRM', null, null, 'Farm', 4, null FROM DUAL);

insert into CODES_T
( select LTLF_SEQ.NEXTVAL, 'ALLOC_SECTOR', 'OIL', null, null, 'Oil Sands', 5, null FROM DUAL);

-- schedule "copy MP" job at 00:00:00(12:00:00am) every day
VARIABLE jobno number;
BEGIN
	DBMS_JOB.SUBMIT(:jobno,
	'pkg_LTLF.CopyMPFromTASMo;',
	SYSDATE,
	'trunc(SYSDATE) + 1');
END;
/

commit;
