package com.management.clientinvoice.requestWrapper;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ClientFilterRequestWrapper {


    private String firstName;

    private String lastName;

    private String contactPersonFirstName;

    private String contactPersonLastName;

    private String email;

    private String countryName;

    private Boolean isActive;


}
