package com.management.clientinvoice.domain;


import org.hibernate.annotations.Where;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "login_history")
@Where(clause="is_active= true")
public class LoginHistory extends BaseEntity {

    private static final long serialVersionUID = 6332964680032660230L;

    private String ip;

    private String deviceType;

    private String deviceId;

    private String timeZone;

    private String appVersion;

    private String deviceVersion;

    private String deviceName;

    @ManyToOne
    private UserIdentity userIdentities;

}

