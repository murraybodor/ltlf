drop table CODES_T;

create table CODES_T
(
  OID                    NUMBER NOT NULL PRIMARY KEY ,
  CODE_TYPE             VARCHAR2(30) NOT NULL,
  CODE_VALUE_STR		VARCHAR2(20),
  CODE_VALUE_NUM		NUMBER,
  CODE_VALUE_DATE		DATE,
  CODE_DESC	        	VARCHAR2(50),
  ORDER_INFO		    NUMBER, 
  SUPP_INFO             VARCHAR2(30)
)
tablespace LTLF_DATA;

comment on table CODES_T 					is 'Holds code values used by the LTLF application'; 

