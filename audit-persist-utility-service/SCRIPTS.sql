--------------------------------------------------------
--  File created - Wednesday-October-29-2025
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Sequence BATCH_JOB_EXECUTION_SEQ
--------------------------------------------------------

CREATE SEQUENCE  "BATCH_JOB_EXECUTION_SEQ"  MINVALUE 0 MAXVALUE 9223372036854775807 INCREMENT BY 1 START WITH 200 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL
--------------------------------------------------------
--  DDL for Sequence BATCH_JOB_SEQ
--------------------------------------------------------

CREATE SEQUENCE  "BATCH_JOB_SEQ"  MINVALUE 0 MAXVALUE 9223372036854775807 INCREMENT BY 1 START WITH 200 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL
--------------------------------------------------------
--  DDL for Sequence BATCH_STEP_EXECUTION_SEQ
--------------------------------------------------------

CREATE SEQUENCE  "BATCH_STEP_EXECUTION_SEQ"  MINVALUE 0 MAXVALUE 9223372036854775807 INCREMENT BY 1 START WITH 300 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL

--------------------------------------------------------
--  File created - Wednesday-October-29-2025
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table APPLICATION_INSTANCES
--------------------------------------------------------

CREATE TABLE "APPLICATION_INSTANCES" ("ID" VARCHAR2(36 CHAR), "CREATION_DATE" TIMESTAMP (6), "APPLICATION_ID" VARCHAR2(36 CHAR))
--------------------------------------------------------
--  DDL for Table APPLICATIONS
--------------------------------------------------------

CREATE TABLE "APPLICATIONS" ("ID" VARCHAR2(36 CHAR), "CREATION_DATE" TIMESTAMP (6), "NAME" VARCHAR2(200 CHAR), "VERSION" VARCHAR2(200 CHAR))
--------------------------------------------------------
--  DDL for Table BATCH_JOB_EXECUTION
--------------------------------------------------------

CREATE TABLE "BATCH_JOB_EXECUTION" ("JOB_EXECUTION_ID" NUMBER(19,0), "VERSION" NUMBER(19,0), "JOB_INSTANCE_ID" NUMBER(19,0), "CREATE_TIME" TIMESTAMP (6), "START_TIME" TIMESTAMP (6) DEFAULT NULL, "END_TIME" TIMESTAMP (6) DEFAULT NULL, "STATUS" VARCHAR2(10 CHAR), "EXIT_CODE" VARCHAR2(2500 CHAR), "EXIT_MESSAGE" VARCHAR2(2500 CHAR), "LAST_UPDATED" TIMESTAMP (6), "JOB_CONFIGURATION_LOCATION" VARCHAR2(2500 CHAR))
--------------------------------------------------------
--  DDL for Table BATCH_JOB_EXECUTION_CONTEXT
--------------------------------------------------------

CREATE TABLE "BATCH_JOB_EXECUTION_CONTEXT" ("JOB_EXECUTION_ID" NUMBER(19,0), "SHORT_CONTEXT" VARCHAR2(2500 CHAR), "SERIALIZED_CONTEXT" CLOB)
--------------------------------------------------------
--  DDL for Table BATCH_JOB_EXECUTION_PARAMS
--------------------------------------------------------

CREATE TABLE "BATCH_JOB_EXECUTION_PARAMS" ("JOB_EXECUTION_ID" NUMBER(19,0), "TYPE_CD" VARCHAR2(6 CHAR), "KEY_NAME" VARCHAR2(100 CHAR), "STRING_VAL" VARCHAR2(250 CHAR), "DATE_VAL" TIMESTAMP (6) DEFAULT NULL, "LONG_VAL" NUMBER(19,0), "DOUBLE_VAL" NUMBER, "IDENTIFYING" CHAR(1))
--------------------------------------------------------
--  DDL for Table BATCH_JOB_INSTANCE
--------------------------------------------------------

CREATE TABLE "BATCH_JOB_INSTANCE" ("JOB_INSTANCE_ID" NUMBER(19,0), "VERSION" NUMBER(19,0), "JOB_NAME" VARCHAR2(100 CHAR), "JOB_KEY" VARCHAR2(32 CHAR))
--------------------------------------------------------
--  DDL for Table BATCH_STEP_EXECUTION
--------------------------------------------------------

CREATE TABLE "BATCH_STEP_EXECUTION" ("STEP_EXECUTION_ID" NUMBER(19,0), "VERSION" NUMBER(19,0), "STEP_NAME" VARCHAR2(100 CHAR), "JOB_EXECUTION_ID" NUMBER(19,0), "START_TIME" TIMESTAMP (6), "END_TIME" TIMESTAMP (6) DEFAULT NULL, "STATUS" VARCHAR2(10 CHAR), "COMMIT_COUNT" NUMBER(19,0), "READ_COUNT" NUMBER(19,0), "FILTER_COUNT" NUMBER(19,0), "WRITE_COUNT" NUMBER(19,0), "READ_SKIP_COUNT" NUMBER(19,0), "WRITE_SKIP_COUNT" NUMBER(19,0), "PROCESS_SKIP_COUNT" NUMBER(19,0), "ROLLBACK_COUNT" NUMBER(19,0), "EXIT_CODE" VARCHAR2(2500 CHAR), "EXIT_MESSAGE" VARCHAR2(2500 CHAR), "LAST_UPDATED" TIMESTAMP (6))
--------------------------------------------------------
--  DDL for Table BATCH_STEP_EXECUTION_CONTEXT
--------------------------------------------------------

