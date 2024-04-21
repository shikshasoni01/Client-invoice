package com.management.clientinvoice.requestWrapper;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;


@Getter
@Setter
public class InvoiceItemRequestWrapper {


    private  Long itemNumber;

    private String quantity;

    @Length(message = "descriptionError")
    private String itemDescription;

    private Double hourlyRate;

    private Double hours;

    private Double itemAmount;

    @Length(message = "sacCodeError")
    private String sacCode;

    private Boolean isActive;







}
