package com.psikku.backend.service.uniquetestresultcalculator;

import com.psikku.backend.dto.useranswer.SubmittedAnswerDto;
import com.psikku.backend.entity.Answer;
import com.psikku.backend.entity.Test;
import com.psikku.backend.entity.TestResult;
import com.psikku.backend.entity.User;
import com.psikku.backend.service.answer.AnswerService;
import com.psikku.backend.service.test.TestService;
import com.psikku.backend.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CovidResultTestCalculator implements UniqueResultTestCalculator{

    private final Logger logger;
    private final AnswerService answerService;
    private final UserService userService;
    private final TestService testService;
    private String result;
    private final String name;

    @Autowired
    public CovidResultTestCalculator(AnswerService answerService, UserService userService, TestService testService) {
        this.logger = LoggerFactory.getLogger(CovidResultTestCalculator.class);
        this.answerService = answerService;
        this.userService = userService;
        this.testService = testService;
        this.result = "";
        this.name = "covid";
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public TestResult calculateNewResult(List<SubmittedAnswerDto> submittedAnswerDtoList) {

        // get username from Security Context (from Token)
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userService.findByUsername(username);

        String testName = submittedAnswerDtoList.get(0).getQuestionId().split("_")[0];
        Test test = testService.findTestByInternalName(testName);

        List<Answer> answersFromDb = answerService.findByIdStartingWith(testName);

        int yesAnswerCategory = 0;

        for(SubmittedAnswerDto answerDto : submittedAnswerDtoList){
            for(Answer ansFromDb : answersFromDb){
                if(answerDto.getAnswers().get(0).equalsIgnoreCase(ansFromDb.getId())){
                    yesAnswerCategory += Integer.parseInt(ansFromDb.getAnswerCategory());
                }
            }
        }

        String category = perCategoryPredicate(yesAnswerCategory);
        StringBuilder sb = new StringBuilder();
        sb.append("Resiko Terpapar Covid: ").append(category);

        setResult(sb.toString());

        TestResult testResult = new TestResult();
        testResult.setUser(user);
        testResult.setTest(test);
        testResult.setResultCalculation(getResult());
        testResult.setResult(getResult());

//        testResultRepository.save(testResult);

        logger.info("username: '"+username+"' covid answer calculated successfully");
        return testResult;
    }

    private String perCategoryPredicate(int resultValue){
        if(resultValue < 8){
            return "rendah";
        }else if(resultValue < 15){
            return "sedang";
        }else {
            return "tinggi";
        }
    }

    @Override
    public String getResult() {
        return result;
    }

    @Override
    public void setResult(String result) {
        this.result = result;
    }
}
