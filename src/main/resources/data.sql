/* Database initialization
Add insert/update/store procedure/function to add master table entry into database,
after your app is run/initialize.  */


--CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

/* Example
--INSERT INTO app_config (key,value,created_at,is_active,version_no)
--       SELECT 'MaxPostLimit', '5', current_timestamp, true, 0
--       WHERE NOT EXISTS (SELECT 1 FROM app_config WHERE key='MaxPostLimit');*/
--
       /*Below tables are used for oauth 2 authentication*/
       CREATE TABLE IF NOT EXISTS OAUTH_CLIENT_DETAILS (
         CLIENT_ID VARCHAR(255) PRIMARY KEY,
         RESOURCE_IDS VARCHAR(255),
         CLIENT_SECRET VARCHAR(255),
         SCOPE VARCHAR(255),
         AUTHORIZED_GRANT_TYPES VARCHAR(255),
         WEB_SERVER_REDIRECT_URI VARCHAR(255),
         AUTHORITIES VARCHAR(255),
         ACCESS_TOKEN_VALIDITY INTEGER,
         REFRESH_TOKEN_VALIDITY INTEGER,
         ADDITIONAL_INFORMATION VARCHAR(4096),
         AUTOAPPROVE VARCHAR(255)
       );

       CREATE TABLE IF NOT EXISTS OAUTH_CLIENT_TOKEN (
         TOKEN_ID VARCHAR(255),
         TOKEN BYTEA,
         AUTHENTICATION_ID VARCHAR(255) PRIMARY KEY,
         USER_NAME VARCHAR(255),
         CLIENT_ID VARCHAR(255)
       );

       CREATE TABLE IF NOT EXISTS OAUTH_ACCESS_TOKEN (
         TOKEN_ID VARCHAR(255),
         TOKEN BYTEA,
         AUTHENTICATION_ID VARCHAR(255) PRIMARY KEY,
         USER_NAME VARCHAR(255),
         CLIENT_ID VARCHAR(255),
         AUTHENTICATION BYTEA,
         REFRESH_TOKEN VARCHAR(255)
       );

       CREATE TABLE IF NOT EXISTS OAUTH_REFRESH_TOKEN (
         TOKEN_ID VARCHAR(255),
         TOKEN BYTEA,
         AUTHENTICATION BYTEA
       );

       CREATE TABLE IF NOT EXISTS OAUTH_CODE (
         CODE VARCHAR(255),
         AUTHENTICATION BYTEA
       );

       CREATE TABLE IF NOT EXISTS OAUTH_APPROVALS (
         USERID VARCHAR(255),
         CLIENTID VARCHAR(255),
         SCOPE VARCHAR(255),
         STATUS VARCHAR(10),
         EXPIRESAT TIMESTAMP,
         LASTMODIFIEDAT TIMESTAMP
       );
--
--       /*password is-------> tp*k3m1H&s*/
--       INSERT INTO OAUTH_CLIENT_DETAILS(CLIENT_ID, RESOURCE_IDS, CLIENT_SECRET, SCOPE, AUTHORIZED_GRANT_TYPES, AUTHORITIES, ACCESS_TOKEN_VALIDITY, REFRESH_TOKEN_VALIDITY)
--       	SELECT 'invoice-management-client', 'resource-server-rest-api',
--       	'$2a$10$TIf24bglcMUQaDlrG3.x3.kiakcse2IsvzsJVOv5Kw7Z.JfuiHMHS',
--       	'read,write', 'password,authorization_code,refresh_token,implicit', 'USER', 600, 86400
--       	WHERE NOT EXISTS (SELECT 1 FROM OAUTH_CLIENT_DETAILS WHERE CLIENT_ID='invoice-management-client');

       	 /*password is-------> tp*k3m1H&s*/
               INSERT INTO OAUTH_CLIENT_DETAILS(CLIENT_ID, RESOURCE_IDS, CLIENT_SECRET, SCOPE, AUTHORIZED_GRANT_TYPES, AUTHORITIES, ACCESS_TOKEN_VALIDITY, REFRESH_TOKEN_VALIDITY)
               	SELECT 'invoice-client', 'resource-server-rest-api',
               	'$2a$10$nVC5JAsadUrjbv5MkmeqreM7byu4E7bJCs/GcY9aS0hrDunZbXu8u',
               	'read,write', 'password,authorization_code,refresh_token,implicit', 'USER', 604800, 2592000
               	WHERE NOT EXISTS (SELECT 1 FROM OAUTH_CLIENT_DETAILS WHERE CLIENT_ID='invoice-client');   /*access token in seconds validity is 7 day and refresh token 120 days*/


       	/* Data for Oauth Doctor & Client */
--
--        INSERT INTO OAUTH_CLIENT_DETAILS(CLIENT_ID, RESOURCE_IDS, CLIENT_SECRET, SCOPE, AUTHORIZED_GRANT_TYPES, AUTHORITIES, ACCESS_TOKEN_VALIDITY, REFRESH_TOKEN_VALIDITY)
--        SELECT 'goodcheckup-doctor', 'resource-server-rest-api', '$2y$15$O19rHJ8lgfRp5Zi1xZyMuOeaCAUyzBLjRr4GC6M6FO2dLpGKsMoL.',
--        'read,write', 'password,refresh_token', 'USER', 7200, 2592000 WHERE NOT EXISTS
--        (SELECT 1 FROM OAUTH_CLIENT_DETAILS WHERE CLIENT_ID='goodcheckup-doctor');

--        INSERT INTO public.role(id,created_at, is_active, version_no, role_type)
--           SELECT uuid_in(md5(random()::text || clock_timestamp()::text)::cstring),now(),true,1,'ADMIN'
--              WHERE NOT EXISTS (SELECT 1 FROM role WHERE role_type='ADMIN');
--
--        INSERT INTO public.role(id,created_at, is_active, version_no, role_type)
--           SELECT uuid_in(md5(random()::text || clock_timestamp()::text)::cstring),now(),true,1,'USER'
--              WHERE NOT EXISTS (SELECT 1 FROM role WHERE role_type='USER');
--

