package com.biz4solutions.clientinvoice.requestWrapper;



import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
public class UpdateUserProfileRequestWrapper {

    private String userId;

    @Email(message = "invalidEmail")
    String email;

    @Length(message = "firstNameSizeError")
    @NotNull(message = "firstNameShouldNotEmpty")
    String firstName;

    @Length(message = "lastNameSizeError")
    @NotNull(message = "lastNameShouldnotEmpty")
    String lastName;

    @Length(message = "addressSizeErrorMessage")
    String	addressLine1;

    @Length(message = "addressSizeErrorMessage")
    String	addressLine2;

    String	city;

    String	state;

    String	country;

    @Length(message = "zipCodeSizeErrorMessage")
    String	zip;

    @Length(message = "phoneNoErrorMessage")
    String phoneNumber;

    String countryCode;

    Date dob;

    String gender;

    private String imageUrl;

    private Boolean isProfileUpdated;

    @Length(message = "employeeIDSizeErrorMessage")
    private String employeeId;

    @Length(message = "departmentSizeErrorMessage")
    private String department;
}


