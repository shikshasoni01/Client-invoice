package com.management.clientinvoice.domain;


import com.management.clientinvoice.constant.DBConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Date;


@Getter
@Setter
@Entity
@Table(name="invoice")
@Where(clause="is_active=true")
@ToString
public class Invoice extends BaseEntity {

    private static final long serialVersionUID = -3942304475746946038L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "invoice_summary", length = DBConstants.INVOICE_SUMMARY_MAX_LIMIT)
    private String invoiceSummary;

    @Column(name = "invoice_description", length = DBConstants.INVOICE_DESCRIPTION_MAX_LIMIT)
    private String invoiceDescription;

    @Column(name = "invoice_start_date")
    private Date invoiceStartDate;

    @Column(name = "invoice_end_date")
    private Date invoiceEndDate;

    @Column(name = "invoice_date")
    private Date invoiceDate;

    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "paid_date")
    private Date paidDate;

    @Column(name = "updated_on",columnDefinition = "DATE DEFAULT CURRENT_DATE")
    private Date updatedOn;

    @Column(name = "shipping_method",length = DBConstants.SHIPPING_MAX_CHAR_LIMIT)
    private String shippingMethod;

    @Column(name = "shipping_term",length = DBConstants.SHIPPING_MAX_CHAR_LIMIT)
    private String shippingTerm;

    @Column(name = "Note", length = DBConstants.NOTE_TYPE_MAX_LIMIT)
    private String note;

    @Column(name = "invoice_amount")
    private float invoiceTotalAmount;

    @ManyToOne
    private UserIdentity updateBy;

    @ManyToOne
    private Projects project;

    @ManyToOne
    private Country country;

    @ManyToOne
    private PaymentMode paymentMode;

    @ManyToOne
    private PaymentTerm paymentTerm;

    @ManyToOne
    private PaymentStatus paymentStatus;

    private Boolean isActive;

    private Double cgst;

    private Double sgst;

    private Double grandTotal;

    @Column(name = "invoice_number")
    private String invoiceNumber;


}