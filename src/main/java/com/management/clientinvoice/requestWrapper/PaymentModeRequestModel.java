package com.management.clientinvoice.requestWrapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentModeRequestModel {

    private Long id;

    private String dataType;

    private String value;

}
