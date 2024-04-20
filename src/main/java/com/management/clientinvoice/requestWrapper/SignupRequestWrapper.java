package com.biz4solutions.clientinvoice.requestWrapper;


public class SignupRequestWrapper extends SignupBaseRequestWrapper {


    String userName;

    //@Pattern(regexp = "^\\+{0,1}[0-9]{3,20}$", message="pleaseEnterValidContactNumber")
    String contactNo;

    String fcmDeviceToken;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getFcmDeviceToken() {
        return fcmDeviceToken;
    }

    public void setFcmDeviceToken(String fcmDeviceToken) {
        this.fcmDeviceToken = fcmDeviceToken;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SignupRequestWrapper [email=" + email + ", userName=" + userName + ", contactNo=" + contactNo
                + ", fcmDeviceToken=" + fcmDeviceToken + "]";
    }
}

