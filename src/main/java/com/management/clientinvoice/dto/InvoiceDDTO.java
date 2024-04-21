package com.management.clientinvoice.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class InvoiceDDTO {

    private Long id;

    private String projectName;

    private String masterDataId;

    private String invoiceSummary;

    private String description;

    private Date invoiceStartDate;

    private Date invoiceEndDate;

    private Date invoiceDate;

    private Date paidDate;

    private Date dueDate;

    private String countryName;

    private Double hourlyRate;

    private Double hoursBilled;

    private Double invoiceAmount;

    private String updatedBy;

    private Date updatedOn;

    private String paymentStatus;

    private String sacCode;

    private String clientName;

    private String contactPerson;

    private String note;

    private String paymentTerm;


}
