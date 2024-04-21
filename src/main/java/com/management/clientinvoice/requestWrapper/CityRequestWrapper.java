package com.management.clientinvoice.requestWrapper;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class CityRequestWrapper {

    @NotNull(message = "requiredFieldIsMissing")
    @NotBlank(message = "requiredFieldIsMissing")
    @Pattern(regexp = "^[A-Za-z]{1,250}(( ?)[A-Za-z]{1,250}){0,}$",message = "cityNameErrorMessage")
    @Length(message = "cityNameSizeErrorMessage")
    private String cityName;

    @NotNull(message = "requiredFieldIsMissing")
    @NotBlank(message = "requiredFieldIsMissing")
    private Long stateId;

}
