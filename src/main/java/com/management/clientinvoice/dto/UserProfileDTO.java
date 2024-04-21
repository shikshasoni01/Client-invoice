package com.management.clientinvoice.dto;


public class UserProfileDTO {

    private String userId;

    private String signupType;

    private String email;

    private String userName;

    private String contactNo;

    private Boolean isEmailVerified;

    private Boolean isContactNoVerified;

    private String lastLoginDeviceType;

    private String lastLoginDeviceId;

    private String role;

    private String firstName;

    private String lastName;

    private String token;

    private UserProfileDTO userProfile;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public UserProfileDTO getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfileDTO userProfile) {
        this.userProfile = userProfile;
    }

    public UserProfileDTO(UserProfileDTO profile) {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSignupType() {
        return signupType;
    }

    public void setSignupType(String signupType) {
        this.signupType = signupType;
    }

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

    public Boolean getIsEmailVerified() {
        return isEmailVerified;
    }

    public void setIsEmailVerified(Boolean isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
    }

    public Boolean getIsContactNoVerified() {
        return isContactNoVerified;
    }

    public void setIsContactNoVerified(Boolean isContactNoVerified) {
        this.isContactNoVerified = isContactNoVerified;
    }

    public String getLastLoginDeviceType() {
        return lastLoginDeviceType;
    }

    public void setLastLoginDeviceType(String lastLoginDeviceType) {
        this.lastLoginDeviceType = lastLoginDeviceType;
    }

    public String getLastLoginDeviceId() {
        return lastLoginDeviceId;
    }

    public void setLastLoginDeviceId(String lastLoginDeviceId) {
        this.lastLoginDeviceId = lastLoginDeviceId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "UserProfileDTO [signupType=" + signupType + ", email=" + email + ", userName=" + userName
                + ", contactNo=" + contactNo + ", isEmailVerified=" + isEmailVerified + ", isContactNoVerified="
                + isContactNoVerified + ", lastLoginDeviceType=" + lastLoginDeviceType + ", lastLoginDeviceId="
                + lastLoginDeviceId + ", role=" + role + ", firstName=" + firstName + ", lastName=" + lastName
                + ", token=" + token + "]";
    }


}
