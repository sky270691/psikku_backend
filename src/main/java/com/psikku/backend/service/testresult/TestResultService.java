package com.psikku.backend.service.testresult;

import com.psikku.backend.dto.testresult.TestFinalResultDto;
import com.psikku.backend.entity.Test;
import com.psikku.backend.entity.TestResult;
import com.psikku.backend.entity.User;

import java.util.List;
import java.util.Optional;

public interface TestResultService  {
    //--------------------repository method-----------------------------------------
    TestResult saveTestResult(TestResult testResult);
    List<TestResult> findAllByUserName (String username);
    List<TestResult> findAllResultByUserId(long userId);
    List<TestResult> findAllResultByTestId(int id);
    List<TestResult> findAllResultByVoucherIdAndUsername(long voucherId, String userName);
    List<TestResult> findAllResultByTestIdAndVoucherId(int id, long voucherId);
    List<TestFinalResultDto> findAllResultByVoucherAndUsername(String voucher, String username);
    List<TestResult> findAllResultByVoucherAndUsername(User user, String voucher);
    List<TestResult> findAllResultByVoucher(String voucher);
    Optional<TestResult> findTestResultByVoucherCodeUsernameAndTest(String voucherCode, String username, int testId);

    //-------------converter method-------------------------------------------------
    TestFinalResultDto convertToTestResultDto(TestResult testResult);
}
