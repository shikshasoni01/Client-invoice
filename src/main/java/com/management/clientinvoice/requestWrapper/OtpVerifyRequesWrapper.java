package com.management.clientinvoice.requestWrapper;


import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class OtpVerifyRequesWrapper {

    @NotNull(message = "otpblank")
    private String otp;

    @NotNull(message = "otpTransactionIdBlank")
    @NotBlank(message = "otpTransactionIdBlank")
    private String transactionId;

    /**
     * @return the otp
     */
    public String getOtp() {
        return otp;
    }

    /**
     * @param otp the otp to set
     */
    public void setOtp(String otp) {
        this.otp = otp;
    }

    /**
     * @return the transactionId
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * @param transactionId the transactionId to set
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "OtpVerifyRequesWrapper [otp=" + otp + ", transactionId=" + transactionId + "]";
    }


}

