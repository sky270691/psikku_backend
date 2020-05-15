package com.psikku.backend.service;

import com.psikku.backend.entity.Payment;

public interface PaymentService {

    void verifyPayment(long paymentId);
    Payment getPaymentById(long paymentId);
    Payment generatePaymentDetail(int packagePrice, Payment.Method paymentMethod);
    void savePayment(Payment payment);

}
