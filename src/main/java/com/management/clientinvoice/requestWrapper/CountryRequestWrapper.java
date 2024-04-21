package com.management.clientinvoice.requestWrapper;

import com.management.clientinvoice.constant.DBConstants;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Getter
@Setter
public class CountryRequestWrapper {

    @NotNull(message = "requiredFieldIsMissing")
    @NotBlank(message = "requiredFieldIsMissing")
    @Length(min = DBConstants.COUNTRY_MIN_CHAR_LIMIT,max = DBConstants.COUNTRY_MAX_CHAR_LIMIT,message = "countryNameSizeErrorMessage")
    @Pattern(regexp = "^[A-Za-z]{1,80}(( ?)[A-Za-z]{1,80}){0,}$",message = "countryNameErrorMessage")
    private String countryName;

    @NotNull(message = "requiredFieldIsMissing")
    @NotBlank(message = "requiredFieldIsMissing")
    @Length(message = "countryCodeErrorMessage")
    @Pattern(regexp = "^(\\+)[0-9]{1,3}$",message = "countryCodeErrorMessage")
    private String countryCode;

    @NotNull(message = "requiredFieldIsMissing")
    @NotBlank(message = "requiredFieldIsMissing")
    @Length(max = DBConstants.CURRENCY_MAX_LIMIT,message = "currencyNameErrorMessage")
    @Pattern(regexp = "^[A-Za-z]{1,80}(( ?)[A-Za-z]{1,80}){0,}$",message = "currencyNameErrorMessage")
    private String currencyName;

    @NotNull(message = "requiredFieldIsMissing")
    @NotBlank(message = "requiredFieldIsMissing")
    @Length(message = "currencyCodeErrorMessage")
    @Pattern(regexp = "^[A-Za-z]{1,3}$",message = "currencyCodeErrorMessage")
    private String currencyCode;

}
