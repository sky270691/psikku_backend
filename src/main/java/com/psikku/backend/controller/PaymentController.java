package com.psikku.backend.controller;

import com.psikku.backend.dto.payment.ValidatePaymentDto;
import com.psikku.backend.dto.voucher.VoucherDto;
import com.psikku.backend.service.payment.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PutMapping
    public ResponseEntity<VoucherDto> validatePayment(@RequestBody ValidatePaymentDto validatePaymentDto){
       VoucherDto voucherDto = paymentService.validatePayment(validatePaymentDto);
       return new ResponseEntity<>(voucherDto, HttpStatus.ACCEPTED);
    }

}