CREATE TABLE "BATCH_STEP_EXECUTION_CONTEXT" ("STEP_EXECUTION_ID" NUMBER(19,0), "SHORT_CONTEXT" VARCHAR2(2500 CHAR), "SERIALIZED_CONTEXT" CLOB)
--------------------------------------------------------
--  DDL for Table BUSINESS_REQUESTS
--------------------------------------------------------

CREATE TABLE "BUSINESS_REQUESTS" ("ID" VARCHAR2(36 CHAR), "CONTENT" CLOB, "EXCEPTION" CLOB, "INPUT_DATE" TIMESTAMP (6), "INPUT_LINE" VARCHAR2(255 CHAR), "PARENT_TREE_STATUS" VARCHAR2(255 CHAR), "TYPE" VARCHAR2(255 CHAR), "FUNCTION_REQUESTS_ID" VARCHAR2(36 CHAR), "HTTP_REQUESTS_ID" VARCHAR2(36 CHAR))
--------------------------------------------------------
--  DDL for Table CATEGORIES
--------------------------------------------------------

CREATE TABLE "CATEGORIES" ("ID" NUMBER(19,0) GENERATED ALWAYS AS IDENTITY MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE , "CREATED_AT" TIMESTAMP (6), "DESCRIPTION" VARCHAR2(1000 CHAR), "DISPLAY_ORDER" NUMBER(10,0), "IMAGE_URL" VARCHAR2(255 CHAR), "IS_ACTIVE" NUMBER(1,0), "NAME" VARCHAR2(255 CHAR), "PARENT_ID" NUMBER(19,0), "UPDATED_AT" TIMESTAMP (6))
--------------------------------------------------------
--  DDL for Table FUNCTION_REQUESTS
--------------------------------------------------------

CREATE TABLE "FUNCTION_REQUESTS" ("ID" VARCHAR2(36 CHAR), "DURATION" NUMBER(10,0), "EXCEPTION_INPUT" CLOB, "EXCEPTION_OUTPUT" VARCHAR2(255 CHAR), "FUNCTION_NAME" VARCHAR2(255 CHAR), "INPUT_DATE" TIMESTAMP (6), "INPUT_LINE" VARCHAR2(255 CHAR), "OUTPUT_DATE" TIMESTAMP (6), "OUTPUT_LINE" VARCHAR2(255 CHAR), "PACKAGE_PATH" VARCHAR2(255 CHAR), "PARENT_TREE_STATUS" VARCHAR2(255 CHAR), "PATH" VARCHAR2(255 CHAR), "RETURN_CONTENT" CLOB, "RETURN_TYPE" VARCHAR2(255 CHAR), "TYPE" VARCHAR2(255 CHAR), "EXCEPTION_FUNCTION_REQUESTS_ID" VARCHAR2(36 CHAR), "HTTP_REQUESTS_ID" VARCHAR2(36 CHAR), "PARENT_FUNCTION_REQUESTS_ID" VARCHAR2(36 CHAR))
--------------------------------------------------------
--  DDL for Table FUNCTION_REQUESTS_ARGUMENTS
--------------------------------------------------------

CREATE TABLE "FUNCTION_REQUESTS_ARGUMENTS" ("ID" VARCHAR2(36 CHAR), "CONTENT" CLOB, "TYPE" VARCHAR2(255 CHAR), "FUNCTION_REQUESTS_ID" VARCHAR2(36 CHAR))
--------------------------------------------------------
--  DDL for Table HTTP_REQUESTS
--------------------------------------------------------

CREATE TABLE "HTTP_REQUESTS" ("ID" VARCHAR2(36 CHAR), "DURATION" NUMBER(10,0), "EXCEPTION_INPUT" CLOB, "EXCEPTION_OUTPUT" CLOB, "HOST" VARCHAR2(255 CHAR), "INPUT_DATE" TIMESTAMP (6), "INPUT_LINE" VARCHAR2(255 CHAR), "LOG_PATH" VARCHAR2(255 CHAR), "METHOD" VARCHAR2(255 CHAR), "OUTPUT_DATE" TIMESTAMP (6), "OUTPUT_LINE" VARCHAR2(255 CHAR), "STATUS" NUMBER(10,0), "URL" VARCHAR2(255 CHAR), "APPLICATION_INSTANCES_ID" VARCHAR2(36 CHAR), "HTTP_REQUESTS_ID" VARCHAR2(36 CHAR))
--------------------------------------------------------
--  DDL for Table HTTP_REQUESTS_HEADERS
--------------------------------------------------------

CREATE TABLE "HTTP_REQUESTS_HEADERS" ("ID" VARCHAR2(36 CHAR), "CONTENT" CLOB, "HEADER" VARCHAR2(255 CHAR), "HTTP_HEADERS_ID" VARCHAR2(36 CHAR))
--------------------------------------------------------
--  DDL for Table HTTP_REQUESTS_PARAMETERS
--------------------------------------------------------

