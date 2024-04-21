package com.management.clientinvoice.enumerator;

public enum InvoiceStatus {

    PENDING("PENDING"),
    PARTIALLYPAID("PARTIALLYPAID"),
    BILLEDTOCLIENT("BILLEDTOCLIENT"),
    FULLYPAID("FULLYPAID");


    private final String invoiceStatus;

    InvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public static InvoiceStatus getEnum(String strEnum) {
        return InvoiceStatus.valueOf(strEnum.toUpperCase().replaceAll(" ", "_"));
    }

}
