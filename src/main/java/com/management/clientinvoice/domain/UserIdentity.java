package com.management.clientinvoice.domain;

import com.management.clientinvoice.constant.AppConfigConstants;
import com.management.clientinvoice.constant.DBConstants;
import com.management.clientinvoice.enumerator.SignupType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user_identity", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"}),
})
@Where(clause="is_active= true")
public class UserIdentity  extends BaseEntity implements UserDetails, Authentication {

    private static final long serialVersionUID = -5477261893023074274L;

    @Transient
    private boolean authenticated = true;

    /**
     * Password Hash of Normal Sign up User / Auth Token of Fb / Linked In
     */
    @Column(name = "auth_password", length = 1024)
    private String authPassword;

    /**
     * Sign-up type of User
     */
    @Column(name = "signup_type", length = DBConstants.ENUM_DEFAULT_MAX_CHAR_LIMIT, nullable = false)
    @Enumerated(EnumType.STRING)
    private SignupType signupType = SignupType.WEB;


    @Column(length = DBConstants.NAME_MAX_CHAR_LIMIT)
    private String firstName = "";

    @Column(length = DBConstants.NAME_MAX_CHAR_LIMIT)
    private String lastName = "";

    /**
     * Flag to check if the Email is verified of this User
     */
    @Column(name = "is_email_verified", nullable = false)
    private Boolean isEmailVerified = Boolean.FALSE;


    /**
     * Email of User
     */
    @Email
    @Column(name = "email", length = DBConstants.EMAIL_MAX_CHAR_LIMIT, unique = AppConfigConstants.IS_EMAIL_UNIQUE)
    private String email = "";

    @Column(name = "contact_no", length = DBConstants.CONTACT_NO_MAX_CHAR_LIMIT, unique = AppConfigConstants.IS_MOBILE_UNIQUE)
    private String contactNo = "";

    @Column(length =DBConstants.COUNTRY_CODE_MAX_CHAR_LIMIT)
    private String countryCode = "";

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    @Column(name = "last_login_device_token", length = DBConstants.DESCRIPTION_TEXT_MAX_CHAR_LIMIT)
    private String lastLoginDeviceToken;

    @Column(name = "unique_id")
    private String uniqueId;

    @Column(length = DBConstants.NAME_MAX_CHAR_LIMIT)
    private String employeeId;

    @Column(length = DBConstants.NAME_MAX_CHAR_LIMIT)
    private String department;

    private Date dob;

    private Long dobLong;

    private String gender = "";

    private Boolean isBlocked = Boolean.FALSE;

    private Boolean isNotificationOn = Boolean.TRUE;

    private String fbId;

    private String fbToken;

    private String fbPWDToken;

    private String appleId;

    private String appleToken;

    private String applePWDToken;

    private Boolean isReported = Boolean.FALSE;//is reported by anyone


    /**
     * Device type: android, iphone, ipad, ipod
     */
    @Column(name = "last_login_device_type", length = DBConstants.DEVICE_TYPE_MAX_LIMIT)
    private String lastLoginDeviceType;

    /**
     * Device Id of lastLoginDeviceType
     */
    @Column(name = "last_login_device_id", length = DBConstants.DEVICE_ID_MAX_CHAR_LIMIT)
    private String lastLoginDeviceId;

    /**
     * Role of this User
     */
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "userIdentities")
    private List<LoginHistory> loginHistory;

    @Fetch(FetchMode.JOIN)
    @JoinColumn(unique = true)
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private UserProfile userProfile;

    /**
     * @return the loginHistory
     */
    public List<LoginHistory> getLoginHistory() {
        return loginHistory;
    }

    /**
     * @param loginHistory the loginHistory to set
     */
    public void setLoginHistory(List<LoginHistory> loginHistory) {
        this.loginHistory = loginHistory;
    }

    /**
     * @return the userProfile
     */
    public UserProfile getUserProfile() {
        return userProfile;
    }

    /**
     * @param userProfile the userProfile to set
     */
    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public String getAuthPassword() {
        return authPassword;
    }

    public void setAuthPassword(String authPassword) {
        this.authPassword = authPassword;
    }

    public SignupType getSignupType() {
        return signupType;
    }

    public void setSignupType(SignupType signupType) {
        this.signupType = signupType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(email != null &&email.equals(""))
            email = null;
        this.email = email;
    }

    public Boolean getIsEmailVerified() {
        return isEmailVerified;
    }

    public void setIsEmailVerified(Boolean isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + this.role.getRoleType().getRoleType());
        return Arrays.asList(authority);
    }

    @Override
    public String getPassword() {
        return this.authPassword;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public String getLastLoginDeviceToken() {
        return lastLoginDeviceToken;
    }

    public void setLastLoginDeviceToken(String lastLoginDeviceToken) {
        this.lastLoginDeviceToken = lastLoginDeviceToken;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @PrePersist
    public void encrypt() {
        if (null != this.authPassword) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            this.authPassword = passwordEncoder.encode(this.authPassword);
//            		SCryptPasswordEncoder cryptPasswordEncoder = new SCryptPasswordEncoder();
//            		this.authPassword = cryptPasswordEncoder.encode(this.authPassword);
        }
    }

}

