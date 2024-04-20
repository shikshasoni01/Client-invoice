package com.biz4solutions.clientinvoice.dto;



import com.biz4solutions.clientinvoice.enumerator.RoleType;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.util.UUID;

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

