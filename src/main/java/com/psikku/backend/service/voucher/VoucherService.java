package com.psikku.backend.service.voucher;

import com.psikku.backend.dto.payment.GeneratedPaymentDetailDto;
import com.psikku.backend.dto.testpackage.TestPackageDto;
import com.psikku.backend.dto.voucher.ValidateVoucherDto;
import com.psikku.backend.entity.Payment;
import com.psikku.backend.entity.TestPackage;
import com.psikku.backend.entity.Voucher;

import java.util.List;
import java.util.Optional;

public interface VoucherService {

    boolean verifyVoucher(String code);
    Voucher getVoucherById(long voucherId);
    Voucher getVoucherByCode(String code);
    boolean validateStatus(ValidateVoucherDto voucherValidStatus);
    TestPackageDto validateStatusV2(String voucherCode, String category);
    void generateVoucher(int userCount, Payment payment, TestPackage testPackage, long companyId);
    GeneratedPaymentDetailDto generateVoucherCurrentPackage(int packageId, int userCount, long companyId);
    boolean saveVoucher(Voucher voucher);
    boolean deleteVoucher(long voucherId);
    Voucher findVoucherByPaymentId(long paymentId);

}
