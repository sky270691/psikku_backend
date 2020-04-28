package com.psikku.backend.service.uniquetestresultcalculator;

import com.psikku.backend.dto.test.SubmittedAnswerDto;

import java.util.List;

public interface UniqueResultTestCalculator {
    void calculateNewResult(List<SubmittedAnswerDto> submittedAnswerDtoList);
}
