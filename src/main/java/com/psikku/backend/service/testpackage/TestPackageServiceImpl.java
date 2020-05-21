package com.psikku.backend.service.testpackage;

import com.psikku.backend.dto.payment.GeneratedPaymentDetailDto;
import com.psikku.backend.dto.testpackage.TestPackageCreationDto;
import com.psikku.backend.entity.TestPackage;
import com.psikku.backend.mapper.testpackage.TestPackageMapper;
import com.psikku.backend.repository.TestPackageRepository;
import com.psikku.backend.service.payment.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TestPackageServiceImpl implements TestPackageService{

    private final Logger logger;
    private final TestPackageRepository testPackageRepository;
    private final PaymentService paymentService;
    private final TestPackageMapper testPackageMapper;

    public TestPackageServiceImpl(TestPackageRepository testPackageRepository, PaymentService paymentService, TestPackageMapper testPackageMapper) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.testPackageRepository = testPackageRepository;
        this.paymentService = paymentService;
        this.testPackageMapper = testPackageMapper;
    }

    @Override
    public GeneratedPaymentDetailDto generatePackage(TestPackageCreationDto testPackageCreationDto) {
        logger.info("try to generate package");
        TestPackage testPackage = testPackageMapper.convertToTestPackage(testPackageCreationDto);
        TestPackage savedTestPackage = testPackageRepository.save(testPackage);
        return paymentService.generatePayment(savedTestPackage,testPackageCreationDto.getNumOfUser(),testPackageCreationDto.getCompanyId(), testPackageCreationDto.getPrice());
    }
}
