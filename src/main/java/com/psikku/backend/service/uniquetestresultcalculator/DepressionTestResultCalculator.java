package com.psikku.backend.service.uniquetestresultcalculator;

import com.psikku.backend.dto.useranswer.SubmittedAnswerDto;
import com.psikku.backend.entity.Answer;
import com.psikku.backend.entity.Test;
import com.psikku.backend.entity.TestResult;
import com.psikku.backend.entity.User;
import com.psikku.backend.service.answer.AnswerService;
import com.psikku.backend.service.test.TestService;
import com.psikku.backend.service.user.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepressionTestResultCalculator implements UniqueResultTestCalculator{

    private final TestService testService;
    private final UserService userService;
    private final AnswerService answerService;
    private String result;

    public DepressionTestResultCalculator(TestService testService, UserService userService, AnswerService answerService) {
        this.testService = testService;
        this.userService = userService;
        this.answerService = answerService;
        this.result = "";
    }

    @Override
    public TestResult calculateNewResult(List<SubmittedAnswerDto> submittedAnswerDtoList) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);

        String testName = submittedAnswerDtoList.get(0).getQuestionId().split("_")[0];
        Test test = testService.findTestByName(testName);

        List<Answer> answersFromDb = answerService.findByIdStartingWith(testName);

        int answerPoints = 0;

        for (SubmittedAnswerDto answerDto : submittedAnswerDtoList) {
            for (Answer ansFromDb : answersFromDb) {
                if(answerDto.getAnswers().get(0).equals(ansFromDb.getId())){
                    answerPoints += Integer.parseInt(ansFromDb.getAnswerCategory());
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(perCategoryPredicate(answerPoints));
        setResult(sb.toString());

        TestResult testResult = new TestResult();
        testResult.setResult(getResult());
        testResult.setUser(user);
        testResult.setTest(test);

        return null;
    }

    private String perCategoryPredicate(int resultValue){
        if(resultValue < 12){
            return "Tidak Depresi";
        }else if(resultValue < 20){
            return "Depresi";
        }else {
            return "Depresi Akut";
        }
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}