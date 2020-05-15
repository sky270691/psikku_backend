package com.psikku.backend.service;

import com.psikku.backend.entity.Voucher;

public interface VoucherService {

    Voucher getVoucherByVoucherCode(String code);
    boolean validateVoucher(Voucher voucher);
    String generateVoucherCode();
    boolean saveVoucher(Voucher voucher);
    boolean deleteVoucher(long voucherId);
}
