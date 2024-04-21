package com.management.clientinvoice.requestWrapper;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Date;
import java.util.List;


@Getter
@Setter
@ToString
public class
InvoiceRequestWrapper{


    @Pattern(regexp = "([0-9]{4}(-)[A-Z]{2})",message = "countryIdErrorMessage")
    @NotNull(message = "requiredFieldIsMissing")
    @NotBlank(message = "requiredFieldIsMissing")
    private Long countryId;

    @NotNull(message = "projectNameErrorMessage")
    @NotBlank(message = "projectNameErrorMessage")
    private Long projectId;

    @NotNull(message = "requiredFieldIsMissing")
    @NotBlank(message = "requiredFieldIsMissing")
    private Long paymentTerm;

    @Length(message = "summaryError")
    private String invoiceSummary;

    @Length(message = "descriptionError")
    private String invoiceDescription;

    private Date dueDate;

    private Date PaidDate;

    @NotNull(message = "invoiceStartDateNotEmpty")
    private Date invoiceStartDate;

    private Date invoiceEndDate;

    private Date invoiceDate;

    private float invoiceTotalAmount;

    private Boolean isActive;

    @Length(message = "noteError")
    private String note;

    private Long paymentStatus;

    private Long paymentMode;

    @Length(message = "shippingError")
    private String shippingTerm;

    @Length(message = "shippingError")
    private String shippingMethod;

    private Double sgst;

    private Double cgst;

    private Double grandTotal;

    private List<InvoiceItemRequestWrapper> invoiceItems;

    private String invoiceNumber;

//    Boolean isPreInvoice = Boolean.TRUE;


}
