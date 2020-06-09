package com.psikku.backend.service.uniquetestresultcalculator;

import com.psikku.backend.dto.useranswer.SubmittedAnswerDto;
import com.psikku.backend.entity.Answer;
import com.psikku.backend.entity.TestResult;
import com.psikku.backend.repository.AnswerRepository;
import com.psikku.backend.repository.TestRepository;
import com.psikku.backend.repository.TestResultRepository;
import com.psikku.backend.repository.UserRepository;
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
public class GayaBelajar1ResultTestCalculator implements UniqueResultTestCalculator {

    private final Logger logger;
    private final UserService userService;
    private final TestService testService;
    private final AnswerService answerService;

    private String result;

    public GayaBelajar1ResultTestCalculator(UserService userService, TestService testService, AnswerService answerService) {
        this.userService = userService;
        this.testService = testService;
        this.answerService = answerService;
        this.logger = LoggerFactory.getLogger(GayaBelajar1ResultTestCalculator.class);
        this.result = "";
    }

    @Override
    public TestResult calculateNewResult(List<SubmittedAnswerDto> gayaBelajar1Only) {

        String[] gayaBelajar1QuestionIdSplit = gayaBelajar1Only.get(0).getQuestionId().split("_");
        String testName = gayaBelajar1QuestionIdSplit[0].toLowerCase();
        List<Answer> gayaBelajar1AnswerFromDb = answerService.findByIdStartingWith(testName);

        int visual = 0;
        int auditori = 0;
        int kinestetik = 0;

        for (Answer answerFromDb : gayaBelajar1AnswerFromDb) {
            for (SubmittedAnswerDto answerDto : gayaBelajar1Only) {
                if(answerFromDb.getId().startsWith(answerDto.getQuestionId())){
                    for (String matchQuestionAnswers : answerDto.getAnswers()) {
                        if(answerFromDb.getId().equals(matchQuestionAnswers)){
                            if(answerFromDb.getAnswerCategory().equals("visual")){
                                visual++;
                            }else if(answerFromDb.getAnswerCategory().equals("auditori")){
                                auditori++;
                            }else{
                                kinestetik++;
                            }
                        }
                    }
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("visual:").append(visual).append(",");
        sb.append("auditori:").append(auditori).append(",");
        sb.append("kinestetik:").append(kinestetik);
        setResult(sb.toString());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        TestResult testResult = new TestResult();
        testResult.setUser(userService.findByUsername(username));
        testResult.setTest(testService.findTestByInternalName(testName.toLowerCase()));
        testResult.setResult(getResult());
        testResult.setResultCalculation(getResult());
        logger.info("username: '"+username+"' GB1 answer calculated successfully");
        return testResult;
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
