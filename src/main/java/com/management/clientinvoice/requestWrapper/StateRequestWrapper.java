package com.biz4solutions.clientinvoice.requestWrapper;

import com.biz4solutions.clientinvoice.constant.DBConstants;
import com.biz4solutions.clientinvoice.domain.Country;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class StateRequestWrapper {


    @NotNull(message = "requiredFieldIsMissing")
    @NotBlank(message = "requiredFieldIsMissing")
    @Length(message = "stateNameSizeErrorMessage")
    @Pattern(regexp = "^[A-Za-z]{1,80}(( ?)[A-Za-z]{1,80}){0,}$",message = "stateNameErrorMessage")
    private String stateName;


    @NotNull(message = "requiredFieldIsMissing")
    @NotBlank(message = "requiredFieldIsMissing")
    private Long countryId;
}
