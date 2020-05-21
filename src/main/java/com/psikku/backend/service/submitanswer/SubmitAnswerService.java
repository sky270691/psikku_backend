package com.psikku.backend.service.submitanswer;


import com.psikku.backend.dto.testresult.TestFinalResultDto;
import com.psikku.backend.dto.useranswer.SubmittedAnswerDto;
import com.psikku.backend.dto.useranswer.UserAnswerDto;
import com.psikku.backend.entity.SubmittedAnswer;
import com.psikku.backend.entity.TestResult;
import com.psikku.backend.entity.User;

import java.util.List;

public interface SubmitAnswerService {
    List<SubmittedAnswer> saveUserAnswer(List<SubmittedAnswer> answerList);
    List<SubmittedAnswer> convertToSubmittedAnswerList(List<SubmittedAnswerDto> submittedAnswerDto, User user);
    List<SubmittedAnswerDto> convertToSubmittedAnswerDtoList(List<SubmittedAnswer> submittedAnswerList);
    TestFinalResultDto calculateGenericTest(UserAnswerDto submittedAnswerList);
    TestFinalResultDto calculateResultTestV2(UserAnswerDto userAnswerDto);
//    void getListTest(List<SubmittedAnswer> submittedAnswerList);
}
