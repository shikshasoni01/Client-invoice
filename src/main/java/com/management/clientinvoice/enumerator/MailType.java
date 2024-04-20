package com.biz4solutions.clientinvoice.enumerator;


public enum MailType {
    EMAIL_OTP("EMAIL_OTP"),
    EMAIL_PASSWORD("EMAIL_PASSWORD"),
    COMMON_MAIL_TEMPLATE("COMMON_MAIL_TEMPLATE"),

    INVOICE_NUMBER_UPDATE_MAIL_TEMPLATE("INVOICE_NUMBER_UPDATE_MAIL_TEMPLATE"),
    BILLED_TO_CLIENT_TEMPLATE("BILLED_TO_CLIENT_TEMPLATE"),
    FULL_PAYMENT_TEMPLATE("FULL_PAYMENT_TEMPLATE"),
    READY_TO_BILL_TEMPLATE("READY_TO_BILL_TEMPLATE"),
    VOID_TEMPLATE("VOID_TEMPLATE"),
    SIGNUP("SIGNUP");

    private final String mailType;


    MailType(String mailType) {
        this.mailType = mailType;
    }

    /**
     * Use this method instead of valueOf.
     *
     * @param strEnum Enum String
     * @return the corresponding enum to the String
     */
    public static MailType getEnum(String strEnum) {
        return MailType.valueOf(strEnum.toUpperCase().replaceAll(" ", "_"));
    }

    public String getMailType() {
        return mailType;
    }

}
