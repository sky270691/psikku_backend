package com.psikku.backend.service.voucher;

import com.psikku.backend.dto.voucher.ValidateVoucherDto;
import com.psikku.backend.entity.Payment;
import com.psikku.backend.entity.TestPackage;
import com.psikku.backend.entity.Voucher;

import java.util.Optional;

public interface VoucherService {

    boolean verifyVoucher(String code);
    Voucher getVoucherById(long voucherId);
    Voucher getVoucherByCode(String code);
    boolean validateStatus(ValidateVoucherDto voucherValidStatus);
    void generateVoucher(int userCount, Payment payment, TestPackage testPackage, long companyId);
    boolean saveVoucher(Voucher voucher);
    boolean deleteVoucher(long voucherId);
    Voucher findVoucherByPaymentId(long paymentId);

}
