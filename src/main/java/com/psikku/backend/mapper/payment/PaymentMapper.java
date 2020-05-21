package com.psikku.backend.mapper.payment;

import com.psikku.backend.dto.payment.GeneratedPaymentDetailDto;
import com.psikku.backend.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public GeneratedPaymentDetailDto convertToGeneratedPaymentDto(Payment payment){
        GeneratedPaymentDetailDto generatedPaymentDetailDto = new GeneratedPaymentDetailDto();
        generatedPaymentDetailDto.setPaymentId(payment.getId());
        generatedPaymentDetailDto.setTotalPayment(payment.getTotalPayment());
        return generatedPaymentDetailDto;
    }

}