CREATE TABLE "HTTP_REQUESTS_PARAMETERS" ("ID" VARCHAR2(36 CHAR), "CONTENT" CLOB, "PARAMETER" VARCHAR2(255 CHAR), "HTTP_REQUESTS_ID" VARCHAR2(36 CHAR))
--------------------------------------------------------
--  DDL for Table LOG_DUMP
--------------------------------------------------------

CREATE TABLE "LOG_DUMP" ("ID" VARCHAR2(36 CHAR), "APPLICATION_INSTANCE" VARCHAR2(255 CHAR), "APPLICATION_NAME" VARCHAR2(255 CHAR), "APPLICATION_VERSION" VARCHAR2(255 CHAR), "CONTENT" CLOB, "EXCEPTION" VARCHAR2(255 CHAR), "PERSISTED" VARCHAR2(255 CHAR), "LOG_TIME" TIMESTAMP (6))
--------------------------------------------------------
--  DDL for Table ORDER_ITEMS
--------------------------------------------------------

CREATE TABLE "ORDER_ITEMS" ("ID" NUMBER(19,0) GENERATED ALWAYS AS IDENTITY MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE , "DISCOUNT_AMOUNT" NUMBER(19,2), "QUANTITY" NUMBER(10,0), "TOTAL_PRICE" NUMBER(19,2), "UNIT_PRICE" NUMBER(19,2), "ORDER_ID" NUMBER(19,0), "PRODUCT_ID" NUMBER(19,0))
--------------------------------------------------------
--  DDL for Table ORDERS
--------------------------------------------------------

CREATE TABLE "ORDERS" ("ID" NUMBER(19,0) GENERATED ALWAYS AS IDENTITY MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE , "BILLING_ADDRESS" VARCHAR2(500 CHAR), "CREATED_AT" TIMESTAMP (6), "DELIVERED_AT" TIMESTAMP (6), "DISCOUNT_AMOUNT" NUMBER(19,2), "NOTES" VARCHAR2(1000 CHAR), "ORDER_NUMBER" VARCHAR2(255 CHAR), "PAYMENT_METHOD" VARCHAR2(255 CHAR), "PAYMENT_STATUS" VARCHAR2(255 CHAR), "SHIPPED_AT" TIMESTAMP (6), "SHIPPING_ADDRESS" VARCHAR2(500 CHAR), "SHIPPING_COST" NUMBER(19,2), "STATUS" VARCHAR2(255 CHAR), "TAX_AMOUNT" NUMBER(19,2), "TOTAL_AMOUNT" NUMBER(19,2), "TRACKING_NUMBER" VARCHAR2(255 CHAR), "UPDATED_AT" TIMESTAMP (6), "USER_ID" NUMBER(19,0))
--------------------------------------------------------
--  DDL for Table PRODUCTS
--------------------------------------------------------

CREATE TABLE "PRODUCTS" ("ID" NUMBER(19,0) GENERATED ALWAYS AS IDENTITY MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE , "BRAND" VARCHAR2(255 CHAR), "CREATED_AT" TIMESTAMP (6), "DESCRIPTION" VARCHAR2(2000 CHAR), "DISCOUNT_PERCENTAGE" NUMBER(19,2), "IMAGE_URL" VARCHAR2(255 CHAR), "NAME" VARCHAR2(255 CHAR), "PRICE" NUMBER(19,2), "RATING" FLOAT(126), "REVIEW_COUNT" NUMBER(10,0), "SKU" VARCHAR2(255 CHAR), "STATUS" VARCHAR2(255 CHAR), "STOCK_QUANTITY" NUMBER(10,0), "UPDATED_AT" TIMESTAMP (6), "CATEGORY_ID" NUMBER(19,0))
--------------------------------------------------------
--  DDL for Table USERS
--------------------------------------------------------

CREATE TABLE "USERS" ("ID" NUMBER(19,0) GENERATED ALWAYS AS IDENTITY MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE , "CREATED_AT" TIMESTAMP (6), "EMAIL" VARCHAR2(255 CHAR), "FIRST_NAME" VARCHAR2(255 CHAR), "LAST_LOGIN" TIMESTAMP (6), "LAST_NAME" VARCHAR2(255 CHAR), "PASSWORD" VARCHAR2(255 CHAR), "PHONE_NUMBER" VARCHAR2(255 CHAR), "ROLE" VARCHAR2(255 CHAR), "STATUS" VARCHAR2(255 CHAR), "UPDATED_AT" TIMESTAMP (6), "USERNAME" VARCHAR2(255 CHAR))
--------------------------------------------------------
--  DDL for Index SYS_C007477
--------------------------------------------------------

CREATE UNIQUE INDEX "SYS_C007477" ON "APPLICATION_INSTANCES" ("ID")
--------------------------------------------------------
--  DDL for Index SYS_C007479
--------------------------------------------------------

CREATE UNIQUE INDEX "SYS_C007479" ON "APPLICATIONS" ("ID")
--------------------------------------------------------
--  DDL for Index SYS_C007429
--------------------------------------------------------