--/*Below tables are used for quartz*/
--CREATE TABLE IF NOT EXISTS qrtz_job_details
--  (
--    SCHED_NAME VARCHAR(120) NOT NULL,
--    JOB_NAME  VARCHAR(200) NOT NULL,
--    JOB_GROUP VARCHAR(200) NOT NULL,
--    DESCRIPTION VARCHAR(250) NULL,
--    JOB_CLASS_NAME   VARCHAR(250) NOT NULL,
--    IS_DURABLE BOOL NOT NULL,
--    IS_NONCONCURRENT BOOL NOT NULL,
--    IS_UPDATE_DATA BOOL NOT NULL,
--    REQUESTS_RECOVERY BOOL NOT NULL,
--    JOB_DATA BYTEA NULL,
--    PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
--);
--
--CREATE TABLE IF NOT EXISTS qrtz_triggers
--  (
--    SCHED_NAME VARCHAR(120) NOT NULL,
--    TRIGGER_NAME VARCHAR(200) NOT NULL,
--    TRIGGER_GROUP VARCHAR(200) NOT NULL,
--    JOB_NAME  VARCHAR(200) NOT NULL,
--    JOB_GROUP VARCHAR(200) NOT NULL,
--    DESCRIPTION VARCHAR(250) NULL,
--    NEXT_FIRE_TIME BIGINT NULL,
--    PREV_FIRE_TIME BIGINT NULL,
--    PRIORITY INTEGER NULL,
--    TRIGGER_STATE VARCHAR(16) NOT NULL,
--    TRIGGER_TYPE VARCHAR(8) NOT NULL,
--    START_TIME BIGINT NOT NULL,
--    END_TIME BIGINT NULL,
--    CALENDAR_NAME VARCHAR(200) NULL,
--    MISFIRE_INSTR SMALLINT NULL,
--    JOB_DATA BYTEA NULL,
--    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
--    FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
--	REFERENCES QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP)
--);
--
--CREATE TABLE IF NOT EXISTS qrtz_simple_triggers
--  (
--    SCHED_NAME VARCHAR(120) NOT NULL,
--    TRIGGER_NAME VARCHAR(200) NOT NULL,
--    TRIGGER_GROUP VARCHAR(200) NOT NULL,
--    REPEAT_COUNT BIGINT NOT NULL,
--    REPEAT_INTERVAL BIGINT NOT NULL,
--    TIMES_TRIGGERED BIGINT NOT NULL,
--    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
--    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
--	REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
--);
--
--CREATE TABLE IF NOT EXISTS qrtz_cron_triggers
--  (
--    SCHED_NAME VARCHAR(120) NOT NULL,
--    TRIGGER_NAME VARCHAR(200) NOT NULL,
--    TRIGGER_GROUP VARCHAR(200) NOT NULL,
--    CRON_EXPRESSION VARCHAR(120) NOT NULL,
--    TIME_ZONE_ID VARCHAR(80),
--    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
--    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
--	REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
--);
--
--CREATE TABLE IF NOT EXISTS qrtz_simprop_triggers
--  (
--    SCHED_NAME VARCHAR(120) NOT NULL,
--    TRIGGER_NAME VARCHAR(200) NOT NULL,
--    TRIGGER_GROUP VARCHAR(200) NOT NULL,
--    STR_PROP_1 VARCHAR(512) NULL,
--    STR_PROP_2 VARCHAR(512) NULL,
--    STR_PROP_3 VARCHAR(512) NULL,
--    INT_PROP_1 INT NULL,
--    INT_PROP_2 INT NULL,
--    LONG_PROP_1 BIGINT NULL,
--    LONG_PROP_2 BIGINT NULL,
--    DEC_PROP_1 NUMERIC(13,4) NULL,
--    DEC_PROP_2 NUMERIC(13,4) NULL,
--    BOOL_PROP_1 BOOL NULL,
--    BOOL_PROP_2 BOOL NULL,
--    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
--    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
--    REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
--);
--
--CREATE TABLE IF NOT EXISTS qrtz_blob_triggers
--  (
--    SCHED_NAME VARCHAR(120) NOT NULL,
--    TRIGGER_NAME VARCHAR(200) NOT NULL,
--    TRIGGER_GROUP VARCHAR(200) NOT NULL,
--    BLOB_DATA BYTEA NULL,
--    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
--    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
--        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
--);
--
--CREATE TABLE IF NOT EXISTS qrtz_calendars
--  (
--    SCHED_NAME VARCHAR(120) NOT NULL,
--    CALENDAR_NAME  VARCHAR(200) NOT NULL,
--    CALENDAR BYTEA NOT NULL,
--    PRIMARY KEY (SCHED_NAME,CALENDAR_NAME)
--);
--
--
--CREATE TABLE IF NOT EXISTS qrtz_paused_trigger_grps
--  (
--    SCHED_NAME VARCHAR(120) NOT NULL,
--    TRIGGER_GROUP  VARCHAR(200) NOT NULL,
--    PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP)
--);
--
--CREATE TABLE IF NOT EXISTS qrtz_fired_triggers
--  (
--    SCHED_NAME VARCHAR(120) NOT NULL,
--    ENTRY_ID VARCHAR(95) NOT NULL,
--    TRIGGER_NAME VARCHAR(200) NOT NULL,
--    TRIGGER_GROUP VARCHAR(200) NOT NULL,
--    INSTANCE_NAME VARCHAR(200) NOT NULL,
--    FIRED_TIME BIGINT NOT NULL,
--    SCHED_TIME BIGINT NOT NULL,
--    PRIORITY INTEGER NOT NULL,
--    STATE VARCHAR(16) NOT NULL,
--    JOB_NAME VARCHAR(200) NULL,
--    JOB_GROUP VARCHAR(200) NULL,
--    IS_NONCONCURRENT BOOL NULL,
--    REQUESTS_RECOVERY BOOL NULL,
--    PRIMARY KEY (SCHED_NAME,ENTRY_ID)
--);
--
--CREATE TABLE IF NOT EXISTS qrtz_scheduler_state
--  (
--    SCHED_NAME VARCHAR(120) NOT NULL,
--    INSTANCE_NAME VARCHAR(200) NOT NULL,
--    LAST_CHECKIN_TIME BIGINT NOT NULL,
--    CHECKIN_INTERVAL BIGINT NOT NULL,
--    PRIMARY KEY (SCHED_NAME,INSTANCE_NAME)
--);
--
--CREATE TABLE IF NOT EXISTS qrtz_locks
--  (
--    SCHED_NAME VARCHAR(120) NOT NULL,
--    LOCK_NAME  VARCHAR(40) NOT NULL,
--    PRIMARY KEY (SCHED_NAME,LOCK_NAME)
--);
--
--create index IF NOT EXISTS idx_qrtz_j_req_recovery on qrtz_job_details(SCHED_NAME,REQUESTS_RECOVERY);
--create index IF NOT EXISTS idx_qrtz_j_grp on qrtz_job_details(SCHED_NAME,JOB_GROUP);
--
--create index IF NOT EXISTS idx_qrtz_t_j on qrtz_triggers(SCHED_NAME,JOB_NAME,JOB_GROUP);
--create index IF NOT EXISTS idx_qrtz_t_jg on qrtz_triggers(SCHED_NAME,JOB_GROUP);
--create index IF NOT EXISTS idx_qrtz_t_c on qrtz_triggers(SCHED_NAME,CALENDAR_NAME);
--create index IF NOT EXISTS idx_qrtz_t_g on qrtz_triggers(SCHED_NAME,TRIGGER_GROUP);
--create index IF NOT EXISTS idx_qrtz_t_state on qrtz_triggers(SCHED_NAME,TRIGGER_STATE);
--create index IF NOT EXISTS idx_qrtz_t_n_state on qrtz_triggers(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_STATE);
--create index IF NOT EXISTS idx_qrtz_t_n_g_state on qrtz_triggers(SCHED_NAME,TRIGGER_GROUP,TRIGGER_STATE);
--create index IF NOT EXISTS idx_qrtz_t_next_fire_time on qrtz_triggers(SCHED_NAME,NEXT_FIRE_TIME);
--create index IF NOT EXISTS idx_qrtz_t_nft_st on qrtz_triggers(SCHED_NAME,TRIGGER_STATE,NEXT_FIRE_TIME);
--create index IF NOT EXISTS idx_qrtz_t_nft_misfire on qrtz_triggers(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME);
--create index IF NOT EXISTS idx_qrtz_t_nft_st_misfire on qrtz_triggers(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_STATE);
--create index IF NOT EXISTS idx_qrtz_t_nft_st_misfire_grp on qrtz_triggers(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_GROUP,TRIGGER_STATE);
--
--create index IF NOT EXISTS idx_qrtz_ft_trig_inst_name on qrtz_fired_triggers(SCHED_NAME,INSTANCE_NAME);
--create index IF NOT EXISTS idx_qrtz_ft_inst_job_req_rcvry on qrtz_fired_triggers(SCHED_NAME,INSTANCE_NAME,REQUESTS_RECOVERY);
--create index IF NOT EXISTS idx_qrtz_ft_j_g on qrtz_fired_triggers(SCHED_NAME,JOB_NAME,JOB_GROUP);
--create index IF NOT EXISTS idx_qrtz_ft_jg on qrtz_fired_triggers(SCHED_NAME,JOB_GROUP);
--create index IF NOT EXISTS idx_qrtz_ft_t_g on qrtz_fired_triggers(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP);
--create index IF NOT EXISTS idx_qrtz_ft_tg on qrtz_fired_triggers(SCHED_NAME,TRIGGER_GROUP);

