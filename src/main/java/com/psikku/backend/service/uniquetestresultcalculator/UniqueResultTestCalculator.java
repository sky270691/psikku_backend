package com.psikku.backend.service.uniquetestresultcalculator;

import com.psikku.backend.dto.useranswer.SubmittedAnswerDto;
import com.psikku.backend.entity.TestResult;

import java.util.List;

public interface UniqueResultTestCalculator {
    TestResult calculateNewResult(List<SubmittedAnswerDto> submittedAnswerDtoList);
    void setResult(String result);
    String getResult();
}
