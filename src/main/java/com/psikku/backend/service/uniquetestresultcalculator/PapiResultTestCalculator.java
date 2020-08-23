package com.psikku.backend.service.uniquetestresultcalculator;

import com.psikku.backend.dto.useranswer.SubmittedAnswerDto;
import com.psikku.backend.entity.Answer;
import com.psikku.backend.entity.Test;
import com.psikku.backend.entity.TestResult;
import com.psikku.backend.entity.User;
import com.psikku.backend.service.answer.AnswerService;
import com.psikku.backend.service.question.QuestionService;
import com.psikku.backend.service.test.TestService;
import com.psikku.backend.service.user.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PapiResultTestCalculator implements UniqueResultTestCalculator{

    private final String name;
    private final AnswerService answerService;
    private final QuestionService questionService;
    private final TestService testService;
    private final UserService userService;

    public PapiResultTestCalculator(String name,
                                    @Lazy AnswerService answerService,
                                    @Lazy QuestionService questionService,
                                    @Lazy TestService testService,
                                    @Lazy UserService userService) {
        this.name = name;
        this.answerService = answerService;
        this.questionService = questionService;
        this.testService = testService;
        this.userService = userService;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public TestResult calculateNewResult(List<SubmittedAnswerDto> submittedAnswerDtoList) {

        String testInternalName = submittedAnswerDtoList.get(0).getQuestionId().split("_")[0];
        List<Answer> answersFromDb = answerService.findByIdStartingWith(testInternalName);
        Test test = testService.findTestByInternalName(testInternalName);


        Map<String,Integer> categoryValue = new HashMap<>();
        categoryValue.put("l",0);
        categoryValue.put("i",0);
        categoryValue.put("t",0);
        categoryValue.put("v",0);
        categoryValue.put("s",0);
        categoryValue.put("r",0);
        categoryValue.put("d",0);
        categoryValue.put("c",0);
        categoryValue.put("e",0);
        categoryValue.put("n",0);
        categoryValue.put("a",0);
        categoryValue.put("p",0);
        categoryValue.put("x",0);
        categoryValue.put("b",0);
        categoryValue.put("o",0);
        categoryValue.put("z",0);
        categoryValue.put("k",0);
        categoryValue.put("f",0);
        categoryValue.put("w",0);

        for (SubmittedAnswerDto submittedAnswerDto : submittedAnswerDtoList) {
            for (Answer answerFromDb : answersFromDb) {
                if(submittedAnswerDto.getAnswers() != null && !submittedAnswerDto.getAnswers().isEmpty()){
                    if(answerFromDb.getId().equalsIgnoreCase(submittedAnswerDto.getAnswers().get(0))){
                        categoryValue.compute(answerFromDb.getAnswerCategory(),(cat,val)->val+1);
                    }
                }
            }
        }
        TestResult testResult = new TestResult();
        testResult.setResult(categoryValue.toString());
        testResult.setTest(test);

        return testResult;
    }

    @Override
    public void setResult(String result) {

    }

    @Override
    public String getResult() {
        return null;
    }
}
