package com.biz4solutions.clientinvoice.requestWrapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentModeRequestModel {

    private Long id;

    private String dataType;

    private String value;

}
