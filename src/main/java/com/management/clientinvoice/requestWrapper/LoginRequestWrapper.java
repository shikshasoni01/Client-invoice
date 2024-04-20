package com.biz4solutions.clientinvoice.requestWrapper;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;


@Getter
@Setter
public class LoginRequestWrapper {



    @NotNull(message = "enterEmail")
    @Email(message = "invalidEmail")
    String email;

    @NotNull(message = "passwordShouldNotEmpty")
    @NotBlank(message = "passwordShouldNotBlank")
    String password;

    @NotNull(message = "oauth2ClientIdNotNull")
    @javax.validation.constraints.NotBlank(message = "oauth2ClientIdNotBlank")
    private String oauth2ClientId;

}

