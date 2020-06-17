package com.psikku.backend.service.testpackage;

import com.psikku.backend.dto.payment.GeneratedPaymentDetailDto;
import com.psikku.backend.dto.test.MinimalTestDto;
import com.psikku.backend.dto.testpackage.TestPackageCreationDto;
import com.psikku.backend.dto.testpackage.TestPackageDto;
import com.psikku.backend.entity.TestPackage;
import com.psikku.backend.entity.User;
import com.psikku.backend.entity.Voucher;
import com.psikku.backend.exception.PackageException;
import com.psikku.backend.mapper.test.TestMapper;
import com.psikku.backend.mapper.testpackage.TestPackageMapper;
import com.psikku.backend.repository.TestPackageRepository;
import com.psikku.backend.service.payment.PaymentService;
import com.psikku.backend.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestPackageServiceImpl implements TestPackageService{

    private final Logger logger;
    private final TestPackageRepository testPackageRepository;
    private final PaymentService paymentService;
    private final TestPackageMapper testPackageMapper;
    private final TestMapper testMapper;
    private final UserService userService;

    @Autowired
    public TestPackageServiceImpl(TestPackageRepository testPackageRepository,
                                  PaymentService paymentService,
                                  TestPackageMapper testPackageMapper,
                                  TestMapper testMapper,
                                  UserService userService) {
        this.testMapper = testMapper;
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
        testPackage.getTestList().forEach(test->minimalTestDtoList.add(testMapper.convertToMinTestDto(test)));
        return minimalTestDtoList;
    }

    @Override
    public List<TestPackageDto> getAllPackage() {
        List<TestPackage> testPackageList = testPackageRepository.findAll();
        List<TestPackageDto> testPackageDtoList = new ArrayList<>();
        testPackageList.forEach(testPackage-> testPackageDtoList.add(testPackageMapper.convertToTestPackageDto(testPackage)));
        return testPackageDtoList;
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
}
