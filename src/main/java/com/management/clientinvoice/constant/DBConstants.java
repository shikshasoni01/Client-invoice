package com.biz4solutions.clientinvoice.constant;

public class DBConstants {

    public static final Boolean STATE_ACTIVE = Boolean.TRUE;

    public static final String EMAIL_VERIFICATION_SUBJECT="Please Verify Your Email for client";
    public static final long EMAIL_EXPITY_TIME = 24*60*60*1000;

    public static final int VALUE_TEXT_MAX_CHAR_LIMIT = 10000;
    public static final int ENUM_DEFAULT_MAX_CHAR_LIMIT = 25;
    public static final int VARCHAR_DEFAULT_MAX_CHAR_LIMIT = 256;

    public static final int FULL_NAME_MAX_CHAR_LIMIT = 255;

    public static final int NAME_MAX_CHAR_LIMIT = 80;

    public static final int SHIPPING_MAX_CHAR_LIMIT = 80;

    public static final int GSTIN_MAX_CHAR_LIMIT = 15;
    public static final int GSTIN_MIN_CHAR_LIMIT = 0;


    public static final int VARCHAR_JSON_STRING_MAX_CHAR_LIMIT = 512;
    public static final int CONTACT_NO_MAX_CHAR_LIMIT = 15;


    public static final int FAX_NO_MAX_CHAR_LIMIT = 15;

    public static final int CONTACT_NO_MIN_CHAR_LIMIT = 10;

    public static final int FAX_NO_MIN_CHAR_LIMIT = 10;
    public static final int EMAIL_MAX_CHAR_LIMIT = 255;
    public static final int DESCRIPTION_TEXT_MAX_CHAR_LIMIT = 1000;

    public static final int CONTRACT_TEXT_MAX_CHAR_LIMIT = 5000;
    public static final int ADDRESS_MAX_CHAR_LIMIT = 255;
    public static final int DEVICE_ID_MAX_CHAR_LIMIT = 50;

    public static final int CLIENT_ID_MAX_CHAR_LIMIT = 7;
    public static final int DEVICE_TYPE_MAX_LIMIT = 20;
    public static final int SAC_CODE_TYPE_MAX_LIMIT=80;
    public static final int CURRENCY_MAX_LIMIT =80;

    public static final int CURRENCY_CODE_MAX_LIMIT =3;
    public static final int NOTE_TYPE_MAX_LIMIT=255;

    public static final int CITY_MAX_CHAR_LIMIT = 80;

    public static final int STATE_MAX_CHAR_LIMIT = 80;
    public static final int COUNTRY_MAX_CHAR_LIMIT = 80;

    public static final int COUNTRY_MIN_CHAR_LIMIT = 1;

    public static final int COUNTRY_CODE_MAX_CHAR_LIMIT = 4;

    public static final int ZIPCODE_MAX_CHAR_LIMIT = 10;
    public static final int ZIPCODE_MIN_CHAR_LIMIT = 5;

    public static final int URL_MAX_CHAR_LIMIT = 2048;

    public static final int THUMB_URL_MAX_CHAR_LIMIT = URL_MAX_CHAR_LIMIT + 6;
    public static final Boolean STATE_INACTIVE = Boolean.FALSE;

    public static final int ABOUT_ME_MAX_CHAR_LENGTH = 2048;
    public static final int PROJECT_DESCRIPTION_MAX_LIMIT=1000;
    public static final int INVOICE_DESCRIPTION_MAX_LIMIT=1000;
    public static final int INVOICE_SUMMARY_MAX_LIMIT=1000;
    public static final int PROJECT_NAME_MAX_LIMIT = 80;

}
