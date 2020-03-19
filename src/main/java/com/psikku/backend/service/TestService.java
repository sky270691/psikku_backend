package com.psikku.backend.service;

import com.psikku.backend.dto.Test.FullTestDto;
import com.psikku.backend.dto.Test.SubmittedAnswerDto;
import com.psikku.backend.dto.Test.TestDto;
import com.psikku.backend.entity.SubmittedAnswer;
import com.psikku.backend.entity.Subtest;
import com.psikku.backend.entity.Test;
import com.psikku.backend.entity.User;

import java.util.List;

public interface TestService {
    List<Test> findAll();
    Test addNewTest(FullTestDto fullTestDto);
    Test convertToTestEntity(FullTestDto fullTestDto);
    TestDto convertToMinimalTestDto(Test test);
    Test findTestById(int id);
    Test findTestByName(String name);
    FullTestDto convertToFullTestDto(Test test);

//  subtest area
    Subtest findSubtestById(String id);
}
