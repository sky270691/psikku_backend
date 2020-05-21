package com.psikku.backend.service.payment;

import com.psikku.backend.dto.payment.GeneratedPaymentDetailDto;
import com.psikku.backend.dto.payment.ValidatePaymentDto;
import com.psikku.backend.dto.voucher.VoucherDto;
import com.psikku.backend.entity.Payment;
import com.psikku.backend.entity.TestPackage;
import com.psikku.backend.entity.Voucher;
import com.psikku.backend.exception.PaymentException;
import com.psikku.backend.mapper.payment.PaymentMapper;
import com.psikku.backend.mapper.voucher.VoucherMapper;
import com.psikku.backend.repository.PaymentRepository;
import com.psikku.backend.service.voucher.VoucherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final Logger logger;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final VoucherService voucherService;
    private final VoucherMapper voucherMapper;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentMapper paymentMapper, VoucherService voucherService, VoucherMapper voucherMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.voucherService = voucherService;
        this.voucherMapper = voucherMapper;
        logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public GeneratedPaymentDetailDto generatePayment(TestPackage testPackage, int numOfUser, long companyId, int price) {
        Payment payment = new Payment();
        payment.setTotalPayment(price);
        payment.setPaid(false);
        Payment savedPayment = paymentRepository.save(payment);
        voucherService.generateVoucher(numOfUser,payment,testPackage,companyId);
        return paymentMapper.convertToGeneratedPaymentDto(savedPayment);
    }

    @Override
    public VoucherDto validatePayment(ValidatePaymentDto validatePaymentDto) {
        Payment payment = findPaymentById(validatePaymentDto.getId());
        payment.setPaid(true);
        payment.setPaymentDate(validatePaymentDto.getPaymentDate());
        payment.setMethod(validatePaymentDto.getPaymentMethod().name());
        payment = paymentRepository.save(payment);
        Voucher voucher = voucherService.findVoucherByPaymentId(payment.getId());
        voucher.setValidUntil(LocalDateTime.now().plusDays(365));
        voucher.setValid(true);
        voucherService.saveVoucher(voucher);
        VoucherDto voucherDto = voucherMapper.convertEntityToVoucherDto(voucher);
        return voucherDto;
    }

    @Override
    public Payment findPaymentById(long id) {
        return paymentRepository.findById(id).orElseThrow(()-> {
            logger.error("payment by id: "+id+" not found");
            return new PaymentException("payment not found");
        });
    }

}
