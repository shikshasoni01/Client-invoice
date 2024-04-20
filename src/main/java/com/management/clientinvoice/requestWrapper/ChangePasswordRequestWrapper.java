package com.biz4solutions.clientinvoice.requestWrapper;


import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ChangePasswordRequestWrapper {

    @NotNull(message = "oldPasswordShouldNotEmpty")
    @NotBlank(message = "oldPasswordShouldNotEmpty")
    String oldPassword;

    @NotNull(message = "newPasswordShouldNotEmpty")
    @NotBlank(message = "newPasswordShouldNotEmpty")
    String newPassword;

    /**
     * @return the oldPassword
     */
    public String getOldPassword() {
        return oldPassword;
    }

    /**
     * @param oldPassword the oldPassword to set
     */
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    /**
     * @return the newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * @param newPassword the newPassword to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ChangePasswordRequestWrapper [oldPassword=" + oldPassword + ", newPassword=" + newPassword + "]";
    }


}

