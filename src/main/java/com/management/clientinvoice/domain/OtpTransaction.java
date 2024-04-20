package com.biz4solutions.clientinvoice.domain;


import com.biz4solutions.clientinvoice.constant.DBConstants;
import com.biz4solutions.clientinvoice.enumerator.OtpType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;


@Entity
@Getter
@Setter
@Where(clause = "is_active= true")
public class OtpTransaction extends BaseEntity {

    private static final long serialVersionUID = 4168792246573938690L;

    @Column(unique = true, nullable = false)
    private String transactionId;

    @Column(length = DBConstants.ENUM_DEFAULT_MAX_CHAR_LIMIT)
    @Enumerated(EnumType.STRING)
    private OtpType type;

    @OneToMany(mappedBy = "otpTransaction")
    private List<Otp> Otps;

    private boolean isVerified;

    @ManyToOne
    private UserIdentity userIdentity;

}

