package com.management.clientinvoice.requestWrapper;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ResetPasswordRequestWrapper {
    @NotNull(message = "newPasswordShouldNotEmpty")
    @NotBlank(message = "newPasswordShouldNotEmpty")
    String newPassword;

    String token;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}

