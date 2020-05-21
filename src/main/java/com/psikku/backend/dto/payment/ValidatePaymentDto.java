package com.psikku.backend.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.psikku.backend.entity.Payment;

import java.time.LocalDate;

public class ValidatePaymentDto {

    @JsonProperty("payment_id")
    private long id;

    @JsonProperty("payment_method")
    private Payment.Method paymentMethod;

    @JsonProperty("payment_date")
    private LocalDate paymentDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Payment.Method getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Payment.Method paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }
}
