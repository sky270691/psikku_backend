package com.psikku.backend.service;


import com.psikku.backend.dto.Test.SubmittedAnswerDto;
import com.psikku.backend.entity.SubmittedAnswer;
import com.psikku.backend.entity.Test;
import com.psikku.backend.entity.User;

import java.util.List;
import java.util.Map;

public interface SubmitAnswerService {
    List<SubmittedAnswer> saveUserAnswer(List<SubmittedAnswer> answerList);
    List<SubmittedAnswer> convertToSubmittedAnswerList(List<SubmittedAnswerDto> submittedAnswerDto, User user);
    List<SubmittedAnswerDto> convertToSubmittedAnswerDtoList(List<SubmittedAnswer> submittedAnswerList);
    void calculateResultTest(List<SubmittedAnswer> submittedAnswerList);
//    void getListTest(List<SubmittedAnswer> submittedAnswerList);
}
