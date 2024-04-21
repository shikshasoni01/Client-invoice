package com.management.clientinvoice.dto;



import com.management.clientinvoice.enumerator.RoleType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsDTO {

    private Long userId;

    private String email = "";

    private String firstName = "";

    private String lastName = "";

    private String contactNo = "";

    private String countryCode = "";

    private RoleType roleType;

}

