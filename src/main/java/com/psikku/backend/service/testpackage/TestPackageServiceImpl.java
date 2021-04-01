package com.psikku.backend.service.testpackage;

import com.psikku.backend.dto.payment.GeneratedPaymentDetailDto;
import com.psikku.backend.dto.test.MinimalTestDto;
import com.psikku.backend.dto.testpackage.PackageWithOrderDto;
import com.psikku.backend.dto.testpackage.TestPackageCreationDto;
import com.psikku.backend.dto.testpackage.TestPackageDto;
import com.psikku.backend.entity.*;
import com.psikku.backend.exception.PackageException;
import com.psikku.backend.mapper.test.TestMapper;
import com.psikku.backend.mapper.testpackage.TestPackageMapper;
import com.psikku.backend.repository.TestPackageRepository;
import com.psikku.backend.service.payment.PaymentService;
import com.psikku.backend.service.testresult.TestResultService;
import com.psikku.backend.service.user.UserService;
import com.psikku.backend.service.voucher.VoucherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TestPackageServiceImpl implements TestPackageService{

    private final Logger logger;
    private final TestPackageRepository testPackageRepository;
    private final TestResultService testResultService;
    private final PaymentService paymentService;
    private final TestPackageMapper testPackageMapper;
    private final TestMapper testMapper;
    private final UserService userService;
    private final VoucherService voucherService;

    @Autowired
    public TestPackageServiceImpl(TestPackageRepository testPackageRepository,
                                  TestResultService testResultService,
                                  PaymentService paymentService,
                                  TestPackageMapper testPackageMapper,
                                  TestMapper testMapper,
                                  UserService userService,
                                  @Lazy VoucherService voucherService) {
        this.testResultService = testResultService;
        this.testMapper = testMapper;
        this.voucherService = voucherService;
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.testPackageRepository = testPackageRepository;
        this.paymentService = paymentService;
        this.testPackageMapper = testPackageMapper;
        this.userService = userService;
    }

    @Override
    public GeneratedPaymentDetailDto generatePackage(TestPackageCreationDto testPackageCreationDto) {
        logger.info("try to generate package");
        TestPackage testPackage = testPackageMapper.convertToTestPackage(testPackageCreationDto);
        TestPackage savedTestPackage = testPackageRepository.save(testPackage);
        return paymentService.generatePayment(savedTestPackage,testPackageCreationDto.getNumOfUser(),testPackageCreationDto.getCompanyId(), testPackageCreationDto.getPrice());
    }

    @Override
    public TestPackage getPackageById(int id) {
        return testPackageRepository.findById(id).orElseThrow(() -> new PackageException("Package not found"));
    }

    @Override
    public List<MinimalTestDto> getAllTestDescByPackageId(int id){
        TestPackage testPackage = getPackageById(id);
        List<MinimalTestDto> minimalTestDtoList = new ArrayList<>();
        testPackage.getTestPackageTestList().forEach(test->{
            MinimalTestDto minimalTestDto = testMapper.convertToMinTestDto(test.getTest());
            minimalTestDto.setPriority(test.getPriority());
            minimalTestDtoList.add(minimalTestDto);
        });
        minimalTestDtoList.sort(Comparator.comparingInt(MinimalTestDto::getPriority));
        return minimalTestDtoList;
    }

    @Override
    public List<TestPackageDto> getAllPackage() {
        List<TestPackage> testPackageList = testPackageRepository.findAll();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<TestResult> testResultList = testResultService.findAllByUserName(username);
        List<TestPackageDto> testPackageDtoList = new ArrayList<>();

        for (TestPackage testPackage : testPackageList) {
            testPackageDtoList.add(testPackageMapper.convertToTestPackageDto(testPackage));
        }


        for (TestPackageDto testPackageDto : testPackageDtoList) {
            for (TestResult testResult : testResultList) {
                if(testResult.getVoucher().getTestPackage().getId() == testPackageDto.getId()){
                    for (MinimalTestDto minimalTestDto : testPackageDto.getMinimalTestDtoList()) {
                        if(testResult.getTest().getId() == minimalTestDto.getId()){
                            minimalTestDto.setFinish(true);
                        }
                    }
                }
            }
        }

        return testPackageDtoList.stream()
                .sorted((x,y)->y.getId()-x.getId())
                .collect(Collectors.toList());
    }

    @Override
    public boolean validateVoucherPackage(int idPackage) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        TestPackage testPackage = getPackageById(idPackage);
        List<Voucher> userVoucherList =
                user.getVoucherList().stream()
                        .filter(v -> v.getTestPackage().getId() == testPackage.getId())
                        .collect(Collectors.toList());
        return !userVoucherList.isEmpty();
    }

    @Override
    public PackageWithOrderDto getOrderingPackageByVoucher(String voucher) {
        Optional<TestPackage> tpOpt = testPackageRepository.getTestPackageByVoucher_VoucherCode(voucher);
        Voucher vch = voucherService.getVoucherByCode(voucher);
        TestPackage testPackage = null;
        if(tpOpt.isPresent()){
            testPackage = tpOpt.get();
        }else{
            throw new PackageException("voucher tidak valid");
        }
        List<MinimalTestDto> minTesDto = new ArrayList<>();
        List<TestPackageTest> tpt = testPackage.getTestPackageTestList();

        tpt.sort(Comparator.comparingInt(TestPackageTest::getPriority));
        for (TestPackageTest testPackageTest : tpt) {
            Test test = testPackageTest.getTest();
            MinimalTestDto minTestDto = new MinimalTestDto();
            minTestDto.setPriority(testPackageTest.getPriority());
            minTestDto.setSkippable(test.getSkippable());
            minTestDto.setDescription(test.getDescription());
            minTestDto.setName(test.getName());
            minTesDto.add(minTestDto);
        }

        PackageWithOrderDto dto = new PackageWithOrderDto();
        dto.setRest(vch.isRest());
        dto.setTestList(minTesDto);
        dto.setCurrentStopPosition(vch.getCurrentTestOrder());
        return dto;
    }

    @Override
    @Transactional
    public void sendStopSignal(String voucher, Integer position) {
        Voucher vch = voucherService.getVoucherByCode(voucher);
        vch.setCurrentTestOrder(position);
        voucherService.saveVoucher(vch);
    }

    @Override
    @Transactional
    public void sendRestSignal(String voucher, boolean rest) {
        Voucher vch = voucherService.getVoucherByCode(voucher);
        vch.setRest(rest);
        voucherService.saveVoucher(vch);
    }
}