CREATE UNIQUE INDEX "SYS_C007429" ON "BATCH_JOB_EXECUTION" ("JOB_EXECUTION_ID")
--------------------------------------------------------
--  DDL for Index SYS_C007449
--------------------------------------------------------

CREATE UNIQUE INDEX "SYS_C007449" ON "BATCH_JOB_EXECUTION_CONTEXT" ("JOB_EXECUTION_ID")
--------------------------------------------------------
--  DDL for Index SYS_C007424
--------------------------------------------------------

CREATE UNIQUE INDEX "SYS_C007424" ON "BATCH_JOB_INSTANCE" ("JOB_INSTANCE_ID")
--------------------------------------------------------
--  DDL for Index JOB_INST_UN
--------------------------------------------------------

CREATE UNIQUE INDEX "JOB_INST_UN" ON "BATCH_JOB_INSTANCE" ("JOB_NAME", "JOB_KEY")
--------------------------------------------------------
--  DDL for Index SYS_C007441
--------------------------------------------------------

CREATE UNIQUE INDEX "SYS_C007441" ON "BATCH_STEP_EXECUTION" ("STEP_EXECUTION_ID")
--------------------------------------------------------
--  DDL for Index SYS_C007445
--------------------------------------------------------

CREATE UNIQUE INDEX "SYS_C007445" ON "BATCH_STEP_EXECUTION_CONTEXT" ("STEP_EXECUTION_ID")
--------------------------------------------------------
--  DDL for Index SYS_C007484
--------------------------------------------------------

CREATE UNIQUE INDEX "SYS_C007484" ON "BUSINESS_REQUESTS" ("ID")
--------------------------------------------------------
--  DDL for Index UK_T8O6PIVUR7NN124JEHX7CYGW5
--------------------------------------------------------

CREATE UNIQUE INDEX "UK_T8O6PIVUR7NN124JEHX7CYGW5" ON "CATEGORIES" ("NAME")
--------------------------------------------------------
--  DDL for Index SYS_C007516
--------------------------------------------------------

CREATE UNIQUE INDEX "SYS_C007516" ON "CATEGORIES" ("ID")
--------------------------------------------------------
--  DDL for Index SYS_C007486
--------------------------------------------------------

CREATE UNIQUE INDEX "SYS_C007486" ON "FUNCTION_REQUESTS" ("ID")
--------------------------------------------------------
--  DDL for Index SYS_C007500
--------------------------------------------------------

CREATE UNIQUE INDEX "SYS_C007500" ON "FUNCTION_REQUESTS_ARGUMENTS" ("ID")
--------------------------------------------------------
--  DDL for Index SYS_C007490
--------------------------------------------------------

CREATE UNIQUE INDEX "SYS_C007490" ON "HTTP_REQUESTS" ("ID")
--------------------------------------------------------
--  DDL for Index SYS_C007503
--------------------------------------------------------

CREATE UNIQUE INDEX "SYS_C007503" ON "HTTP_REQUESTS_HEADERS" ("ID")
--------------------------------------------------------
--  DDL for Index SYS_C007506
--------------------------------------------------------

CREATE UNIQUE INDEX "SYS_C007506" ON "HTTP_REQUESTS_PARAMETERS" ("ID")
--------------------------------------------------------
--  DDL for Index SYS_C007481
--------------------------------------------------------

CREATE UNIQUE INDEX "SYS_C007481" ON "LOG_DUMP" ("ID")
--------------------------------------------------------
--  DDL for Index SYS_C007523
--------------------------------------------------------

CREATE UNIQUE INDEX "SYS_C007523" ON "ORDER_ITEMS" ("ID")
--------------------------------------------------------
--  DDL for Index UK_NTHKIU7PGMNQNU86I2JYOE2V7
--------------------------------------------------------

CREATE UNIQUE INDEX "UK_NTHKIU7PGMNQNU86I2JYOE2V7" ON "ORDERS" ("ORDER_NUMBER")
--------------------------------------------------------
--  DDL for Index SYS_C007529
--------------------------------------------------------

CREATE UNIQUE INDEX "SYS_C007529" ON "ORDERS" ("ID")
--------------------------------------------------------
--  DDL for Index SYS_C007533
--------------------------------------------------------

CREATE UNIQUE INDEX "SYS_C007533" ON "PRODUCTS" ("ID")
--------------------------------------------------------
--  DDL for Index SYS_C007538
--------------------------------------------------------

CREATE UNIQUE INDEX "SYS_C007538" ON "USERS" ("ID")
--------------------------------------------------------
--  DDL for Index UK_6DOTKOTT2KJSP8VW4D0M25FB7
--------------------------------------------------------

CREATE UNIQUE INDEX "UK_6DOTKOTT2KJSP8VW4D0M25FB7" ON "USERS" ("EMAIL")
--------------------------------------------------------
--  DDL for Index UK_R43AF9AP4EDM43MMTQ01ODDJ6
--------------------------------------------------------

CREATE UNIQUE INDEX "UK_R43AF9AP4EDM43MMTQ01ODDJ6" ON "USERS" ("USERNAME")
--------------------------------------------------------
--  Constraints for Table APPLICATION_INSTANCES
--------------------------------------------------------

