package com.management.clientinvoice.requestWrapper;



import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CommonLoginRequestWrapper {

    @NotNull(message = "pleaseEnterAllFields")
    String loginIdentifier;

    @NotNull(message = "passwordShouldNotEmpty")
    @NotBlank(message = "passwordShouldNotBlank")
    String password;

    String fcmDeviceToken;


    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "CommonLoginRequestWrapper [loginIdentifier=" + loginIdentifier + ", password=" + password
                + ", fcmDeviceToken=" + fcmDeviceToken + "]";
    }


}

