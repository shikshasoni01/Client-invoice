package com.biz4solutions.clientinvoice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyDTO {

    private  Long id;

    private String companyName;

    private String logoUrl;

    private String phoneNumber;

    private String website;

    private String address1;

    private String address2;

    private String city;

    private String state;

    private String zip;

    private String country;

    private String panNumber;

    private String gstin;

    private String ein;

    private String companyType;
}
