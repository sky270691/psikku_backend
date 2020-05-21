package com.psikku.backend.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeneratedPaymentDetailDto {

    @JsonProperty(value = "payment_id")
    private long paymentId;

    @JsonProperty(value = "total_payment")
    private int totalPayment;

    @JsonProperty(value = "payment_method",access = JsonProperty.Access.WRITE_ONLY)
    private String method;

    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }

    public int getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(int totalPayment) {
        this.totalPayment = totalPayment;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
