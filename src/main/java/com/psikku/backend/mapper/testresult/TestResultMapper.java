package com.psikku.backend.mapper.testresult;

import com.psikku.backend.dto.testresult.TestFinalResultDto;
import com.psikku.backend.entity.TestResult;
import com.psikku.backend.service.test.TestService;
import com.psikku.backend.service.user.UserService;
import com.psikku.backend.service.voucher.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class TestResultMapper {

    private final TestService testService;
    private final VoucherService voucherService;
    private final UserService userService;

    @Autowired
    public TestResultMapper(@Lazy TestService testService,
                            VoucherService voucherService,
                            UserService userService) {
        this.testService = testService;
        this.voucherService = voucherService;
        this.userService = userService;
    }

    public TestFinalResultDto convertToTestFinalResultDto(TestResult testResult){

        TestFinalResultDto testFinalResultDto = new TestFinalResultDto();
        testFinalResultDto.setTestName(testResult.getTest().getName());
        testFinalResultDto.setInternalName(testResult.getTest().getInternalName());
        testFinalResultDto.setResult(testResult.getResult());
        testFinalResultDto.setTestDateTime(testResult.getDateOfTest());
        testFinalResultDto.setResultCalculation(testResult.getResultCalculation());

        return testFinalResultDto;
    }

    public TestResult convertToTestResult(TestFinalResultDto testFinalResultDto,
                                          String voucher,
                                          String username){

        TestResult testResult = new TestResult();
        testResult.setTest(testService.findTestByInternalName(testFinalResultDto.getInternalName()));
        testResult.setDateOfTest(testFinalResultDto.getTestDateTime());
        testResult.setVoucher(voucherService.getVoucherByCode(voucher));
        testResult.setResult(testFinalResultDto.getResult());
        testResult.setResultCalculation(testFinalResultDto.getResultCalculation());
        testResult.setUser(userService.findByUsername(username));

        return testResult;
    }

}

