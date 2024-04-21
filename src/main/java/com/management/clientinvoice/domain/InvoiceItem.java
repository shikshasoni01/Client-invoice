package com.management.clientinvoice.domain;


import com.management.clientinvoice.constant.DBConstants;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;


@Getter
@Setter
@Entity
@Table(name="invoiceItem")
@Where(clause="is_active=true")
public class InvoiceItem extends  BaseEntity{

    private static final long serialVersionUID = -3942304475746946038L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @OnDelete ( action = OnDeleteAction.CASCADE)
    @Column(name = "id")
    private Long id;

    @Column(name = "item_number")
    private  Long itemNumber;

    @Column(name = "quantity")
    private String quantity;

    @Column(name="item_description",length = DBConstants.INVOICE_DESCRIPTION_MAX_LIMIT)
    private String itemDescription;

    @Column(name = "hourly_rate")
    private Double hourlyRate;

    @Column(name = "hours_billed")
    private Double hours;

    @Column(name = "item_amount")
    private Double itemAmount;

    @Column(name ="SACCode", length = DBConstants.SAC_CODE_TYPE_MAX_LIMIT)
    private String sacCode;

    private Boolean isActive;

    @ManyToOne( fetch = FetchType.LAZY)
    private Invoice invoice;



}