ALTER TABLE "APPLICATION_INSTANCES" MODIFY ("ID" NOT NULL ENABLE)
ALTER TABLE "APPLICATION_INSTANCES" MODIFY ("APPLICATION_ID" NOT NULL ENABLE)
ALTER TABLE "APPLICATION_INSTANCES" ADD PRIMARY KEY ("ID") USING INDEX  ENABLE
--------------------------------------------------------
--  Constraints for Table APPLICATIONS
--------------------------------------------------------

ALTER TABLE "APPLICATIONS" MODIFY ("ID" NOT NULL ENABLE)
ALTER TABLE "APPLICATIONS" ADD PRIMARY KEY ("ID") USING INDEX  ENABLE
--------------------------------------------------------
--  Constraints for Table BATCH_JOB_EXECUTION
--------------------------------------------------------

ALTER TABLE "BATCH_JOB_EXECUTION" MODIFY ("JOB_EXECUTION_ID" NOT NULL ENABLE)
ALTER TABLE "BATCH_JOB_EXECUTION" MODIFY ("JOB_INSTANCE_ID" NOT NULL ENABLE)
ALTER TABLE "BATCH_JOB_EXECUTION" MODIFY ("CREATE_TIME" NOT NULL ENABLE)
ALTER TABLE "BATCH_JOB_EXECUTION" ADD PRIMARY KEY ("JOB_EXECUTION_ID") USING INDEX  ENABLE
--------------------------------------------------------
--  Constraints for Table BATCH_JOB_EXECUTION_CONTEXT
--------------------------------------------------------

ALTER TABLE "BATCH_JOB_EXECUTION_CONTEXT" MODIFY ("JOB_EXECUTION_ID" NOT NULL ENABLE)
ALTER TABLE "BATCH_JOB_EXECUTION_CONTEXT" MODIFY ("SHORT_CONTEXT" NOT NULL ENABLE)
ALTER TABLE "BATCH_JOB_EXECUTION_CONTEXT" ADD PRIMARY KEY ("JOB_EXECUTION_ID") USING INDEX  ENABLE
--------------------------------------------------------
--  Constraints for Table BATCH_JOB_EXECUTION_PARAMS
--------------------------------------------------------

ALTER TABLE "BATCH_JOB_EXECUTION_PARAMS" MODIFY ("JOB_EXECUTION_ID" NOT NULL ENABLE)
ALTER TABLE "BATCH_JOB_EXECUTION_PARAMS" MODIFY ("TYPE_CD" NOT NULL ENABLE)
ALTER TABLE "BATCH_JOB_EXECUTION_PARAMS" MODIFY ("KEY_NAME" NOT NULL ENABLE)
ALTER TABLE "BATCH_JOB_EXECUTION_PARAMS" MODIFY ("IDENTIFYING" NOT NULL ENABLE)
--------------------------------------------------------
--  Constraints for Table BATCH_JOB_INSTANCE
--------------------------------------------------------

ALTER TABLE "BATCH_JOB_INSTANCE" MODIFY ("JOB_INSTANCE_ID" NOT NULL ENABLE)
ALTER TABLE "BATCH_JOB_INSTANCE" MODIFY ("JOB_NAME" NOT NULL ENABLE)
ALTER TABLE "BATCH_JOB_INSTANCE" MODIFY ("JOB_KEY" NOT NULL ENABLE)
ALTER TABLE "BATCH_JOB_INSTANCE" ADD PRIMARY KEY ("JOB_INSTANCE_ID") USING INDEX  ENABLE
ALTER TABLE "BATCH_JOB_INSTANCE" ADD CONSTRAINT "JOB_INST_UN" UNIQUE ("JOB_NAME", "JOB_KEY") USING INDEX  ENABLE
--------------------------------------------------------
--  Constraints for Table BATCH_STEP_EXECUTION
--------------------------------------------------------

ALTER TABLE "BATCH_STEP_EXECUTION" MODIFY ("STEP_EXECUTION_ID" NOT NULL ENABLE)
ALTER TABLE "BATCH_STEP_EXECUTION" MODIFY ("VERSION" NOT NULL ENABLE)
ALTER TABLE "BATCH_STEP_EXECUTION" MODIFY ("STEP_NAME" NOT NULL ENABLE)
ALTER TABLE "BATCH_STEP_EXECUTION" MODIFY ("JOB_EXECUTION_ID" NOT NULL ENABLE)
ALTER TABLE "BATCH_STEP_EXECUTION" MODIFY ("START_TIME" NOT NULL ENABLE)
ALTER TABLE "BATCH_STEP_EXECUTION" ADD PRIMARY KEY ("STEP_EXECUTION_ID") USING INDEX  ENABLE
--------------------------------------------------------
--  Constraints for Table BATCH_STEP_EXECUTION_CONTEXT
--------------------------------------------------------

