package com.psikku.backend.service.payment;

import com.psikku.backend.dto.payment.GeneratedPaymentDetailDto;
import com.psikku.backend.dto.payment.ValidatePaymentDto;
import com.psikku.backend.dto.voucher.VoucherDto;
import com.psikku.backend.entity.Payment;
import com.psikku.backend.entity.TestPackage;

public interface PaymentService {

    GeneratedPaymentDetailDto generatePayment(TestPackage testPackage, int numOfUser, long companyId, int price);

    VoucherDto validatePayment(ValidatePaymentDto validatePaymentDto);

    Payment findPaymentById(long id);

}
