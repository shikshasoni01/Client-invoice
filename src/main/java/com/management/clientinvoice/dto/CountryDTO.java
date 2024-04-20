package com.biz4solutions.clientinvoice.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountryDTO {

    private Long id;

    private String countryName;

    private String countryCode;

    private String currencyName;

    private String currencyCode;

  }
