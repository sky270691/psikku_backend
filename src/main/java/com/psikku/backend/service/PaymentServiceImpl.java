package com.psikku.backend.service;

import com.psikku.backend.entity.Payment;
import com.psikku.backend.exception.PaymentException;
import com.psikku.backend.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public void verifyPayment(long paymentId) {
        Payment thePayment = getPaymentById(paymentId);
        thePayment.setPaid(true);
        savePayment(thePayment);
        logger.info("payment detail verified successfully");
    }

    @Override
    public Payment getPaymentById(long paymentId) {
        Payment thePayment = paymentRepository.findById(paymentId).orElseThrow(()-> {
            logger.error("error finding payment in database");
            return new PaymentException("payment not found");
        });
        logger.info("getting payment data success");
        return thePayment;
    }

    //Todo
    // make sure date persisted just like what we desire
    @Override
    public Payment generatePaymentDetail(int packagePrice, Payment.Method paymentMethod) {
        Payment payment = new Payment();
        payment.setPaid(false);
        payment.setTotalPayment(packagePrice);
        payment.setMethod(paymentMethod.name());
        logger.info("payment detail generated");
        return payment;
    }

    @Override
    public void savePayment(Payment payment) {
        paymentRepository.save(payment);
        logger.info("payment data saved successfully");
    }
}
