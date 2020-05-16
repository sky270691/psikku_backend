package com.psikku.backend.service;

import com.psikku.backend.entity.Voucher;
import com.psikku.backend.exception.VoucherException;
import com.psikku.backend.repository.VoucherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

public class VoucherServiceImpl implements VoucherService {

    private VoucherRepository voucherRepository;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public VoucherServiceImpl(VoucherRepository voucherRepository){
        this.voucherRepository = voucherRepository;
    }

    @Override
    public boolean verifyVoucher(String code) {

        Voucher voucher = getVoucherByCode(code);
        return (voucher != null && voucher.isValid());
    }

    @Override
    public Voucher getVoucherByCode(String code) {
        return voucherRepository.findVoucherByVoucherCode(code).orElseThrow(()->{
            logger.error("error getting the voucher");
            return new VoucherException("Voucher not Found");
        });
    }

    @Override
    public Voucher getVoucherById(long voucherId) {
        return voucherRepository.findById(voucherId).orElseThrow(()->{
            logger.error("find voucher by Id error");
            return new VoucherException("Voucher Not Found");
        });
    }

    @Override
    public void setVoucherValidStatus(boolean voucherValidStatus) {

    }

    @Override
    public String generateVoucherCode() {

        return generateAlphaNumeric(12);
    }

    @Override
    public boolean saveVoucher(Voucher voucher) {
        voucherRepository.save(voucher);
        return true;
    }

    @Override
    public boolean deleteVoucher(long voucherId) {
        return false;
    }

    private static String generateAlphaNumeric(int stringLength){
        String alphaLower = "abcdefghijklmnopqrstuvwxyz";
        String alphaUpper = alphaLower.toUpperCase();
        String number = "0123456789";

        String combination = alphaLower + alphaUpper + number;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stringLength; i++) {
            Random random = new SecureRandom();
            sb.append(combination.charAt(random.nextInt(combination.length())));
        }
        return sb.toString();
    }
}
