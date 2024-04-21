package com.management.clientinvoice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class  InvoiceItemDTO {

    private  Long id;

    private  Long itemNumber;

    private String quantity;

    private String itemDescription;

    private String hourlyRate;

    private String hours;

    private String itemAmount;

    private String sacCode;

    private Boolean isActive;


}
