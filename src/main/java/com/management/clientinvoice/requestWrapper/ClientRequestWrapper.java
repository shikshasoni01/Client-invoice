package com.management.clientinvoice.requestWrapper;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Date;



@Getter
@Setter
public class ClientRequestWrapper {

        @Pattern(regexp = "^([A-Za-z]{1,50}$)",message = "firstNameErrorMessage")
        @NotNull(message = "requiredFieldIsMissing")
        @NotBlank(message = "requiredFieldIsMissing")
        @Length(message ="firstNameSizeErrorMessage" )
        private String firstName;

//        @Pattern(regexp = "^[A-Za-z]{1,50}$|",message = "lastNameErrorMessage")
//        @NotNull(message = "requiredFieldIsMissing")
//        @NotBlank(message = "requiredFieldIsMissing")
//        @Length(message ="lastNameSizeErrorMessage" )
        private String lastName;

//        @Pattern(regexp = "^[A-Za-z]{1,50}$|",message = "contactFirstNameErrorMessage")
//        @Length(message ="contactFirstNameSizeErrorMessage" )
        private String contactPersonFirstName;

//        @Pattern(regexp = "^[A-Za-z]{1,50}$|",message = "contactLastNameErrorMessage")
//        @Length(message ="contactLastNameErrorMessage" )
        private String contactPersonLastName;

//        @Pattern(regexp = "^[A-Za-z]{1,50}$|",message = "alternateNameErrorMessage")
//        @Length(message ="alternateNameSizeErrorMessage" )
        private String alternateName;

//        @Pattern(regexp = "([0-9]{4}(-)[A-Z]{2})",message = "clientIdErrorMessage")
//        @NotNull(message = "requiredFieldIsMissing")
//        @NotBlank(message = "requiredFieldIsMissing")
        private String clientId;

//        @NotNull(message = "requiredFieldIsMissing")
//        @NotBlank(message = "requiredFieldIsMissing")
//        @Length(message ="emailSizeErrorMessage" )
        private String email;

//        @NotNull(message = "requiredFieldIsMissing")
//        @NotBlank(message = "requiredFieldIsMissing")
//        @Length(message ="address SizeErrorMessage" )
        private String billToAddress1;

        @Pattern(regexp = "\"^((https?|ftp|smtp):\\/\\/)?(www.)?[a-z0-9]+\\.[a-z]+(\\/[a-zA-Z0-9#]+\\/?)*$|\"s",message = "websiteErrorMessage")
        private String website;

//        @Pattern(regexp = "^(\\+)[0-9]{1,3}( )[0-9]{10,15}$",message = "phoneNoErrorMessage")
//        @Length(message ="phoneNoErrorMessage" )
        private String contactNo;

//        @Pattern(regexp = "^(\\+)[0-9]{1,3}( )[0-9]{10,15}$",message = "faxErrorMessage")
        @Length(message ="faxErrorMessage" )
        private String faxNumber;

        private Long countryId;

        private Long shipToStateId;

        private Long shipToCityId;

        @NotNull(message = "requiredFieldIsMissing")
        @NotBlank(message = "requiredFieldIsMissing")
        private Long paymentModeId;

//        @Length(message ="addressSizeErrorMessage" )
        private String billToAddress2;

        private Date updatedOn;
//
//        @Length(message ="gstinSizeErrorMessage" )
        private String gstin;

//        @Length(message ="zipCodeSizeErrorMessage" )
        private String billToZip;

        private Long clientTypeId;

        private Boolean isActive;

        private String shipToAddress1;

        private String shipToAddress2;

        private String shipToZip;

        private String panNo;

        private Long billToStateId;

        private Long billToCityId;

}
