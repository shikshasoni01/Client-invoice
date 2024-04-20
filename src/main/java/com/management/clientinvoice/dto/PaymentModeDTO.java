package com.biz4solutions.clientinvoice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PaymentModeDTO {

    private Long id;

    private String dataType;

    private String value;



}
