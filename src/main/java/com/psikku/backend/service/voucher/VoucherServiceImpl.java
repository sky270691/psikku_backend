package com.psikku.backend.service.voucher;

import com.psikku.backend.dto.payment.GeneratedPaymentDetailDto;
import com.psikku.backend.dto.voucher.ValidateVoucherDto;
import com.psikku.backend.entity.Payment;
import com.psikku.backend.entity.TestPackage;
import com.psikku.backend.entity.User;
import com.psikku.backend.entity.Voucher;
import com.psikku.backend.exception.VoucherException;
import com.psikku.backend.repository.VoucherRepository;
import com.psikku.backend.service.company.CompanyService;
import com.psikku.backend.service.payment.PaymentService;
import com.psikku.backend.service.testpackage.TestPackageService;
import com.psikku.backend.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@Service
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;
    private final CompanyService companyService;
    private final TestPackageService testPackageService;
    private final Logger logger;
    private final UserService userService;
    private final PaymentService paymentService;

    @Autowired
    public VoucherServiceImpl(VoucherRepository voucherRepository,
                              CompanyService companyService,
                              @Lazy TestPackageService testPackageService,
                              UserService userService,
                              @Lazy PaymentService paymentService){
        this.voucherRepository = voucherRepository;
        this.companyService = companyService;
        this.testPackageService = testPackageService;
        this.paymentService = paymentService;
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.userService = userService;
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
    public boolean validateStatus(ValidateVoucherDto validateVoucherDto) {

        Voucher voucherLookup = voucherRepository.findVoucherByVoucherCode(validateVoucherDto.getVoucher())
                .orElseThrow(()->new VoucherException("voucher not found"));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        TestPackage testPackage = testPackageService.getPackageById(validateVoucherDto.getTestPackageId());

        //set the used count of the voucher based on the total user in voucher
        voucherLookup.setUsed(voucherLookup.getUserList().size());

        if(voucherLookup.getUserList().contains(user)){
            throw new VoucherException("voucher already redeemed");
        }
        if(voucherLookup.getTestPackage().getId() == testPackage.getId()
            && voucherLookup.getUsed() < voucherLookup.getUserCount()){
            voucherLookup.getUserList().add(user);
            voucherLookup.setUsed(voucherLookup.getUsed()+1);
            voucherRepository.save(voucherLookup);
            return true;
        }
        throw new VoucherException("Total users of this voucher already reached it's limit");
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
    public GeneratedPaymentDetailDto generateVoucherCurrentPackage(int packageId, int userCount, long companyId) {
        TestPackage testPackage = testPackageService.getPackageById(packageId);
        GeneratedPaymentDetailDto generatedPaymentDetailDto = paymentService.generatePayment(testPackage,userCount,companyId,testPackage.getPrice());
        return generatedPaymentDetailDto;
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