--INSERT INTO app_config (id, created_at, updated_at, is_active, version_no, key, value) SELECT uuid_generate_v5(uuid_ns_x500(), CAST(uuid_generate_v4() as varchar)), '2020-02-12 04:15:19.589', '2020-02-12 04:15:19.589', true, 1, 'FORGOT_PWD_MAX_RETRIES', 3 WHERE NOT EXISTS (SELECT 1 FROM app_config WHERE key = 'FORGOT_PWD_MAX_RETRIES' );

---------------function to calculate weightage
/*
SELECT * FROM (select distinct ui2.id as userId, ui2.FirstName as firstName, ui2.LastName as lastName, ui2.email as email, ui2.dob_long as dobLong, r2.role_type as role, up2.default_picurl as defaultPicURL, AVG(floor(calculateWeightage(ui1.id, ui2.id))::integer) as matchPercentage
from user_identity ui1, user_profile up1, user_identity ui2, user_profile up2, "role" r2
where ui2.role_id = r2.id
and up1.user_identity_id = ui1.id
and ui1.id != '6e194c06-2067-4382-a79e-49f7384da3fc' ---logged in user
and ui1.is_blocked = false
and up1.gender_preference in ('MALE', 'FEMALE')
and up2.user_identity_id = ui2.id
and ui2.is_blocked = false
and up1.age_group_min <= EXTRACT(YEAR FROM age(cast(ui2.dob as date)))
and up1.age_group_max >= EXTRACT(YEAR FROM age(cast(ui2.dob as date)))
and NOT EXISTS (SELECT s FROM swipes s WHERE s.swipe_by_user_id = '6e194c06-2067-4382-a79e-49f7384da3fc' AND s.swipe_to_user_id = ui2.id) --swiped users by me
or NOT EXISTS (SELECT s FROM swipes s WHERE s.swipe_to_user_id = '6e194c06-2067-4382-a79e-49f7384da3fc' AND s.swipe_type='DISLIKE') --someone disliked me
group by ui2.id, up2.default_picurl, r2.role_type
) as resultedmatchweightage

where matchPercentage > 10 -->70 for matches and <70 for profiles

order by matchPercentage desc
offset 0 limit 20
*/


