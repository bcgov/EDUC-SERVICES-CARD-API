
CREATE TABLE SERVICES_CARD_INFO
(
    SERVICES_CARD_INFO_ID RAW(16) NOT NULL,
    DIGITAL_IDENTITY_ID RAW(16) NOT NULL,
    DID VARCHAR2(255) NOT NULL,
    USER_DISPLAY_NAME VARCHAR2(255),
    GIVEN_NAME VARCHAR2(255),
    GIVEN_NAMES VARCHAR2(255),
    SURNAME VARCHAR2(255) NOT NULL,
    BIRTHDATE DATE NOT NULL,
    GENDER VARCHAR2(7) NOT NULL,
    EMAIL VARCHAR2(255),
    STREET_ADDRESS VARCHAR2(1000) NOT NULL,
    CITY VARCHAR2(255) NOT NULL,
    PROVINCE VARCHAR2(255) NOT NULL,
    COUNTRY VARCHAR2(255) NOT NULL,
    POSTAL_CODE VARCHAR2(7) NOT NULL,
    IDENTITY_ASSURANCE_LEVEL VARCHAR2(1) NOT NULL,
    CREATE_USER VARCHAR2(32) NOT NULL,
    CREATE_DATE DATE DEFAULT SYSDATE NOT NULL,
    UPDATE_USER VARCHAR2(32) NOT NULL,
    UPDATE_DATE DATE DEFAULT SYSDATE NOT NULL,
    CONSTRAINT SERVICES_CARD_IDENTITY_PK PRIMARY KEY (SERVICES_CARD_INFO_ID)
);

CREATE INDEX SERVICES_CARD_INFO_DID_I ON SERVICES_CARD_INFO ( DID );

ALTER INDEX API_SERVICES_CARD.SERVICES_CARD_IDENTITY_PK REBUILD LOGGING NOREVERSE TABLESPACE API_SERVICES_CARD_IDX NOCOMPRESS;
ALTER INDEX API_SERVICES_CARD.SERVICES_CARD_IDENTITY_PK REBUILD LOGGING NOREVERSE TABLESPACE API_SERVICES_CARD_IDX NOCOMPRESS;