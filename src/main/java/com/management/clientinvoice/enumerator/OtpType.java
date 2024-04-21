package com.management.clientinvoice.enumerator;


public enum OtpType {

    SIGNUP("Sign up"),
    FORGOTPASSWORD("Forgot Password");

    private final String otpType;

    OtpType(String otpType) {
        this.otpType = otpType;
    }

    /**
     * Use this method instead of valueOf.
     * @param strEnum Enum String
     * @return the corresponding enum to the String
     */
    public static OtpType getEnum(String strEnum) {
        return OtpType.valueOf(strEnum.toUpperCase().replaceAll(" ", "_"));
    }

    public String getOtpType() {
        return otpType;
    }

}