ALTER TABLE "BATCH_STEP_EXECUTION_CONTEXT" MODIFY ("STEP_EXECUTION_ID" NOT NULL ENABLE)
ALTER TABLE "BATCH_STEP_EXECUTION_CONTEXT" MODIFY ("SHORT_CONTEXT" NOT NULL ENABLE)
ALTER TABLE "BATCH_STEP_EXECUTION_CONTEXT" ADD PRIMARY KEY ("STEP_EXECUTION_ID") USING INDEX  ENABLE
--------------------------------------------------------
--  Constraints for Table BUSINESS_REQUESTS
--------------------------------------------------------

ALTER TABLE "BUSINESS_REQUESTS" MODIFY ("ID" NOT NULL ENABLE)
ALTER TABLE "BUSINESS_REQUESTS" ADD PRIMARY KEY ("ID") USING INDEX  ENABLE
--------------------------------------------------------
--  Constraints for Table CATEGORIES
--------------------------------------------------------

ALTER TABLE "CATEGORIES" MODIFY ("ID" NOT NULL ENABLE)
ALTER TABLE "CATEGORIES" MODIFY ("NAME" NOT NULL ENABLE)
ALTER TABLE "CATEGORIES" ADD PRIMARY KEY ("ID") USING INDEX  ENABLE
ALTER TABLE "CATEGORIES" ADD CONSTRAINT "UK_T8O6PIVUR7NN124JEHX7CYGW5" UNIQUE ("NAME") USING INDEX  ENABLE
--------------------------------------------------------
--  Constraints for Table FUNCTION_REQUESTS
--------------------------------------------------------

ALTER TABLE "FUNCTION_REQUESTS" MODIFY ("ID" NOT NULL ENABLE)
ALTER TABLE "FUNCTION_REQUESTS" ADD PRIMARY KEY ("ID") USING INDEX  ENABLE
--------------------------------------------------------
--  Constraints for Table FUNCTION_REQUESTS_ARGUMENTS
--------------------------------------------------------

ALTER TABLE "FUNCTION_REQUESTS_ARGUMENTS" MODIFY ("ID" NOT NULL ENABLE)
ALTER TABLE "FUNCTION_REQUESTS_ARGUMENTS" MODIFY ("FUNCTION_REQUESTS_ID" NOT NULL ENABLE)
ALTER TABLE "FUNCTION_REQUESTS_ARGUMENTS" ADD PRIMARY KEY ("ID") USING INDEX  ENABLE
--------------------------------------------------------
--  Constraints for Table HTTP_REQUESTS
--------------------------------------------------------

ALTER TABLE "HTTP_REQUESTS" MODIFY ("ID" NOT NULL ENABLE)
ALTER TABLE "HTTP_REQUESTS" MODIFY ("STATUS" NOT NULL ENABLE)
ALTER TABLE "HTTP_REQUESTS" MODIFY ("APPLICATION_INSTANCES_ID" NOT NULL ENABLE)
ALTER TABLE "HTTP_REQUESTS" ADD PRIMARY KEY ("ID") USING INDEX  ENABLE
--------------------------------------------------------
--  Constraints for Table HTTP_REQUESTS_HEADERS
--------------------------------------------------------

ALTER TABLE "HTTP_REQUESTS_HEADERS" MODIFY ("ID" NOT NULL ENABLE)
ALTER TABLE "HTTP_REQUESTS_HEADERS" MODIFY ("HTTP_HEADERS_ID" NOT NULL ENABLE)
ALTER TABLE "HTTP_REQUESTS_HEADERS" ADD PRIMARY KEY ("ID") USING INDEX  ENABLE
--------------------------------------------------------
--  Constraints for Table HTTP_REQUESTS_PARAMETERS
--------------------------------------------------------

ALTER TABLE "HTTP_REQUESTS_PARAMETERS" MODIFY ("ID" NOT NULL ENABLE)
ALTER TABLE "HTTP_REQUESTS_PARAMETERS" MODIFY ("HTTP_REQUESTS_ID" NOT NULL ENABLE)
ALTER TABLE "HTTP_REQUESTS_PARAMETERS" ADD PRIMARY KEY ("ID") USING INDEX  ENABLE
--------------------------------------------------------
--  Constraints for Table LOG_DUMP
--------------------------------------------------------

ALTER TABLE "LOG_DUMP" MODIFY ("ID" NOT NULL ENABLE)
ALTER TABLE "LOG_DUMP" ADD PRIMARY KEY ("ID") USING INDEX  ENABLE
--------------------------------------------------------
--  Constraints for Table ORDER_ITEMS
--------------------------------------------------------

ALTER TABLE "ORDER_ITEMS" MODIFY ("ID" NOT NULL ENABLE)
ALTER TABLE "ORDER_ITEMS" MODIFY ("QUANTITY" NOT NULL ENABLE)
ALTER TABLE "ORDER_ITEMS" MODIFY ("TOTAL_PRICE" NOT NULL ENABLE)
ALTER TABLE "ORDER_ITEMS" MODIFY ("UNIT_PRICE" NOT NULL ENABLE)
ALTER TABLE "ORDER_ITEMS" MODIFY ("ORDER_ID" NOT NULL ENABLE)
ALTER TABLE "ORDER_ITEMS" MODIFY ("PRODUCT_ID" NOT NULL ENABLE)
ALTER TABLE "ORDER_ITEMS" ADD PRIMARY KEY ("ID") USING INDEX  ENABLE
--------------------------------------------------------
--  Constraints for Table ORDERS
--------------------------------------------------------

