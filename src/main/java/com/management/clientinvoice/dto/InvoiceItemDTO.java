package com.biz4solutions.clientinvoice.dto;

import com.biz4solutions.clientinvoice.domain.Invoice;
import com.biz4solutions.clientinvoice.domain.InvoiceItem;
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