--INSERT INTO payment_mode (id,data_type, value, is_active) VALUES('1','PAYMENT_MODE', 'Bank', true);
--INSERT INTO payment_mode (id,data_type, value, is_active) VALUES('2','PAYMENT_MODE', 'Credit Card', true);
--INSERT INTO client_type (id,data_type, value, is_active) VALUES('1','D', 'Domestic Client', true);
--INSERT INTO client_type (id,data_type, value, is_active) VALUES('2','I', 'International Client', true);
--INSERT INTO client_type (id,data_type, value, is_active) VALUES('3','L', 'LLC Client', true);
--INSERT INTO payment_term (id,data_type, value, is_active) VALUES('1','PAYMENT_TERM', 'Net 3', true);
--INSERT INTO payment_term (id,data_type, value, is_active) VALUES('2','PAYMENT_TERM', 'Net 15', true);
--INSERT INTO payment_status (id,data_type, value, is_active) VALUES('1','PAYMENT_STATUS', 'BILLEDTOCLIENT', true);
--INSERT INTO payment_status (id,data_type, value, is_active) VALUES('2','PAYMENT_STATUS', 'PENDING', true);
--INSERT INTO payment_status (id,data_type, value, is_active) VALUES('3','PAYMENT_STATUS', 'FULLYPAID', true);
--INSERT INTO payment_status (id,data_type, value, is_active) VALUES('4','PAYMENT_STATUS', 'PARTIALLYPAID', true);
----INSERT INTO payment_st(id,data_type, value, is_active) VALUES('2','PAYMENT_TERM', 'Net 15', true);
--
--INSERT INTO master_data(id, key, value)VALUES ('1', 'Company Name', 'Biz4Solutions');
--INSERT INTO master_data(id, key, value) VALUES('2', 'CGST(%)', '9(%)');
--INSERT INTO master_data(id, key, value) VALUES('3', 'SGST(%)', '9(%)');
--
--INSERT INTO bank(id, ifsc_code, account_name, ach_routing_number, bank_account_number, bank_name, bank_type, branch, company_account_number, swift_code, wire_routing_number)
--	VALUES ('1', 'KKB7834583', 'Biz 4 Solutions Private Limited', '111111111', '11111111111', 'Kotak Mahindra Bank', 'I', 'Baner,Pune', '510101003486531', 'UBININBBKWP', '11111111111');
--
--INSERT INTO public.bank
--(id, ifsc_code, account_name, account_number_beneficiary, account_number_credit, ach_routing_number, bank_account_number, bank_name, bank_type, branch, credit_to_swift_code, pay_to_swift_code, wire_routing_number)
--VALUES(1, '124569', 'Biz4solutions Pvt Ltd', '510101003486531', '10929227', '111111', '111223123', 'Kotak Mahindra Bank', 'D', 'Baner, Pune, India', 'UBININBBKWP', 'CITIUS33', '111111');
--
--INSERT INTO public.bank
--(id, ifsc_code, account_name, account_number_beneficiary, account_number_credit, ach_routing_number, bank_account_number, bank_name, bank_type, branch, credit_to_swift_code, pay_to_swift_code, wire_routing_number)
--VALUES(2, '124569', 'Biz4solutions Pvt Ltd', '44454', '1111111', '111111', '111223123', 'Union Bank of India', 'I', 'Kasarwadi, Baner, Pune, India', '1111111', '11111', '111111');
--
--
--
--INSERT INTO public.bank
--(id, ifsc_code, account_name, account_number_beneficiary, account_number_credit, ach_routing_number, bank_account_number, bank_name, bank_type, branch, credit_to_swift_code, pay_to_swift_code, wire_routing_number)
--VALUES(3, '124569', 'Biz4solutions Pvt Ltd', '44454', '1111111', '111111', '111223123', 'Bank of America', 'L', 'Baner, Pune, India', '1111111', '11111', '111111');
--
--
--INSERT INTO company(
--	id, address1, address2, company_name, company_type, ein, gstin, logo_url, pan_no, phone_number, website)
--	VALUES ('1', '201, Sai Shilp Business Center,', 'Survey No 79, Balewadi Phata',
--	'Biz 4 Solutions Private Limited', 'D', 'Baner, Pune - 411045 MH, India', ' 27AAECB5261H1ZQ',
--	 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRyE1chhaukaUygDx76mybYltQw2C1UF3l_jnFCteOSHg&s',
--	 'AAECB5261H', ' +91 7276300411', 'www.biz4solutions.com');
--
--
--
--INSERT INTO company(
--	id, address1, address2, company_name, company_type, ein, gstin, logo_url, pan_no, phone_number, website)
--	VALUES ('2', '5555 Any Lane Blvd ', 'FRISCO, TX-75034',
--	'Biz4Solutions LLC', 'L', '12-885555', ' 27AAECB5261H1ZQ',
--	 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRyE1chhaukaUygDx76mybYltQw2C1UF3l_jnFCteOSHg&s',
--	 'AAECB5261H', ' +91 7276300411', 'www.biz4solutions.com');

