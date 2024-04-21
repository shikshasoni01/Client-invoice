package com.management.clientinvoice.dto;


import com.management.clientinvoice.domain.Projects;
import com.management.clientinvoice.domain.UserIdentity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;


import java.io.Serializable;

@Getter
@Setter
public class UserDTO implements Serializable {

    private static final long serialVersionUID = -4108842598344893668L;

    private String firstName;
    private String lastName;
    private String userId;
    private String email;

    private Long dobLong;
    private String role;
    private String contactNo;
    private String countryCode;
    private String gender;

    private String city;
    private Boolean isReported;
    private Boolean isBlocked;

    private PaginationDTO reportedByDetails;

    private boolean valid = false;

    private String defaultPicURL;

    private int matchPercentage;

    private String employeeId;

    private String department ;


    @JsonIgnore
    private String bucketName;

    @JsonIgnore
    private String keyString;

    @JsonIgnore
    private String token;


    public UserDTO() {
    }

    public UserDTO(UserIdentity userIdentity) {
        if (userIdentity != null) {

            this.userId = userIdentity.getId().toString();

            if (!StringUtils.isEmpty(userIdentity.getFirstName())) {
                this.firstName = userIdentity.getFirstName();
            }

            if (!StringUtils.isEmpty(userIdentity.getLastName())) {
                this.lastName = userIdentity.getLastName();
            }

            this.contactNo = StringUtils.isEmpty(userIdentity.getContactNo()) ? "" : userIdentity.getContactNo();
            this.countryCode = StringUtils.isEmpty(userIdentity.getCountryCode()) ? "" : userIdentity.getCountryCode();
            this.gender = StringUtils.isEmpty(userIdentity.getGender()) ? "" : userIdentity.getGender();
            this.isBlocked = userIdentity.getIsBlocked();
            this.isReported = userIdentity.getIsReported();
            this.email = StringUtils.isEmpty(userIdentity.getEmail()) ? "" : userIdentity.getEmail();

            if (!StringUtils.isEmpty(userIdentity.getEmail())) {
                this.email = userIdentity.getEmail();
            }

            this.employeeId=userIdentity.getEmployeeId ();
            this.department=userIdentity.getDepartment ();
            this.role = userIdentity.getRole().getRoleType().getRoleType();

        }
    }

    public UserDTO(Projects projects) {
    }
}




