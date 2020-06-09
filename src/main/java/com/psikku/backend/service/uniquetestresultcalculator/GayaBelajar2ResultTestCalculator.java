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

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GayaBelajar2ResultTestCalculator implements UniqueResultTestCalculator{

    private final Logger logger;

    private final AnswerService answerService;
    private final TestService testService;
    private final UserService userService;

    private String result;

    public GayaBelajar2ResultTestCalculator(AnswerService answerService, TestService testService, UserService userService) {
        this.answerService = answerService;
        this.testService = testService;
        this.userService = userService;
        this.logger = LoggerFactory.getLogger(GayaBelajar2ResultTestCalculator.class);
        this.result = "";
    }

    @Override
    public TestResult calculateNewResult(List<SubmittedAnswerDto> gayaBelajar2AnsOnly) {

        String[] gayaBelajar2DtoSplit = gayaBelajar2AnsOnly.get(0).getQuestionId().split("_");
        String testName = gayaBelajar2DtoSplit[0].toLowerCase();
        List<Answer> gayaBelajar2AnsFromDb =
                answerService.findByIdStartingWith(testName);
        int ce = 0;
        int ac = 0;
        int ro = 0;
        int ae = 0;

        for (SubmittedAnswerDto gb2AnsDto : gayaBelajar2AnsOnly) {
            for (Answer gb2AnsFromDb : gayaBelajar2AnsFromDb) {
                if(gb2AnsFromDb.getId().startsWith(gb2AnsDto.getQuestionId())){
                    for (String gb2AnsDtoEachAnswer : gb2AnsDto.getAnswers()) {
                        if(gb2AnsDtoEachAnswer.equalsIgnoreCase(gb2AnsFromDb.getId())){
                            if(gb2AnsFromDb.getAnswerCategory().equals("ce")){
                                ce++;
                            }else if(gb2AnsFromDb.getAnswerCategory().equals("ac")){
                                ac++;
                            }else if(gb2AnsFromDb.getAnswerCategory().equals("ro")){
                                ro++;
                            }else {
                                ae++;
                            }
                        }
                    }
                }
            }
        }
        Map<String,Integer> resultKeyValue = new HashMap<>();
        resultKeyValue.put("ce",ce);
        resultKeyValue.put("ac",ac);
        resultKeyValue.put("ro",ro);
        resultKeyValue.put("ae",ae);

        List<String> ceAc = new ArrayList<>();
        if(resultKeyValue.get("ce") > resultKeyValue.get("ac")){
            ceAc.add("ce");
        }else if(resultKeyValue.get("ce") < resultKeyValue.get("ac")){
            ceAc.add("ac");
        }

        List<String> roAe = new ArrayList<>();
        if(resultKeyValue.get("ro") > resultKeyValue.get("ae")){
            roAe.add("ro");
        }else if(resultKeyValue.get("ro") < resultKeyValue.get("ae")){
            roAe.add("ae");
        }

        List<String> sortedHighestResultKey =
                resultKeyValue.entrySet().stream()
                                         .sorted((x,y)->y.getValue().compareTo(x.getValue()))
                                         .map(x->x.getKey())
                                         .collect(Collectors.toList());
        String highestCategory = sortedHighestResultKey.get(0);

        StringBuilder sb = new StringBuilder();
        sb.append("Highest Category:").append(highestCategory).append(",");
        sb.append("Combination Category:").append(ceAc.get(0)).append("+").append(roAe.get(0));

        setResult(sb.toString());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        TestResult testResult = new TestResult();
        testResult.setUser(userService.findByUsername(username));
        testResult.setTest(testService.findTestByInternalName(testName));
        testResult.setResult(getResult());
        testResult.setResultCalculation("ce:"+ce+","+"ac:"+ac+","+"ro:"+ro+","+"ae:"+ae);
        logger.info("username: '"+username+"' GB2 answer calculated successfully");
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
