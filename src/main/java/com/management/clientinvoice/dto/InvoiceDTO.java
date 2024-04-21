package com.management.clientinvoice.dto;


import lombok.Getter;
import lombok.Setter;
import java.sql.Date;
import java.util.*;


@Getter
@Setter
public class InvoiceDTO extends  InvoiceItemDTO{

    private static final long serialVersionUID = 1L;

    private Long id;

    private  Long ClientId;

    private String ClientFirstName;

    private String ClientLastName;

    private String invoiceSummary;

    private String invoiceDescription;

    private Date invoiceStartDate;

    private Date invoiceEndDate;

    private String paymentMode;

    private Long paymentModeId;

    private Date invoiceDate;

    private String invoiceTotalAmount;

    private Long projectId;

    private String projectName;

    private String paymentTerm;

    private Long paymentTermId;

    private Date dueDate;

    private Date paidDate;

    private Boolean isActive;

    private Long countryId;

    private String countryName;

    private String currencyName;

    private String currencyCode;

    private Date updatedOn;

    private String updatedBy;

    private String note;

    private Long paymentStatusId;

    private String paymentStatus;

    private String shippingTerm;

    private String shippingMethod;

    private Double sgst;

    private Double cgst;

    private String grandTotal;

    private List<InvoiceItemDTO> invoiceItems;

    private String invoiceNumber;

    private String refDraftInvoiceNumber;

    private boolean readyToBilledToClient= false;


    public InvoiceDTO() {
    }


}