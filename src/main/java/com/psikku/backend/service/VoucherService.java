package com.psikku.backend.service;

import com.psikku.backend.entity.Voucher;

public interface VoucherService {

    boolean verifyVoucher(String code);
    Voucher getVoucherById(long voucherId);
    void setVoucherValidStatus(boolean voucherValidStatus);
    String generateVoucherCode();
    boolean saveVoucher(Voucher voucher);
    boolean deleteVoucher(long voucherId);

}