--INSERT INTO master_data (id,data_type, value, is_active) VALUES('3','PROJECT_STATUS', 'Closed', true);
--INSERT INTO master_data (id,data_type, value, is_active) VALUES('4','PROJECT_STATUS', 'Active', true);
--INSERT INTO master_data (id,data_type, value, is_active) VALUES('5','PROJECT_STATUS', 'Inactive', true);
--INSERT INTO master_data (id,data_type, value, is_active) VALUES('8','CLIENT_STATUS', 'Inactive', true);
--INSERT INTO master_data (id,data_type, value, is_active) VALUES('9','CLIENT_STATUS', 'Active', true);
--INSERT INTO master_data (id,data_type, value, is_active) VALUES('10','DEPARTMENT', 'HR', true);
--INSERT INTO master_data (id,data_type, value, is_active) VALUES('11','DEPARTMENT', 'Management', true);

--drop function public.calculateWeightage;
--commit;
--
--DROP FUNCTION if exists public.calculateWeightage;
--CREATE or REPLACE FUNCTION public.calculateWeightage(IN userid uuid, IN partnerid uuid)
--RETURNS float
--LANGUAGE 'plpgsql'
--
--AS $BODY$
--DECLARE percentage float;
--DECLARE city_weightage float;
--DECLARE height_weightage float;
--DECLARE partner_age int;
--DECLARE age_weightage float;
--DECLARE maritalstatus_weightage float;
--DECLARE havekids_weightage float;
--DECLARE attire_weightage float;
--DECLARE drinkingbehaviour_weightage float;
--DECLARE education_weightage float;
--DECLARE ethnicity_weightage float;
--DECLARE exercise_weightage float;
--DECLARE vaccination_weightage float;
--DECLARE extro_intro_weightage float;
--DECLARE flower_weightage float;
--DECLARE food_weightage float;
--DECLARE drinks_weightage float;
--DECLARE gender_weightage float;
--DECLARE pets_weightage float;
--DECLARE politics_weightage float;
--DECLARE profession_weightage float;
--DECLARE religion_weightage float;
--DECLARE salary_weightage float;
--DECLARE smoking_weightage float;
--DECLARE voting_weightage float;
--DECLARE budget_weightage float;
--DECLARE place_weightage float;
--DECLARE time_weightage float;
--DECLARE ridesharing_weightage float;
--DECLARE activities_weightage float;
--DECLARE hobbies_weightage float;
--DECLARE interests_weightage float;
--
--
--begin
--  percentage=0;
--	--city weightage
--	SELECT COALESCE((SELECT weight from weightage where parameter = 'CITY'
--    and ((select count(*) from user_profile up1, user_profile up2 where up1.user_identity_id = userid and up2.user_identity_id = partnerid
--    and nullif(trim(up1.city),'') is not null and nullif(trim(up2.city),'') is not null and lower(up1.city) = lower(up2.city)) > 0)), 0) into city_weightage;
--
--	--height range weighatge
--	SELECT COALESCE((select weight from weightage where parameter = 'HEIGHT'
--    and ((select count(*) from user_profile up1, user_profile up2 where up1.user_identity_id = userid and up2.user_identity_id = partnerid
--    and nullif(trim(up1.height_range),'') is not null and nullif(trim(up2.height_range),'') is not null and lower(up1.height_range) = lower(up2.height_range)) > 0)), 0) INTO height_weightage;
--
--    --pref age group
--    select EXTRACT(YEAR FROM age(cast(dob as date))) into partner_age from user_identity ui where ui.id = partnerid;
--
--    SELECT COALESCE((select weight from weightage where parameter = 'PREFERRED_AGE_GROUP'
--	and ((select count(*) from user_profile up1, user_profile up2 where up1.user_identity_id = userid
--	and up2.user_identity_id = partnerid
--	and up1.age_group_min is not null and up1.age_group_max is not null
--	and up2.age_group_min is not null and up2.age_group_max is not null
--	and up1.age_group_min <= partner_age and up1.age_group_max >= partner_age) > 0)), 0) into age_weightage;
--
--    --marital status
--    SELECT COALESCE((select weight from weightage where parameter = 'MARITAL_STATUS'
--    and ((select count(*) from user_profile up1, user_profile up2 where up1.user_identity_id = userid and up2.user_identity_id = partnerid
--    and nullif(trim(up1.marital_status),'') is not null and nullif(trim(up2.marital_status),'') is not null
--    and lower(up1.marital_status) = lower(up2.marital_status)) > 0)), 0) INTO maritalstatus_weightage;
--
--    --have kids
--    SELECT COALESCE((select weight from weightage where parameter = 'KIDS'
--	and ((select count(*) from user_profile up1, user_profile up2
--	where up1.user_identity_id = userid
--	and up2.user_identity_id = partnerid
--	and up1.have_kids is not null and up2.have_kids is not null and up1.have_kids = up2.have_kids) > 0)), 0) INTO havekids_weightage;
--
--    --attire
--    SELECT COALESCE((select weight from weightage where parameter = 'ATTIRE'
--    and ((select count(*) from user_profile up1, user_profile up2 where up1.user_identity_id = userid and up2.user_identity_id = partnerid
--    and up1.attire_id is not null and up2.attire_id is not null and up1.attire_id = up2.attire_id) > 0)), 0) INTO attire_weightage;
--
--    --drinking behaviour
--    SELECT COALESCE((select weight from weightage where parameter = 'DRINKING'
--    and ((select count(*) from user_profile up1, user_profile up2 where up1.user_identity_id = userid and up2.user_identity_id = partnerid
--    and nullif(trim(up1.drinking_behaviour),'') is not null and nullif(trim(up2.drinking_behaviour),'') is not null
--    and lower(up1.drinking_behaviour) = lower(up2.drinking_behaviour)) > 0)), 0) INTO drinkingbehaviour_weightage;
--
--    --education
--    SELECT COALESCE((select weight from weightage where parameter = 'EDUCATION'
--    and ((select count(*) from user_profile up1, user_profile up2 where up1.user_identity_id = userid and up2.user_identity_id = partnerid
--    and up1.education_id is not null and up2.education_id is not null
--    and up1.education_id = up2.education_id) > 0)), 0) INTO education_weightage;
--
--    --ethnicity
--    SELECT COALESCE((select weight from weightage where parameter = 'ETHNICITY'
--    and ((select count(*) from user_profile up1, user_profile up2 where up1.user_identity_id = userid and up2.user_identity_id = partnerid
--    and up1.ethnicity_id is not null and up2.ethnicity_id is not null and up1.ethnicity_id = up2.ethnicity_id) > 0)), 0) INTO ethnicity_weightage;
--
--    --exercise
--    SELECT COALESCE((select weight from weightage where parameter = 'EXERCISE'
--    and ((select count(*) from user_profile up1, user_profile up2
--    where up1.user_identity_id = userid
--    and up2.user_identity_id = partnerid
--    and up1.is_exercise is not null and up2.is_exercise is not null and up1.is_exercise = up2.is_exercise) > 0)), 0) INTO exercise_weightage;
--
-- --vaccination_weightage
--    SELECT COALESCE((select weight from weightage where parameter = 'VACCINATION'
--    and ((select count(*) from user_profile up1, user_profile up2
--    where up1.user_identity_id = userid
--    and up2.user_identity_id = partnerid
--    and up1.is_vaccination_taken is not null and up2.is_vaccination_taken is not null and up1.is_vaccination_taken = up2.is_vaccination_taken) > 0)), 0) INTO vaccination_weightage;

