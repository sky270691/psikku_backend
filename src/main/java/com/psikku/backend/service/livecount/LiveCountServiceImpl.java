package com.psikku.backend.service.livecount;

import com.psikku.backend.dto.testresult.TestFinalResultDto;
import com.psikku.backend.dto.testresult.TestResultDto;
import com.psikku.backend.entity.TestResult;
import com.psikku.backend.entity.Voucher;
import com.psikku.backend.service.testresult.TestResultService;
import com.psikku.backend.service.voucher.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LiveCountServiceImpl implements LiveCountService {


    private final TestResultService testResultService;
    private final VoucherService voucherService;

    @Autowired
    public LiveCountServiceImpl(TestResultService testResultService, VoucherService voucherService) {
        this.testResultService = testResultService;
        this.voucherService = voucherService;
    }


    @Override
    public List<TestFinalResultDto> getAllTestResultByTestIdAndVoucher(int testId, String voucher) {
        Voucher myVoucher = voucherService.getVoucherByCode(voucher);
        List<TestResult> testResultList = testResultService.findAllResultByTestIdAndVoucherId(testId,myVoucher.getId());
        List<TestFinalResultDto> testFinalResultDtoList = testResultList.stream()
                                                    .map(result -> testResultService.convertToTestResultDto(result))
                                                    .collect(Collectors.toList());
        return testFinalResultDtoList;
    }
}
