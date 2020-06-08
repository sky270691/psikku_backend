package com.psikku.backend.service.voucher;

import com.psikku.backend.entity.Payment;
import com.psikku.backend.entity.TestPackage;
import com.psikku.backend.entity.Voucher;
import com.psikku.backend.exception.VoucherException;
import com.psikku.backend.repository.VoucherRepository;
import com.psikku.backend.service.company.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@Service
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;
    private final CompanyService companyService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public VoucherServiceImpl(VoucherRepository voucherRepository, CompanyService companyService){
        this.voucherRepository = voucherRepository;
        this.companyService = companyService;
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
    public void generateVoucher(int userCount, Payment payment, TestPackage testPackage,long companyId) {
        Voucher voucher = new Voucher();
        voucher.setUserCount(userCount);
        voucher.setPayment(payment);
        voucher.setValid(false);
        voucher.setVoucherCode(generateAlphaNumeric(12));
        voucher.setTestPackage(testPackage);
        voucher.setCompany(companyService.findById(companyId));

//        LocalDateTime localDateTime = LocalDateTime.now();
//        voucher.setValidUntil(localDateTime.plusDays(365));
        saveVoucher(voucher);
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

    @Override
    public Voucher findVoucherByPaymentId(long paymentId) {
        logger.info("getting voucher by payment Id...");
        return voucherRepository.findByPayment_Id(paymentId).orElseThrow(()->{
            logger.error("error find voucher by payment id: "+paymentId);
            return new VoucherException("Voucher not found");
        });
    }

    private String generateAlphaNumeric(int stringLength){
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
