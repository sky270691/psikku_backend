package com.psikku.backend.service.test;

import com.psikku.backend.dto.test.FullTestDto;
import com.psikku.backend.dto.test.MinimalTestDto;
import com.psikku.backend.entity.Subtest;
import com.psikku.backend.entity.Test;

import java.util.List;

public interface TestService {
    List<Test> findAll();
    Test addNewTest(FullTestDto fullTestDto);
    Test convertToTestEntity(FullTestDto fullTestDto);
    MinimalTestDto convertToMinimalTestDto(Test test);
    Test findTestById(int id);
    Test findTestByName(String name);
    FullTestDto convertToFullTestDto(Test test);
    List<MinimalTestDto> getAllMinTestList();
    List<MinimalTestDto> getMinTestByVoucher(String voucherCode);

//  subtest area
    Subtest findSubtestById(String id);
}
