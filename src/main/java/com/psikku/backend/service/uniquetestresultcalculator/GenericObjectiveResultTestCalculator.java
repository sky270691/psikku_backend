package com.psikku.backend.service.uniquetestresultcalculator;

import com.psikku.backend.dto.useranswer.SubmittedAnswerDto;
import com.psikku.backend.entity.*;
import com.psikku.backend.exception.TestException;
import com.psikku.backend.service.answer.AnswerService;
import com.psikku.backend.service.test.TestService;
import com.psikku.backend.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenericObjectiveResultTestCalculator implements UniqueResultTestCalculator{

    private String result;

    private final UserService userService;
    private final AnswerService answerService;
    private final String name;
    private final TestService testService;


    @Autowired
    public GenericObjectiveResultTestCalculator(UserService userService,
                                                @Lazy AnswerService answerService,
                                                @Lazy TestService testService) {
        this.userService = userService;
        this.answerService = answerService;
        this.testService = testService;
        this.name = "genericTest";
    }


    @Override
    public TestResult calculateNewResult(List<SubmittedAnswerDto> submittedAnswerDtoList) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);


        String testname = submittedAnswerDtoList.get(0).getQuestionId().split("_")[0];
        List<Answer> answersFromDb = answerService.findByIdStartingWith(testname);
        Test test = testService.findTestByInternalName(testname);


        int correctAnswer = 0;

        List<SubmittedAnswerDto> objectiveOnly = submittedAnswerDtoList.stream()
                .filter(answerDto -> {
                    String[] answerDtoIdSplit = answerDto.getQuestionId().split("_");
                    String subtestId = answerDtoIdSplit[0]+"_"+answerDtoIdSplit[1];
                    for (Subtest subtest : test.getSubtestList()) {
                        if(subtest.getId().equalsIgnoreCase(subtestId) &&
                                subtest.getTestType().equalsIgnoreCase("objective")){
                            return true;
                        }
                    }
                    return false;
                }).collect(Collectors.toList());

        List<SubmittedAnswerDto> userInputOnly = submittedAnswerDtoList.stream()
                .filter(answerDto -> {
                    String[] answerDtoIdSplit = answerDto.getQuestionId().split("_");
                    String subtestId = answerDtoIdSplit[0]+"_"+answerDtoIdSplit[1];
                    for (Subtest subtest : test.getSubtestList()) {
                        if(subtest.getId().equalsIgnoreCase(subtestId) &&
                                subtest.getTestType().equalsIgnoreCase("user-input")){
                            return true;
                        }
                    }
                    return false;
                }).collect(Collectors.toList());


        for (Subtest subtest : test.getSubtestList()) {
            switch(subtest.getTestType()){
                case "objective":
                    correctAnswer += calculateObjective(objectiveOnly,answersFromDb);
                    break;
                case "user-input":
                    correctAnswer += calculateUserInput(userInputOnly,answersFromDb);
                    break;
                default:
                    throw new TestException("Test type not valid");
            }
        }

        double points = (double) correctAnswer / (double)getCountQuestion(test) * 100;


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
        testResult.setResultCalculation("correct answer:"+correctAnswer+",points:"+points);
        testResult.setResult(getResult());
        return testResult;
    }

    private int calculateObjective(List<SubmittedAnswerDto> answerDtoList, List<Answer> answersFromDb){
        int correctAnswer = 0;
        for(SubmittedAnswerDto answerDto : answerDtoList){
            for(Answer ansFromDb : answersFromDb){
                if(ansFromDb.getId().equals(answerDto.getAnswers().get(0))){
                    correctAnswer += ansFromDb.getIsCorrect();
                }
            }
        }
        return correctAnswer;
    }

    private int calculateUserInput(List<SubmittedAnswerDto> answerDtoList, List<Answer> answersFromDb){
        int correctAnswer = 0;
        for(SubmittedAnswerDto answerDto : answerDtoList){
            for(Answer ansFromDb : answersFromDb){
                if(ansFromDb.getAnswerContent().equals(answerDto.getAnswers().get(0))){
                    correctAnswer += ansFromDb.getIsCorrect();
                }
            }
        }
        return correctAnswer;
    }

    private int getCountQuestion(Test test){
        int totalQuestion = (int) test.getSubtestList().stream().flatMap(subtest -> subtest.getQuestionList().stream())
                .count();

        return totalQuestion;
    }

    @Override
    public String getResult() {
        return result;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setResult(String result) {
        this.result = result;
    }

}
