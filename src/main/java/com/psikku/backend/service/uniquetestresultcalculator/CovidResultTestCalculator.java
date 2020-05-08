package com.psikku.backend.service.uniquetestresultcalculator;

import com.psikku.backend.dto.useranswer.SubmittedAnswerDto;
import com.psikku.backend.entity.Answer;
import com.psikku.backend.entity.Test;
import com.psikku.backend.entity.TestResult;
import com.psikku.backend.entity.User;
import com.psikku.backend.repository.AnswerRepository;
import com.psikku.backend.repository.TestRepository;
import com.psikku.backend.repository.TestResultRepository;
import com.psikku.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CovidResultTestCalculator implements UniqueResultTestCalculator{

    public final Logger logger = LoggerFactory.getLogger(CovidResultTestCalculator.class);

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private TestResultRepository testResultRepository;

    private String result;

    @Override
    public TestResult calculateNewResult(List<SubmittedAnswerDto> submittedAnswerDtoList) {

        // get username from Security Context (from Token)
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userRepository.findUserByUsername(username);

        String testName = submittedAnswerDtoList.get(0).getQuestionId().split("_")[0];
        Test test = testRepository.findTestByName(testName).orElseThrow(()-> {
            logger.error("Finding test error");
            return new RuntimeException("Test not Found");
        });

        List<Answer> answersFromDb = answerRepository.findByIdStartingWith(testName);

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
        sb.append("resiko:").append(category);

        setResult(sb.toString());

        TestResult testResult = new TestResult();
        testResult.setUser(user);
        testResult.setTest(test);
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
