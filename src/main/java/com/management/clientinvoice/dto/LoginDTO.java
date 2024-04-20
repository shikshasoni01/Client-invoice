package com.biz4solutions.clientinvoice.dto;


import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter

public class LoginDTO extends UserProfileDTO implements Serializable {

    private static final long serialVersionUID = -3440716754856380406L;

    private boolean isNewUser = false;

    private String referenceId;

    private String accessToken;

    private String refreshToken;


    public LoginDTO(UserProfileDTO profile) {
        super(profile);
    }

}