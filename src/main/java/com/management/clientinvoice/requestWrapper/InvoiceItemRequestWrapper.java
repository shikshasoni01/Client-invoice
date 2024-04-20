package com.biz4solutions.clientinvoice.requestWrapper;

import com.biz4solutions.clientinvoice.constant.DBConstants;
import com.biz4solutions.clientinvoice.domain.Invoice;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;


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
