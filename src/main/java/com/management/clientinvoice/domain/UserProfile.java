package com.biz4solutions.clientinvoice.domain;


import com.biz4solutions.clientinvoice.constant.DBConstants;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import javax.persistence.*;

@Entity
@Table(name = "user_profile")
@Getter
@Setter
@Where(clause = "is_active= true")
public class UserProfile extends BaseEntity{

    private static final long serialVersionUID = 2815987607149346364L;


    @Column(name = "email", length = DBConstants.EMAIL_MAX_CHAR_LIMIT)
    private String email;

    @Column(length = DBConstants.NAME_MAX_CHAR_LIMIT)
    private String firstName;

    @Column(length = DBConstants.NAME_MAX_CHAR_LIMIT)
    private String lastName;

    private String setPasswordToken;

    private Integer failedLoginAttempts;

    private String city = "";

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    private String maritalStatus = "";

    private String defaultPicURL = "";

    @OneToOne//(mappedBy = "userProfile")
    private UserIdentity userIdentity;

    @OneToOne
    @JoinColumn(name = "default_pic_id")
    private UserPicture defaultPic;

}

