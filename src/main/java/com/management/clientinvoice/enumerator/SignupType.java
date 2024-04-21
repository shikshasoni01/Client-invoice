package com.management.clientinvoice.enumerator;


public enum SignupType {

    APP("APP"),		   // for sign-up within App
    WEB("WEB"),		   // for sign-up from WEB Console
    ;

    private final String signupType;

    private SignupType(String signupType) {
        this.signupType = signupType;
    }

    public String getSignupType() {
        return signupType;
    }

    /**
     * Use this method instead of valueOf.
     * @param strEnum Enum String
     * @return the corresponding enum to the String
     */
    public static SignupType getEnum(String strEnum) {
        return SignupType.valueOf(strEnum.toUpperCase().replaceAll(" ", "_"));
    }

}

