package com.psikku.backend.service;

import com.psikku.backend.entity.Voucher;

import java.util.Optional;

public interface VoucherService {

    boolean verifyVoucher(String code);
    Voucher getVoucherById(long voucherId);
    Voucher getVoucherByCode(String code);
    void setVoucherValidStatus(boolean voucherValidStatus);
    String generateVoucherCode();
    boolean saveVoucher(Voucher voucher);
    boolean deleteVoucher(long voucherId);

}
