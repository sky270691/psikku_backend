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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class discResultTestCalculator implements UniqueResultTestCalculator {

    private String result;
    private final TestService testService;
    private final UserService userService;
    private final AnswerService answerService;
    private final String name;
    private final ResourceLoader resourceLoader;
    private final Logger logger;

    @Autowired
    public discResultTestCalculator(TestService testService,
                                    UserService userService,
                                    AnswerService answerService,
                                    ResourceLoader resourceLoader) {
        this.testService = testService;
        this.userService = userService;
        this.answerService = answerService;
        this.resourceLoader = resourceLoader;
        this.logger = LoggerFactory.getLogger(this.getClass());
        result = "";
        this.name = "disc";
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public TestResult calculateNewResult(List<SubmittedAnswerDto> submittedAnswerDtoList) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        String testname = submittedAnswerDtoList.get(0).getQuestionId().split("_")[0];
        List<Answer> answersFromDb = answerService.findByIdStartingWith(testname);
        Test test = testService.findTestByInternalName(testname);

        Map<String,Integer> mMap = new HashMap<>();
        Map<String,Integer> lMap = new HashMap<>();

        for (SubmittedAnswerDto submittedAnswerDto : submittedAnswerDtoList) {
            if(submittedAnswerDto.getAnswers().size() > 1) {
                String mId = submittedAnswerDto.getAnswers().get(0);
                String lId = submittedAnswerDto.getAnswers().get(1);
                for (Answer answerFromDb : answersFromDb) {
                    if (answerFromDb.getId().equalsIgnoreCase(mId)) {
                        String ans = answerFromDb.getAnswerCategory().split("\\|")[0];
                        mMap.merge(ans, 1, Integer::sum);
                    }
                    if (answerFromDb.getId().equalsIgnoreCase(lId)) {
                        String ans = answerFromDb.getAnswerCategory().split("\\|")[1];
                        lMap.merge(ans, 1, Integer::sum);
                    }
                }
            }
        }

        StringBuilder sb = new StringBuilder();

        sb.append(mMap);
        sb.append("|");
        sb.append(lMap);
        result = sb.toString();
        TestResult testResult = new TestResult();
        testResult.setTest(test);
        testResult.setUser(user);
        testResult.setResult(getResult());
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
