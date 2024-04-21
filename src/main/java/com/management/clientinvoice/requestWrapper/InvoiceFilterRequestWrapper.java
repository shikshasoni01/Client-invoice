package com.management.clientinvoice.requestWrapper;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;


@Getter
@Setter
public class InvoiceFilterRequestWrapper {


    private String clientFirstName;

    private String clientLastName;

    private String invoiceNumber;

    private String projectName;

    private Date dueDateFrom;

    private Date dueDateTo;

    private Date paidDateFrom;

    private Date paidDateTo;

    private Date invoiceDateFrom;

    private Date invoiceDateTo;

    private Boolean isActive;

    private Long paymentStatus;


}
