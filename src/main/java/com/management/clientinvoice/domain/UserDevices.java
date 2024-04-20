package com.biz4solutions.clientinvoice.domain;



import com.biz4solutions.clientinvoice.constant.DBConstants;
import com.biz4solutions.clientinvoice.enumerator.DeviceType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "user_devices")
@Where(clause="is_active= true")
public class UserDevices extends BaseEntity {

     private static final long serialVersionUID = -1446694529491359658L;

    /**
     * User to whom this device belongs
     */
    @ManyToOne(optional = false)
    private UserIdentity userIdentity;

    /**
     * Device Type i.e. iOS, Android, iPod, iPad, phone, tablet or web
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", length = DBConstants.ENUM_DEFAULT_MAX_CHAR_LIMIT, nullable = false)
    private DeviceType deviceType;

    /**
     * OS Version of the Device
     */
    @Column(name = "os_version", length = DBConstants.ADDRESS_MAX_CHAR_LIMIT)
    private String osVersion;

    /**
     * Registration ID for Push Notification
     */
    @Column(name = "registration_id", length = 255)
    private String registrationId;

    /**
     * Device ID of Android / Device UDID for iOS
     */
    @Column(name = "device_UID", length = 255)
    private String deviceUID;

    /**
     * Version of App currently installed on this Device
     */
    @Column(name = "app_version", length = 255)
    private String appVersion;

    /**
     * domain name of Device
     */
    @Column(name = "domain_name", length = 255)
    private String domainName;

    /**
     * domain version of Device
     */
    @Column(name = "domain_version", length = 255)
    private String domainVersion;

    /**
     * Vendor information of this Device
     */
    @Column(name = "vendor", length = 255)
    private String vendor;

    /**
     * True if the app is last accessed from this device.
     * At any instance only 1 device will be default.
     */
    @Column(name = "is_default")
    private Boolean isDefault = Boolean.FALSE;

}

