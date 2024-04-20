package com.biz4solutions.clientinvoice.domain;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
@Getter
@Setter
@Where(clause = "is_active= true")
public class Otp extends BaseEntity {

    private static final long serialVersionUID = 4459352638034619430L;

    private String otpValue;

    private Date expireOn;

    @ManyToOne
    private OtpTransaction otpTransaction;


    /**
     * @return the otpTransaction
     */
    public OtpTransaction getOtpTransaction() {
        return otpTransaction;
    }


    /**
     * @param otpTransaction the otpTransaction to set
     */
    public void setOtpTransaction(OtpTransaction otpTransaction) {
        this.otpTransaction = otpTransaction;
    }


    /**
     * @return the otpValue
     */
    public String getOtpValue() {
        return otpValue;
    }


    /**
     * @param otpValue the otpValue to set
     */
    public void setOtpValue(String otpValue) {
        this.otpValue = otpValue;
    }


    /**
     * @return the expireOn
     */
    public Date getExpireOn() {
        return expireOn;
    }


    /**
     * @param expireOn the expireOn to set
     */
    public void setExpireOn(Date expireOn) {
        this.expireOn = expireOn;
    }

}

