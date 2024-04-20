package com.biz4solutions.clientinvoice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class InvoicePdfDTO {

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

    private float invoiceTotalAmount;

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

    private String sgst;

    private String cgst;

    private String igst;

    private String grandTotal;

    private List<InvoicePdfItemDTO> invoiceItems;

    private String invoiceNumber;

    private String shipToAddress1;

    private String shipToAddress2;

    private String shipToZip;

    private String billToAddress1;

    private String billToAddress2;

    private String billToZip;

    private String companyName;

    private String companyAddress1;

    private String companyAddress2;

    private String companyCity;

    private String companyState;

    private String companyZip;

    private String companyCountry;

    private String companyPhoneNo;

    private  String companyPanNo;

    private String companyWebsite;

    private String bankName;

    private String branch;

    private String AccountName;

    private String AccountNoForCredit;

    private String AccountNoForBeneficiary;

    private CompanyDTO companyDTO;

    private String address1;

    private String address2;

    private String zip;

    private String panNo;

    private String poNo;

    private String gstin;

    private String clientGstin;

    private String ifsc;

    private String total;

    private String ein;

    private String shipToStateName;

    private String billToStateName;

    private String payToSwiftCode;

    private String creditToSwiftCode;

    private String wireRoutingNumber;

    private String achRoutingNumber;

    private String AccountNo;

    private String amountInWords;

    private String payToBankName;

    private String masterDataSgst;

    private String masterDataCgst;

    private String shipToCityName;

    private String billToCityName;

    private String billToCountryName;

    private String shipToCountryName;

    private Boolean hoursPresent= false;

    private Boolean ratePresent= false;

    private Boolean quantityPresent = false;

    private String sgstValue;

    private String cgstValue;

    private String igstValue;

    private Boolean readyToBilledToClient = false;

    public InvoicePdfDTO() {
    }

}
