package com.psikku.backend.service.uniquetestresultcalculator;

import com.psikku.backend.dto.useranswer.SubmittedAnswerDto;
import com.psikku.backend.entity.Answer;
import com.psikku.backend.entity.Test;
import com.psikku.backend.entity.TestResult;
import com.psikku.backend.entity.User;
import com.psikku.backend.exception.TestException;
import com.psikku.backend.repository.AnswerRepository;
import com.psikku.backend.repository.TestRepository;
import com.psikku.backend.repository.TestResultRepository;
import com.psikku.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

public class DepressionResultTestCalculator implements UniqueResultTestCalculator{

    @Autowired
    TestResultRepository testResultRepository;

    @Autowired
    TestRepository testRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AnswerRepository answerRepository;

    private String result;

    @Override
    public TestResult calculateNewResult(List<SubmittedAnswerDto> submittedAnswerDtoList) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByUsername(username);

        String testName = submittedAnswerDtoList.get(0).getQuestionId().split("_")[0];
        Test test = testRepository.findTestByName(testName).orElseThrow(() ->
                new TestException(this.getClass().getSimpleName()+": Test not found"));

        List<Answer> answersFromDb = answerRepository.findByIdStartingWith(testName);

        int answerPoints = 0;

        for (SubmittedAnswerDto answerDto : submittedAnswerDtoList) {
            for (Answer ansFromDb : answersFromDb) {
                if(answerDto.getAnswers().get(0).equals(ansFromDb.getId())){
                    answerPoints += Integer.parseInt(ansFromDb.getAnswerCategory());
                }
            }
        }

        TestResult testResult = new TestResult();
        testResult.setResult(perCategoryPredicate(answerPoints));
        testResult.setUser(user);
        testResult.setTest(test);
        testResultRepository.save(testResult);

        return testResult;
    }

    private String perCategoryPredicate(int resultValue){
        if(resultValue < 40){
            return "Kecemasan Ringan";
        }else if(resultValue < 60){
            return "Kecemasan Sedang";
        }else {
            return "Kecemasan Berat";
        }
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