--    --extrovert introvert
--    SELECT COALESCE((select weight from weightage where parameter = 'EXTROVERT_INTROVERT'
--    and ((select count(*) from user_profile up1, user_profile up2 where up1.user_identity_id = userid and up2.user_identity_id = partnerid
--    and nullif(trim(up1.extrovert_introvert),'') is not null and nullif(trim(up2.extrovert_introvert),'') is not null
--    and lower(up1.extrovert_introvert) = lower(up2.extrovert_introvert)) > 0)), 0) INTO extro_intro_weightage;
--
--    --flower
--    SELECT COALESCE((select weight from weightage where parameter = 'FAVOURITE_FLOWER'
--    and ((select count(*) from user_profile up1, user_profile up2 where up1.user_identity_id = userid and up2.user_identity_id = partnerid
--    and up1.flower_id is not null and up2.flower_id is not null
--    and up1.flower_id = up2.flower_id) > 0)), 0) INTO flower_weightage;
--
--    --food
--    SELECT COALESCE((select weight from weightage where parameter = 'FOODS'
--    and ((select count(*) from user_profile up1, user_profile up2 where up1.user_identity_id = userid and up2.user_identity_id = partnerid
--    and up1.food_id is not null and up2.food_id is not null
--    and up1.food_id = up2.food_id) > 0)), 0) INTO food_weightage;
--
--    --drinks
--    SELECT COALESCE((select weight from weightage where parameter = 'DRINKS'
--    and ((select count(*) from user_profile up1, user_profile up2 where up1.user_identity_id = userid and up2.user_identity_id = partnerid
--    and up1.drink_id is not null and up2.drink_id is not null
--    and up1.drink_id = up2.drink_id) > 0)), 0) INTO drinks_weightage;
--
--    --gender prefernces
--    SELECT COALESCE((select weight from weightage where parameter = 'GENDER_PREFERENCE'
--    and ((select count(*) from user_profile_gender_preference upgp1, user_profile_gender_preference upgp2
--    where upgp1.user_profile_id = userid and upgp2.user_profile_id = partnerid
--    and upgp1.gender_preference is not null and upgp2.gender_preference is not null
--    and upgp1.gender_preference = upgp2.gender_preference) > 0)), 0) INTO gender_weightage;
--
--    --pets
--    SELECT COALESCE((select weight from weightage where parameter = 'PETS'
--    and ((select count(*) from user_profile up1, user_profile up2 where up1.user_identity_id = userid and up2.user_identity_id = partnerid
--    and up1.pet_id is not null and up2.pet_id is not null
--    and up1.pet_id = up2.pet_id) > 0)), 0) INTO pets_weightage;
--
--    --politics
--    SELECT COALESCE((select weight from weightage where parameter = 'POLITICS'
--    and ((select count(*) from user_profile up1, user_profile up2 where up1.user_identity_id = userid and up2.user_identity_id = partnerid
--    and nullif(trim(up1.politics),'') is not null and nullif(trim(up2.politics),'') is not null
--    and lower(up1.politics) = lower(up2.politics)) > 0)), 0) INTO politics_weightage;
--
--    --profession
--	select COALESCE((select weight from weightage where parameter = 'PROFESSION'
--	and ((select count(*) from user_profile up1, user_profile up2 where up1.user_identity_id = userid and up2.user_identity_id = partnerid
--	and up1.profession_id is not null and up2.profession_id is not null
--	and up1.profession_id = up2.profession_id) > 0)), 0) INTO profession_weightage;
--
--	--religion
--	select COALESCE((select weight from weightage where parameter = 'RELIGION'
--	and ((select count(*) from user_profile up1, user_profile up2 where up1.user_identity_id = userid and up2.user_identity_id = partnerid
--	and up1.religion_id is not null and up2.religion_id is not null
--	and up1.religion_id = up2.religion_id) > 0)), 0) INTO religion_weightage;
--
--	--salary
--	select COALESCE((select weight from weightage where parameter = 'SALARY'
--	and ((select count(*) from user_profile up1, user_profile up2 where up1.user_identity_id = userid and up2.user_identity_id = partnerid
--	and up1.salary_id is not null and up2.salary_id is not null
--	and up1.salary_id = up2.salary_id) > 0)), 0) INTO salary_weightage;
--
--	--smoking
--	select COALESCE((select weight from weightage where parameter = 'SMOKING'
--	and ((select count(*) from user_profile up1, user_profile up2
--	where up1.user_identity_id = userid
--	and up2.user_identity_id = partnerid
--	and up1.is_smoking is not null and up2.is_smoking is not null and up1.is_smoking = up2.is_smoking) > 0)), 0) INTO smoking_weightage;
--
--	--voting
--	select COALESCE((select weight from weightage where parameter = 'VOTING'
--	and ((select count(*) from user_profile up1, user_profile up2
--	where up1.user_identity_id = userid
--	and up2.user_identity_id = partnerid
--	and up1.is_voting is not null and up2.is_voting is not null and up1.is_voting = up2.is_voting) > 0)), 0) INTO voting_weightage;
--
--
--	--past time favorites
--	activities_weightage = calculateWeightForPastimeActivities(userid, partnerid);
--	hobbies_weightage = calculateWeightForPastimeHobbies(userid, partnerid);
--	interests_weightage = calculateWeightForPastimeInterests(userid, partnerid);
--
--	--budget
--	select coalesce((select weight from weightage where parameter = 'BUDGET'
--	and ((select count(*) from dating_preference dp1, dating_preference dp2
--	where dp1.user_identity_id = userid and dp2.user_identity_id = partnerid
--	and dp1.is_active = true and dp2.is_active = true
--	and dp1.budget_range_id is not null and dp2.budget_range_id is not null
--	and dp1.budget_range_id = dp2.budget_range_id) > 0)), 0) INTO budget_weightage;
--
--	--place
--	select coalesce((select weight from weightage where parameter = 'PLACE'
--	and ((select count(*) from dating_preference dp1, dating_preference dp2
--	where dp1.user_identity_id = userid and dp2.user_identity_id = partnerid
--	and dp1.is_active = true and dp2.is_active = true
--	and dp1.place_id is not null and dp2.place_id is not null
--	and dp1.place_id = dp2.place_id) > 0)), 0) INTO place_weightage;
--
--	--time
--	select coalesce((select weight from weightage where parameter = 'TIME'
--	and ((select count(*) from dating_preference dp1, dating_preference dp2
--	where dp1.user_identity_id = userid and dp2.user_identity_id = partnerid
--	and dp1.is_active = true and dp2.is_active = true
--	and COALESCE(trim(dp1."time"),'') is not null and COALESCE(trim(dp2."time"),'') is not null
--	and lower(dp1."time") = lower(dp2."time")) > 0)), 0) INTO time_weightage;
--
--	--ridesharing
--	select coalesce((select weight from weightage where parameter = 'RIDE_SHARING'
--	and ((select count(*) from dating_preference dp1, dating_preference dp2
--	where dp1.user_identity_id = userid and dp2.user_identity_id = partnerid
--	and dp1.is_active = true and dp2.is_active = true
--	and dp1.is_ride_sharing is not null and dp2.is_ride_sharing is not null
--	and dp1.is_ride_sharing = dp2.is_ride_sharing) > 0)), 0) INTO ridesharing_weightage;
--
--
--    percentage = city_weightage + height_weightage + age_weightage + maritalstatus_weightage + havekids_weightage +
--               attire_weightage+drinkingbehaviour_weightage+education_weightage+ethnicity_weightage+exercise_weightage+vaccination_weightage+
--               extro_intro_weightage+flower_weightage+food_weightage+drinks_weightage+gender_weightage+pets_weightage+politics_weightage+
--               profession_weightage+religion_weightage+salary_weightage+smoking_weightage+voting_weightage+budget_weightage+
--               place_weightage+time_weightage+ridesharing_weightage+activities_weightage+hobbies_weightage+interests_weightage;
--
--return percentage;
--END
--$BODY$;
--
--
----select public.calculateWeightage('6e194c06-2067-4382-a79e-49f7384da3fc', '76a3135f-7c63-467c-90b3-0eabbeb56c59');
--
---------------------------------------------------------------------------------------------------------------------
--DROP FUNCTION if exists public.calculateWeightForPastimeInterests;
--CREATE or REPLACE FUNCTION public.calculateWeightForPastimeInterests(IN user_id uuid, IN partner_id uuid)
--RETURNS float
--LANGUAGE 'plpgsql'
--
--AS $BODY$
--DECLARE percentage float;
--DECLARE totalCount float;
--DECLARE matchedCount float;
--DECLARE interests_weightage float;
--
--begin
--  percentage=0;
--  --for common interests
--  SELECT COALESCE((select count(distinct ptfi1.interest) from past_time_favs_interest ptfi1
--  inner join past_time_favs ptf1 on ptf1.id = ptfi1.past_time_favs_id
--  where ptf1.user_identity_id = user_id
--  and ptfi1.interest in (select ptfi2.interest from past_time_favs_interest ptfi2
--  inner join past_time_favs ptf2 on ptf2.id = ptfi2.past_time_favs_id
--  where ptf2.user_identity_id = partner_id)), 0) INTO matchedCount;
--
--  --raise notice 'matchedCount: %', matchedCount;
-- raise notice 'matchedCount : %  %', matchedCount, E'\n';
-- --for combined interest without duplicate values
--  SELECT COALESCE((SELECT COUNT(*)
-- from (select ptfi1.interest from past_time_favs_interest ptfi1 inner join past_time_favs ptf1 on ptf1.id = ptfi1.past_time_favs_id
--  where ptf1.user_identity_id = user_id
--  union
--  select ptfi2.interest from past_time_favs_interest ptfi2 inner join past_time_favs ptf2 on ptf2.id = ptfi2.past_time_favs_id
--  where ptf2.user_identity_id = partner_id) as interestCount), 0) INTO totalCount ;
--
--  --raise notice 'totalCount: %', totalCount;
--  raise notice 'totalCount : %  %', totalCount, E'\n';
--
--  select weight INTO interests_weightage from weightage w where w."parameter" = 'INTERESTS';
--
--  --raise notice 'weight: %', interests_weightage;
--  raise notice 'interests_weightage : %  %', interests_weightage, E'\n';
--
--  if(totalCount > 0) then
--	percentage = (matchedCount / totalCount ) * interests_weightage;
--    --raise notice 'percentage 1: %', percentage;
--    raise notice 'percentage 1 : %  %', percentage, E'\n';
--  end if;
--
---- raise notice 'percentage 2: %', percentage;
--    raise notice 'percentage 2 : %  %', percentage, E'\n';
--return percentage;
--END
--$BODY$;
------------------------------------------------------------------------------------------------------------------
--DROP FUNCTION if exists public.calculateWeightForPastimeActivities;
--CREATE or REPLACE FUNCTION public.calculateWeightForPastimeActivities(IN user_id uuid, IN partner_id uuid)
--RETURNS float
--LANGUAGE 'plpgsql'
--
--AS $BODY$
--DECLARE percentage float;
--DECLARE totalCount float;
--DECLARE matchedCount float;
--DECLARE activities_weightage float;
--
--begin
--  percentage=0;
--  --for common activities
--  SELECT COALESCE((select count(distinct ptfa1.activities) from past_time_favs_activities ptfa1
--  inner join past_time_favs ptf1 on ptf1.id = ptfa1.past_time_favs_id
--  where ptf1.user_identity_id = user_id
--  and ptfa1.activities in (select ptfa2.activities from past_time_favs_activities ptfa2
--  inner join past_time_favs ptf2 on ptf2.id = ptfa2.past_time_favs_id
--  where ptf2.user_identity_id = partner_id)), 0) INTO matchedCount;
--
--  --raise notice 'matchedCount: %', matchedCount;
-- raise notice 'matchedCount : %  %', matchedCount, E'\n';
-- --for combined interest without duplicate values
--  SELECT COALESCE((SELECT COUNT(*)
-- from (select ptfa1.activities from past_time_favs_activities ptfa1 inner join past_time_favs ptf1 on ptf1.id = ptfa1.past_time_favs_id
--  where ptf1.user_identity_id = user_id
--  union
--  select ptfa2.activities from past_time_favs_activities ptfa2 inner join past_time_favs ptf2 on ptf2.id = ptfa2.past_time_favs_id
--  where ptf2.user_identity_id = partner_id) as activitiesCount), 0) INTO totalCount ;
--
--  --raise notice 'totalCount: %', totalCount;
--  raise notice 'totalCount : %  %', totalCount, E'\n';
--
--  select weight INTO activities_weightage from weightage w where w."parameter" = 'ACTIVITIES';
--
--  --raise notice 'weight: %', activities_weightage;
--  raise notice 'activities_weightage : %  %', activities_weightage, E'\n';
--
--  if(totalCount > 0) then
--	percentage = (matchedCount / totalCount ) * activities_weightage;
--    --raise notice 'percentage 1: %', percentage;
--    raise notice 'percentage 1 : %  %', percentage, E'\n';
--  end if;
--
---- raise notice 'percentage 2: %', percentage;
--    raise notice 'percentage 2 : %  %', percentage, E'\n';
--return percentage;
--END
--$BODY$;
--
----select public.calculateWeightForPastimeActivities('6e194c06-2067-4382-a79e-49f7384da3fc', '76a3135f-7c63-467c-90b3-0eabbeb56c59');
--
--
--------------------------------------------------------------------------------------------------------
--DROP FUNCTION if exists public.calculateWeightForPastimeHobbies;
--CREATE or REPLACE FUNCTION public.calculateWeightForPastimeHobbies(IN user_id uuid, IN partner_id uuid)
--RETURNS float
--LANGUAGE 'plpgsql'
--
--AS $BODY$
--DECLARE percentage float;
--DECLARE totalCount float;
--DECLARE matchedCount float;
--DECLARE hobbies_weightage float;
--
--begin
-- percentage=0;
----for common hobbies
--select count(distinct ptfh1.hobbies) INTO matchedCount from past_time_favs_hobbies ptfh1 inner join past_time_favs ptf1 on ptf1.id = ptfh1.past_time_favs_id
--where ptf1.user_identity_id = user_id
--and ptfh1.hobbies in (select ptfh2.hobbies from past_time_favs_hobbies ptfh2 inner join past_time_favs ptf2 on ptf2.id = ptfh2.past_time_favs_id
--where ptf2.user_identity_id = partner_id);
--
--raise notice 'matchedCount : % %', matchedCount, E'\n';
--
----for combined hobbies without duplicate values
--SELECT COALESCE((SELECT count(*) FROM
--(
--select ptfh1.hobbies from past_time_favs_hobbies ptfh1 inner join past_time_favs ptf1 on ptf1.id = ptfh1.past_time_favs_id
--where ptf1.user_identity_id = user_id
--UNION ALL
--select ptfh2.hobbies from past_time_favs_hobbies ptfh2 inner join past_time_favs ptf2 on ptf2.id = ptfh2.past_time_favs_id
--where ptf2.user_identity_id = partner_id
--) as hobbyCount), 0) into totalCount;
--
----raise notice 'totalCount: %', totalCount;
--raise notice 'totalCount : % %', totalCount, E'\n';
--
--select weight INTO hobbies_weightage from weightage w where w."parameter" = 'HOBBIES';
--
----raise notice 'weight: %', interests_weightage;
--raise notice 'hobbies_weightage : % %', hobbies_weightage, E'\n';
--
--if(totalCount > 0) then
--percentage = (matchedCount / totalCount ) * hobbies_weightage;
--raise notice 'percentage 1 : % %', percentage, E'\n';
--end if;
--
---- raise notice 'percentage 2: %', percentage;
--raise notice 'percentage 2 : % %', percentage, E'\n';
--return percentage;
--END
--$BODY$;

--select public.calculateWeightForPastimeHobbies('6e194c06-2067-4382-a79e-49f7384da3fc', '76a3135f-7c63-467c-90b3-0eabbeb56c59');