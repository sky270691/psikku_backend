package com.psikku.backend.service.uniquetestresultcalculator;

import com.psikku.backend.dto.useranswer.SubmittedAnswerDto;
import com.psikku.backend.entity.*;
import com.psikku.backend.exception.TestException;
import com.psikku.backend.repository.AnswerRepository;
import com.psikku.backend.repository.TestRepository;
import com.psikku.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class GenericResultTestCalculator implements UniqueResultTestCalculator{

    private String result;

    UserRepository userRepository;
    AnswerRepository answerRepository;
    TestRepository testRepository;

    @Autowired
    public GenericResultTestCalculator(UserRepository userRepository, AnswerRepository answerRepository, TestRepository testRepository) {
        this.userRepository = userRepository;
        this.answerRepository = answerRepository;
        this.testRepository = testRepository;
    }

    @Override
    public TestResult calculateNewResult(List<SubmittedAnswerDto> submittedAnswerDtoList) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByUsername(username);
        String testname = submittedAnswerDtoList.get(0).getQuestionId().split("_")[0];
        List<Answer> answersFromDb = answerRepository.findByIdStartingWith(testname);
        Test test = testRepository.findTestByInternalName(testname).orElseThrow(()->new TestException(getClass().getSimpleName()+": Test Not Found"));

        //Todo
        // Real implementation
//        int correctAnswer = 0;
//
//        for(SubmittedAnswerDto answerDto : submittedAnswerDtoList){
//            for(Answer ansFromDb : answersFromDb){
//                if(ansFromDb.getId().equals(answerDto.getAnswers().get(0))){
//                    correctAnswer += ansFromDb.getIsCorrect();
//                }
//            }
//        }
//
//        double points = (double) correctAnswer / (double)getCountQuestion(test) * 100;

        //Todo
        // fake implementation
        Random random = new Random();
        int correctAnswer = random.nextInt(7)+31;
        int points = correctAnswer*2;

        StringBuilder sb = new StringBuilder();

        sb.append("Jumlah Jawaban Benar:");
        sb.append(correctAnswer);
        sb.append(",");
        sb.append("Nilai Test:");
        sb.append(points);
        setResult(sb.toString());

        TestResult testResult = new TestResult();
        testResult.setTest(test);
        testResult.setUser(user);
        testResult.setResult(getResult());
        return testResult;
    }

    private int getCountQuestion(Test test){
        int totalQuestion = (int) test.getSubtestList().stream().flatMap(subtest -> subtest.getQuestionList().stream())
                                                                .count();

        return totalQuestion;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
