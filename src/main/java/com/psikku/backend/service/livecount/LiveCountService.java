package com.psikku.backend.service.livecount;

import com.psikku.backend.dto.testresult.TestFinalResultDto;
import com.psikku.backend.dto.testresult.TestResultDto;

import java.util.List;

public interface LiveCountService {

    List<TestFinalResultDto> getAllTestResultByTestIdAndVoucher(int id, String voucher);

}