ALTER TABLE "ORDERS" MODIFY ("ID" NOT NULL ENABLE)
ALTER TABLE "ORDERS" MODIFY ("ORDER_NUMBER" NOT NULL ENABLE)
ALTER TABLE "ORDERS" MODIFY ("STATUS" NOT NULL ENABLE)
ALTER TABLE "ORDERS" MODIFY ("TOTAL_AMOUNT" NOT NULL ENABLE)
ALTER TABLE "ORDERS" MODIFY ("USER_ID" NOT NULL ENABLE)
ALTER TABLE "ORDERS" ADD PRIMARY KEY ("ID") USING INDEX  ENABLE
ALTER TABLE "ORDERS" ADD CONSTRAINT "UK_NTHKIU7PGMNQNU86I2JYOE2V7" UNIQUE ("ORDER_NUMBER") USING INDEX  ENABLE
--------------------------------------------------------
--  Constraints for Table PRODUCTS
--------------------------------------------------------

ALTER TABLE "PRODUCTS" MODIFY ("ID" NOT NULL ENABLE)
ALTER TABLE "PRODUCTS" MODIFY ("NAME" NOT NULL ENABLE)
ALTER TABLE "PRODUCTS" MODIFY ("PRICE" NOT NULL ENABLE)
ALTER TABLE "PRODUCTS" ADD PRIMARY KEY ("ID") USING INDEX  ENABLE
--------------------------------------------------------
--  Constraints for Table USERS
--------------------------------------------------------

ALTER TABLE "USERS" MODIFY ("ID" NOT NULL ENABLE)
ALTER TABLE "USERS" MODIFY ("EMAIL" NOT NULL ENABLE)
ALTER TABLE "USERS" MODIFY ("PASSWORD" NOT NULL ENABLE)
ALTER TABLE "USERS" MODIFY ("USERNAME" NOT NULL ENABLE)
ALTER TABLE "USERS" ADD PRIMARY KEY ("ID") USING INDEX  ENABLE
ALTER TABLE "USERS" ADD CONSTRAINT "UK_6DOTKOTT2KJSP8VW4D0M25FB7" UNIQUE ("EMAIL") USING INDEX  ENABLE
ALTER TABLE "USERS" ADD CONSTRAINT "UK_R43AF9AP4EDM43MMTQ01ODDJ6" UNIQUE ("USERNAME") USING INDEX  ENABLE
--------------------------------------------------------
--  Ref Constraints for Table APPLICATION_INSTANCES
--------------------------------------------------------

ALTER TABLE "APPLICATION_INSTANCES" ADD CONSTRAINT "FKQCWB7F93B7JPOXLECRHE7MBF2" FOREIGN KEY ("APPLICATION_ID") REFERENCES "APPLICATIONS" ("ID") ENABLE
--------------------------------------------------------
--  Ref Constraints for Table BATCH_JOB_EXECUTION
--------------------------------------------------------

ALTER TABLE "BATCH_JOB_EXECUTION" ADD CONSTRAINT "JOB_INST_EXEC_FK" FOREIGN KEY ("JOB_INSTANCE_ID") REFERENCES "BATCH_JOB_INSTANCE" ("JOB_INSTANCE_ID") ENABLE
--------------------------------------------------------
--  Ref Constraints for Table BATCH_JOB_EXECUTION_CONTEXT
--------------------------------------------------------

ALTER TABLE "BATCH_JOB_EXECUTION_CONTEXT" ADD CONSTRAINT "JOB_EXEC_CTX_FK" FOREIGN KEY ("JOB_EXECUTION_ID") REFERENCES "BATCH_JOB_EXECUTION" ("JOB_EXECUTION_ID") ENABLE
--------------------------------------------------------
--  Ref Constraints for Table BATCH_JOB_EXECUTION_PARAMS
--------------------------------------------------------

ALTER TABLE "BATCH_JOB_EXECUTION_PARAMS" ADD CONSTRAINT "JOB_EXEC_PARAMS_FK" FOREIGN KEY ("JOB_EXECUTION_ID") REFERENCES "BATCH_JOB_EXECUTION" ("JOB_EXECUTION_ID") ENABLE
--------------------------------------------------------
--  Ref Constraints for Table BATCH_STEP_EXECUTION
--------------------------------------------------------

ALTER TABLE "BATCH_STEP_EXECUTION" ADD CONSTRAINT "JOB_EXEC_STEP_FK" FOREIGN KEY ("JOB_EXECUTION_ID") REFERENCES "BATCH_JOB_EXECUTION" ("JOB_EXECUTION_ID") ENABLE
--------------------------------------------------------
--  Ref Constraints for Table BATCH_STEP_EXECUTION_CONTEXT
--------------------------------------------------------

ALTER TABLE "BATCH_STEP_EXECUTION_CONTEXT" ADD CONSTRAINT "STEP_EXEC_CTX_FK" FOREIGN KEY ("STEP_EXECUTION_ID") REFERENCES "BATCH_STEP_EXECUTION" ("STEP_EXECUTION_ID") ENABLE
--------------------------------------------------------
--  Ref Constraints for Table BUSINESS_REQUESTS
--------------------------------------------------------

