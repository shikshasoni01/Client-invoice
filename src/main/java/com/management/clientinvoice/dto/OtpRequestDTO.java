package com.biz4solutions.clientinvoice.dto;

public class OtpRequestDTO {

    private String transactionId;

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
        return "OtpRequestDTO [transactionId=" + transactionId + "]";
    }


}

