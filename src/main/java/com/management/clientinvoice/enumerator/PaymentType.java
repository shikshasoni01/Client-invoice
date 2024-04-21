package com.management.clientinvoice.enumerator;

public enum PaymentType {

    PENDING("PENDING"),
    BILLED_TO_CLIENT("BILLED_TO_CLIENT"),
    PARTIALLY_PAID(" PARTIALLY_PAID"),
    FULLY_PAID("FULLY_PAID");

    private final String paymentType;

    PaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * Use this method instead of valueOf.
     *
     * @param strEnum Enum String
     * @return the corresponding enum to the String
     */
    public static PaymentType getEnum(String strEnum) {
        return PaymentType.valueOf(strEnum.toUpperCase().replaceAll(" ", "_"));
    }

    public String getPaymentType() {
        return paymentType;
    }

}