ALTER TABLE "BUSINESS_REQUESTS" ADD CONSTRAINT "FKIYSNK8GGHSI3LQ8IDB42HW90W" FOREIGN KEY ("FUNCTION_REQUESTS_ID") REFERENCES "FUNCTION_REQUESTS" ("ID") ENABLE
ALTER TABLE "BUSINESS_REQUESTS" ADD CONSTRAINT "FKRCMC8BBTO7OQQT53SC6RYKESJ" FOREIGN KEY ("HTTP_REQUESTS_ID") REFERENCES "HTTP_REQUESTS" ("ID") ENABLE
--------------------------------------------------------
--  Ref Constraints for Table FUNCTION_REQUESTS
--------------------------------------------------------

ALTER TABLE "FUNCTION_REQUESTS" ADD CONSTRAINT "FKNLD86FXPDOICED80TKPSK7JL2" FOREIGN KEY ("EXCEPTION_FUNCTION_REQUESTS_ID") REFERENCES "FUNCTION_REQUESTS" ("ID") ENABLE
ALTER TABLE "FUNCTION_REQUESTS" ADD CONSTRAINT "FK7936OMC22WT5GMYD7H5CORMCQ" FOREIGN KEY ("HTTP_REQUESTS_ID") REFERENCES "HTTP_REQUESTS" ("ID") ENABLE
ALTER TABLE "FUNCTION_REQUESTS" ADD CONSTRAINT "FKA98IW6R5KDO3FXX3EJ6TMA4IJ" FOREIGN KEY ("PARENT_FUNCTION_REQUESTS_ID") REFERENCES "FUNCTION_REQUESTS" ("ID") ENABLE
--------------------------------------------------------
--  Ref Constraints for Table FUNCTION_REQUESTS_ARGUMENTS
--------------------------------------------------------

ALTER TABLE "FUNCTION_REQUESTS_ARGUMENTS" ADD CONSTRAINT "FKSGT0QGR5QBLTICBBG3PAQRIGB" FOREIGN KEY ("FUNCTION_REQUESTS_ID") REFERENCES "FUNCTION_REQUESTS" ("ID") ENABLE
--------------------------------------------------------
--  Ref Constraints for Table HTTP_REQUESTS
--------------------------------------------------------

ALTER TABLE "HTTP_REQUESTS" ADD CONSTRAINT "FKIMGQMMF66THXOE97OWAOMRLB1" FOREIGN KEY ("APPLICATION_INSTANCES_ID") REFERENCES "APPLICATION_INSTANCES" ("ID") ENABLE
ALTER TABLE "HTTP_REQUESTS" ADD CONSTRAINT "FKIHSDGC5C5AULE3APPA231FJP0" FOREIGN KEY ("HTTP_REQUESTS_ID") REFERENCES "HTTP_REQUESTS" ("ID") ENABLE
--------------------------------------------------------
--  Ref Constraints for Table HTTP_REQUESTS_HEADERS
--------------------------------------------------------

ALTER TABLE "HTTP_REQUESTS_HEADERS" ADD CONSTRAINT "FK8M9YKG2TY2DVEPLEQBXBB3KXJ" FOREIGN KEY ("HTTP_HEADERS_ID") REFERENCES "HTTP_REQUESTS" ("ID") ENABLE
--------------------------------------------------------
--  Ref Constraints for Table HTTP_REQUESTS_PARAMETERS
--------------------------------------------------------

ALTER TABLE "HTTP_REQUESTS_PARAMETERS" ADD CONSTRAINT "FK98HWWGRIOK8IBOECM0K2AFDGA" FOREIGN KEY ("HTTP_REQUESTS_ID") REFERENCES "HTTP_REQUESTS" ("ID") ENABLE
--------------------------------------------------------
--  Ref Constraints for Table ORDER_ITEMS
--------------------------------------------------------

ALTER TABLE "ORDER_ITEMS" ADD CONSTRAINT "FKBIOXGBV59VETRXE0EJFUBEP1W" FOREIGN KEY ("ORDER_ID") REFERENCES "ORDERS" ("ID") ENABLE
ALTER TABLE "ORDER_ITEMS" ADD CONSTRAINT "FKOCIMC7DTR037RH4LS4L95NLFI" FOREIGN KEY ("PRODUCT_ID") REFERENCES "PRODUCTS" ("ID") ENABLE
--------------------------------------------------------
--  Ref Constraints for Table ORDERS
--------------------------------------------------------

ALTER TABLE "ORDERS" ADD CONSTRAINT "FK32QL8UBNTJ5UH44PH9659TIIH" FOREIGN KEY ("USER_ID") REFERENCES "USERS" ("ID") ENABLE
--------------------------------------------------------
--  Ref Constraints for Table PRODUCTS
--------------------------------------------------------

ALTER TABLE "PRODUCTS" ADD CONSTRAINT "FKOG2RP4QTHBTT2LFYHFO32LSW9" FOREIGN KEY ("CATEGORY_ID") REFERENCES "CATEGORIES" ("ID") ENABLE
