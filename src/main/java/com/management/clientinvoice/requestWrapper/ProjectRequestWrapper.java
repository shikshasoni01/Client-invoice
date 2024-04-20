package com.biz4solutions.clientinvoice.requestWrapper;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Date;

@Getter
@Setter
public class ProjectRequestWrapper {

    @NotNull(message = "requiredFieldIsMissing")
    @NotBlank(message = "requiredFieldIsMissing")
    private Long clientId;

    @NotNull(message = "requiredFieldIsMissing")
    @NotBlank(message = "requiredFieldIsMissing")
    @Length(message = "projectNameSizeErrorMessage")
    @Pattern( regexp = "^[A-Za-z]{1,80}(( ?)[A-Za-z]{1,80}){0,}$",message = "projectNameErrorMessage")
    private String projectName;

    @Pattern (regexp = "^[A-Za-z]{1,255}(( ?)[A-Za-z]{1,255}){0,}$|",message = "projectDescriptionErrorMessage")
    @Length(message = "projectDescriptionErrorMessage")
    private String description;

    @NotBlank(message = "requiredFieldIsMissing")
    private Date startDate;

    @Pattern ( regexp = "^[A-Za-z]{1,80}$",message = "projectManagerErrorMessage")
    @NotNull(message = "requiredFieldIsMissing")
    @NotBlank(message = "requiredFieldIsMissing")
    private Long projectManagerId;

    @NotBlank(message = "requiredFieldIsMissing")
    private Long projectLocationId;


    private String poNo;

    private Boolean  isActive;

}
