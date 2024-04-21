package com.management.clientinvoice.domain;


import com.management.clientinvoice.constant.DBConstants;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "client")
@Where(clause = "is_active = true")
public class Client extends BaseEntity  {

    private static final long serialVersionUID = 128253945703119607L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @NotNull(message = "firstNameShouldNotEmpty")
    @Column(name = "First_name" , length = DBConstants.NAME_MAX_CHAR_LIMIT)
    @Size(min = 1)
    private String firstName;

    @Column(name = "Last_name",length = DBConstants.NAME_MAX_CHAR_LIMIT)
    private String lastName;

    @Column(name = "Contact_person_first_name",length = DBConstants.NAME_MAX_CHAR_LIMIT)
    private String contactPersonFirstName;

    @Column(name = "contact_person_last_name",length = DBConstants.NAME_MAX_CHAR_LIMIT)
    private String contactPersonLastName;

    @Column(name = "alternate_name",length = DBConstants.NAME_MAX_CHAR_LIMIT)
    private String alternateName;


   // @Column(name = "gstin",length = DBConstants.GSTIN_MAX_CHAR_LIMIT,unique = AppConfigConstants.IS_CLIENT_ID_UNIQUE)
    @Column(name ="gstin")
    private String gstin;

    //message "Client ID must contain 4-digits and two alphabetic characters"
//    @NotBlank
//    @Pattern(regexp = "([0-9]{4}(-)[A-Z]{2})")
  //  @Column(name = "client_id",unique = AppConfigConstants.IS_CLIENT_ID_UNIQUE,length = DBConstants.CLIENT_ID_MAX_CHAR_LIMIT)
   @Column(name = "client_id")
    private String clientId;

//    @NotBlank
//    @NotNull(message = "emailShouldNotEmpty")
//    @Email
//    @Column(name = "email", length = DBConstants.EMAIL_MAX_CHAR_LIMIT, unique = AppConfigConstants.IS_EMAIL_UNIQUE)
   @Column(name = "email")
    private String email;


    @Column(name = "address1",length = DBConstants.ADDRESS_MAX_CHAR_LIMIT)
    private String billToAddress1;

    @Column(name = "address2",length = DBConstants.ADDRESS_MAX_CHAR_LIMIT)
    private String billToAddress2;

    @Column(name = "zip")
//    @Size(min=DBConstants.ZIPCODE_MIN_CHAR_LIMIT, max = DBConstants.ZIPCODE_MAX_CHAR_LIMIT)
    private String billToZip;

    @Pattern(regexp = "^((https?|ftp|smtp):\\/\\/)?(www.)?[a-z0-9]+\\.[a-z]+(\\/[a-zA-Z0-9#]+\\/?)*$|")
    @Column(name = "website")
    private String website;

//    @Column(name = "contact_no",unique = AppConfigConstants.IS_MOBILE_UNIQUE)
//    @Size(min = DBConstants.CONTACT_NO_MIN_CHAR_LIMIT,max = DBConstants.CONTACT_NO_MAX_CHAR_LIMIT)
    @Column(name = "contact_no")
    private String contactNo;

    @Column(name = "Fax_number")
//    @Size(min =DBConstants.FAX_NO_MIN_CHAR_LIMIT ,max = DBConstants.FAX_NO_MAX_CHAR_LIMIT)
    private String faxNumber;

    @Column(name = "Updated_on",columnDefinition = "DATE DEFAULT CURRENT_DATE")
    private Date updatedOn;

    @ManyToOne
    private PaymentMode paymentMode;

    @ManyToOne
    private Country country;

    @ManyToOne
    private ClientType clientType;

    @ManyToOne
    private State shipToState;

    @ManyToOne
    private City shipToCity;

    @ManyToOne
    private State billToState;

    @ManyToOne
    private City billToCity;

    @Column(name = "ship_to_address1")
    private String shipToAddress1;

    @Column(name = "ship_to_address2")
    private String shipToAddress2;

    @Column(name = "ship_to_zip")
//    @Size(min=DBConstants.ZIPCODE_MIN_CHAR_LIMIT, max = DBConstants.ZIPCODE_MAX_CHAR_LIMIT)
    private String shipToZip;

    @Column(name = "pan_no")
    private String panNo;


}
