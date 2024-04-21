package com.management.clientinvoice.domain;


import com.management.clientinvoice.constant.DBConstants;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Setter
@Entity
@Table(name="userTable")
public class User extends BaseEntity {

    private static final long serialVersionUID = 6329146659304109633L;


    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name="first_name",length = DBConstants.NAME_MAX_CHAR_LIMIT)
    private String firstName;

    @Column(name="last_name",length = DBConstants.NAME_MAX_CHAR_LIMIT)
    private String lastName;

    @Column(name="email",length = DBConstants.EMAIL_MAX_CHAR_LIMIT)
    private String email;

    @Column(name="employee_id")
    private String employeeId;

    @Column(name="contact_number")
    @Size(min = DBConstants.CONTACT_NO_MIN_CHAR_LIMIT ,max = DBConstants.CONTACT_NO_MAX_CHAR_LIMIT)
    private String contactNumber;

    @Column(name="profile_image_url")
    private String profileImageUrl;

 }
