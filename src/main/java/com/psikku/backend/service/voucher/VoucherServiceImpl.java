package com.psikku.backend.service.voucher;

import com.psikku.backend.dto.payment.GeneratedPaymentDetailDto;
import com.psikku.backend.dto.test.MinimalTestDto;
import com.psikku.backend.dto.testpackage.TestPackageDto;
import com.psikku.backend.dto.voucher.ValidateVoucherDto;
import com.psikku.backend.entity.*;
import com.psikku.backend.exception.VoucherException;
import com.psikku.backend.mapper.testpackage.TestPackageMapper;
import com.psikku.backend.repository.VoucherRepository;
import com.psikku.backend.service.company.CompanyService;
import com.psikku.backend.service.payment.PaymentService;
import com.psikku.backend.service.testpackage.TestPackageService;
import com.psikku.backend.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.security.SecureRandom;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;
    private final CompanyService companyService;
    private final TestPackageService testPackageService;
    private final Logger logger;
    private final UserService userService;
    private final PaymentService paymentService;
    private final TestPackageMapper testPackageMapper;
    private final ResourceLoader resourceLoader;

    @Autowired
    public VoucherServiceImpl(VoucherRepository voucherRepository,
                              CompanyService companyService,
                              @Lazy TestPackageService testPackageService,
                              UserService userService,
                              @Lazy PaymentService paymentService,
                              @Lazy TestPackageMapper testPackageMapper,
                              ResourceLoader resourceLoader) {
        this.voucherRepository = voucherRepository;
        this.companyService = companyService;
        this.testPackageService = testPackageService;
        this.paymentService = paymentService;
        this.testPackageMapper = testPackageMapper;
        this.resourceLoader = resourceLoader;
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
        return voucherRepository.findVoucherByVoucherCode(code).orElseThrow(() -> {
            logger.error("error getting the voucher");
            return new VoucherException("Voucher not Found");
        });
    }

    @Override
    public Voucher getVoucherById(long voucherId) {
        return voucherRepository.findById(voucherId).orElseThrow(() -> {
            logger.error("find voucher by Id error");
            return new VoucherException("Voucher Not Found");
        });
    }

    @Override
    public boolean validateStatus(ValidateVoucherDto validateVoucherDto) {

        Voucher voucherLookup = voucherRepository.findVoucherByVoucherCode(validateVoucherDto.getVoucher())
                .orElseThrow(() -> new VoucherException("voucher not found"));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        TestPackage testPackage = testPackageService.getPackageById(validateVoucherDto.getTestPackageId());

        if (!testPackage.getCategory().equalsIgnoreCase(validateVoucherDto.getCategory())) {
            throw new VoucherException("Voucher category did not valid for the test package");
        }

        //todo
        // detect voucher by company
        if (voucherLookup.getCompany() != null) {
//            user.getCompany().getId() == voucherLookup.getCompany().getId()
        }

        //set the used count of the voucher based on the total user in voucher
        voucherLookup.setUsed(voucherLookup.getUserList().size());

        if (voucherLookup.getUserList().contains(user) && voucherLookup.getTestPackage().getId() == testPackage.getId()) {
            throw new VoucherException("voucher already redeemed");
        }

        if (voucherLookup.getTestPackage().getId() == testPackage.getId()
                && voucherLookup.getUsed() < voucherLookup.getUserCount()) {
            voucherLookup.getUserList().add(user);
            voucherLookup.setUsed(voucherLookup.getUsed() + 1);
            voucherRepository.save(voucherLookup);
        } else {
            throw new VoucherException(" voucher did not valid for the test package or Total limit already reached");
        }

        return true;
    }

    @Override
    public TestPackageDto validateStatusV2(String voucherCode, String category) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        Resource resource = resourceLoader.getResource("classpath:static/test/uservoucher.txt");


        Voucher voucher = getVoucherByCode(voucherCode);
        TestPackage testPackage = voucher.getTestPackage();

        if (!voucher.isValid()){
            throw new VoucherException("Voucher is not valid");
        }

        if (!testPackage.getCategory().equalsIgnoreCase(category)) {
            throw new VoucherException("Voucher category did not valid for the test package");
        }

        TestPackageDto dto = testPackageMapper.convertToTestPackageDto(testPackage);
        dto.setCurrentTestOrder(voucher.getCurrentTestOrder());
        if (voucher.getUserList().contains(user)) {
            dto.setMessage("voucher already redeemed");
            List<TestResult> allTestResultForCurrentUser = user.getTestResult();
            if (allTestResultForCurrentUser != null && !allTestResultForCurrentUser.isEmpty()) {
                for (TestResult testResult : allTestResultForCurrentUser) {
                    if (testResult.getVoucher().getId() == voucher.getId()) {
                        for (MinimalTestDto minimalTestDto : dto.getMinimalTestDtoList()) {
                            if (minimalTestDto.getId() == testResult.getTest().getId()) {
                                minimalTestDto.setFinish(true);
                            }
                        }

                    }
                }
            }
            return dto;
        }


        if (voucher.getCompany().getId() == 1) {
            voucher.getUserList().add(user);
            voucher.setUsed(voucher.getUsed() + 1);
            voucherRepository.save(voucher);
            dto.getMinimalTestDtoList().sort(Comparator.comparingInt(MinimalTestDto::getPriority));
            return dto;
        }

        if (testPackage.isRequiredPreRegister()) {

            Path userVoucherValidationFile = null;

            try {
                userVoucherValidationFile = Paths.get(resource.getURI());
            } catch (IOException e) {
                e.printStackTrace();
            }

            Path tempFile = userVoucherValidationFile.getParent().resolve("temp.txt");

            synchronized (this) {
                try (BufferedReader reader = Files.newBufferedReader(userVoucherValidationFile);
                     BufferedWriter writer = Files.newBufferedWriter(tempFile)) {

                    if (reader.lines().filter(line -> line.startsWith(voucherCode))
                            .anyMatch(line -> line.split(":")[1].equalsIgnoreCase(user.getEmail()))) {

                        String currentLine;

                        while ((currentLine = reader.readLine()) != null) {
                            if (!currentLine.equalsIgnoreCase(voucherCode + ":" + user.getEmail())) {
                                writer.write(currentLine + "\n");
                            }
                        }
                        writer.close();

                        voucher.getUserList().add(user);
                        voucher.setUsed(voucher.getUsed() + 1);
                        voucherRepository.save(voucher);

                        Files.move(tempFile, tempFile.getParent().resolve("uservoucher.txt"), StandardCopyOption.REPLACE_EXISTING);

                        return dto;
                    } else {
                        throw new VoucherException("anda tidak terdaftar di paket tes ini");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        } else {
            voucher.getUserList().add(user);
            voucher.setUsed(voucher.getUsed() + 1);
            voucherRepository.save(voucher);
            return dto;
        }
        throw new VoucherException("invalid voucher");
    }

    @Override
    public void generateVoucher(int userCount, Payment payment, TestPackage testPackage, long companyId) {
        Voucher voucher = new Voucher();
        voucher.setUserCount(userCount);
        voucher.setPayment(payment);
        voucher.setValid(false);
        voucher.setVoucherCode(generateAlphaNumeric(12));
        voucher.setTestPackage(testPackage);
        voucher.setCurrentTestOrder(-1);
        voucher.setCompany(companyService.findById(companyId));

        saveVoucher(voucher);
    }

    @Override
    public GeneratedPaymentDetailDto generateVoucherCurrentPackage(int packageId, int userCount, long companyId) {
        TestPackage testPackage = testPackageService.getPackageById(packageId);
        GeneratedPaymentDetailDto generatedPaymentDetailDto = paymentService.generatePayment(testPackage, userCount, companyId, testPackage.getPrice());
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
        return voucherRepository.findByPayment_Id(paymentId).orElseThrow(() -> {
            logger.error("error find voucher by payment id: " + paymentId);
            return new VoucherException("Voucher not found");
        });
    }

    @Override
    public boolean validateUrutan(String voucher, Integer testId) {
        Voucher vch = getVoucherByCode(voucher);
        if(vch.getCurrentTestOrder() == -1){
            return true;
        }
        int currentOrder = vch.getCurrentTestOrder();
        boolean valid = vch.getTestPackage().getTestPackageTestList().stream()
                .filter(x -> x.getTest().getId() == testId)
                .map(x -> x.getPriority() <= currentOrder)
                .findFirst().orElse(false);
        return valid;
    }

    @Override
    public synchronized void registerUserToVoucher(List<String> email, String voucher) {
        voucherRepository.findVoucherByVoucherCode(voucher).orElseThrow(() -> new VoucherException("voucher not found"));
        Resource resource = resourceLoader.getResource("classpath:static/test/uservoucher.txt");

        Path txtFile = null;
        try {
            txtFile = Paths.get(resource.getURI());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = Files.newBufferedWriter(txtFile, StandardOpenOption.WRITE, StandardOpenOption.APPEND)) {
            for (String emailString : email) {
                bw.write(voucher + ":" + emailString + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String generateAlphaNumeric(int stringLength) {
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
