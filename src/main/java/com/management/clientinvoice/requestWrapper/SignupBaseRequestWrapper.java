package com.biz4solutions.clientinvoice.requestWrapper;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignupBaseRequestWrapper {

    @NotNull(message = "requiredFieldIsMissing")
    @NotBlank(message = "passwordShouldNotBlank")
    String authPassword;

    @NotNull(message = "firstNameShouldNotEmpty")
    @NotBlank(message = "firstNameShouldNotEmpty")
    @Pattern(regexp = "^[A-Za-z]{1,80}$",message = "firstNameErrorMessage")
    @Length(message = "firstNameSizeError")
    String firstName;

    @Pattern(regexp = "^[A-Za-z]{1,80}$",message = "lastNameErrorMessage")
    @NotNull(message = "lastNameShouldNotEmpty")
    @NotBlank(message = "lastNameShouldNotEmpty")
    @Length(message = "lastNameSizeError")
    String lastName;

    @NotNull(message = "signupTypeShouldNotEmpty")
    @NotBlank(message = "signupTypeShouldNotEmpty")
    String	signupType;

    @NotNull(message = "contactNoShouldNotEmpty")
    @NotBlank(message = "contactNoShouldNotEmpty")
    @Size(min = 10,max = 15,message = "phoneErrorMessage")
    @Pattern(regexp = "^[0-9]{10,15}$",message = "phoneErrorMessage")
    private String contactNo;

    @Pattern(regexp = "^(\\+)[0-9]{1,3}$",message = "countryCodeErrorMessage")
    @Size(min = 1,max =4,message ="countryCodeErrorMessage" )
    @NotNull(message = "requiredFieldIsMissing")
    @NotBlank(message = "requiredFieldIsMissing")
    private String countryCode;



    @Email(message = "invalidEmail")
    @NotNull(message = "requiredFieldIsMissing")
    @NotBlank(message = "requiredFieldIsMissing")
    String email;

    @NotNull(message = "roleTypeShouldNotEmpty")
    @NotBlank(message = "roleTypeShouldNotEmpty")
    String roleType;

//    @Column(name = "verification_code", length = 64)
//    private String verificationCode;
}

