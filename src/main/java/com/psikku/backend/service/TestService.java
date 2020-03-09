package com.psikku.backend.service;

import com.psikku.backend.dto.FullTestDto;
import com.psikku.backend.dto.TestDto;
import com.psikku.backend.entity.Test;

import java.util.List;

public interface TestService {
    List<Test> findAll();
    Test addNewTest(FullTestDto fullTestDto);
    Test convertToTestEntity(FullTestDto fullTestDto);
    TestDto convertToTestDto(Test test);
}
