package com.biz4solutions.clientinvoice.requestWrapper;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RequestOtpRequestWrapper {

    @NotNull(message = "emailShouldNotEmpty")
    private String email;

    private String transactionId;

    private String role;
}

