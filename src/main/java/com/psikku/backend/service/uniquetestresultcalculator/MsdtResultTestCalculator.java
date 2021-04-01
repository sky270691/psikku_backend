package com.psikku.backend.service.uniquetestresultcalculator;

import com.psikku.backend.dto.useranswer.SubmittedAnswerDto;
import com.psikku.backend.entity.Answer;
import com.psikku.backend.entity.Test;
import com.psikku.backend.entity.TestResult;
import com.psikku.backend.service.answer.AnswerService;
import com.psikku.backend.service.question.QuestionService;
import com.psikku.backend.service.test.TestService;
import com.psikku.backend.service.user.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MsdtResultTestCalculator implements UniqueResultTestCalculator{

    private final String name;
    private final AnswerService answerService;
    private final QuestionService questionService;
    private final TestService testService;
    private final UserService userService;

    public MsdtResultTestCalculator(
                                    @Lazy AnswerService answerService,
                                    @Lazy QuestionService questionService,
                                    @Lazy TestService testService,
                                    @Lazy UserService userService) {

        this.answerService = answerService;
        this.questionService = questionService;
        this.testService = testService;
        this.userService = userService;
        this.name = "msdt";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public TestResult calculateNewResult(List<SubmittedAnswerDto> submittedAnswerDtoList) {

        String testInternalName = submittedAnswerDtoList.get(0).getQuestionId().split("_")[0];
        List<Answer> answersFromDb = answerService.findByIdStartingWith(testInternalName);
        Test test = testService.findTestByInternalName(testInternalName);

        Map<Integer,Integer> koreksi = new HashMap<>();
        koreksi.put(1,1);
        koreksi.put(2,2);
        koreksi.put(3,1);
        koreksi.put(4,0);
        koreksi.put(5,3);
        koreksi.put(6,-1);
        koreksi.put(7,0);
        koreksi.put(8,-4);

        Map<String,Integer> aPair = new HashMap<>();
        Map<String,Integer> bPair = new HashMap<>();

        if(!submittedAnswerDtoList.isEmpty()) {
            for (SubmittedAnswerDto submittedAnswerDto : submittedAnswerDtoList) {
                if(!submittedAnswerDto.getAnswers().isEmpty()) {
                    for (Answer answerFromDb : answersFromDb) {
                        if(submittedAnswerDto.getAnswers().get(0).equalsIgnoreCase(answerFromDb.getId())){
                            if(answerFromDb.getAnswerCategory().contains("a")){
                                if(!aPair.containsKey(answerFromDb.getAnswerCategory())){
                                    aPair.put(answerFromDb.getAnswerCategory(),1);
                                }else{
                                    int currentValue = aPair.get(answerFromDb.getAnswerCategory());
                                    aPair.put(answerFromDb.getAnswerCategory(),currentValue+1);
                                }
                            }else{
                                if(!bPair.containsKey(answerFromDb.getAnswerCategory())){
                                    bPair.put(answerFromDb.getAnswerCategory(),1);
                                }else{
                                    int currentValue = bPair.get(answerFromDb.getAnswerCategory());
                                    bPair.put(answerFromDb.getAnswerCategory(),currentValue+1);
                                }
                            }
                        }
                    }
                }
            }
        }

        int a = 1;
        int b = 2;
        int c = 1;
        int d = 0;
        int e = 3;
        int f = -1;
        int g = 0;
        int h = -4;

        if(aPair.get("1a") != null && bPair.get("1b") != null){
            a = a + aPair.get("1a") + bPair.get("1b");
        }else if(aPair.get("1a") == null && bPair.get("1b") != null){
            a = a + bPair.get("1b");
        }else if(aPair.get("1a") != null && bPair.get("1b") == null){
            a = a + aPair.get("1a");
        }

        if(aPair.get("2a") != null && bPair.get("2b") != null){
            b = b + aPair.get("2a") + bPair.get("1b");
        }else if(aPair.get("2a") == null && bPair.get("2b") != null){
            b = b + bPair.get("2b");
        }else if(aPair.get("2a") != null && bPair.get("2b") == null){
            b = b + aPair.get("2a");
        }

        if(aPair.get("3a") != null && bPair.get("3b") != null){
            c = c + aPair.get("3a") + bPair.get("3b");
        }else if(aPair.get("3a") == null && bPair.get("3b") != null){
            c = c + bPair.get("3b");
        }else if(aPair.get("3a") != null && bPair.get("3b") == null){
            c = c + aPair.get("3a");
        }

        if(aPair.get("4a") != null && bPair.get("4b") != null){
            d = d + aPair.get("4a") + bPair.get("4b");
        }else if(aPair.get("4a") == null && bPair.get("4b") != null){
            d = d + bPair.get("4b");
        }else if(aPair.get("4a") != null && bPair.get("4b") == null){
            d = d + aPair.get("4a");
        }

        if(aPair.get("5a") != null && bPair.get("5b") != null){
            e = e + aPair.get("5a") + bPair.get("5b");
        }else if(aPair.get("5a") == null && bPair.get("5b") != null){
            e = e + bPair.get("5b");
        }else if(aPair.get("5a") != null && bPair.get("5b") == null){
            e = e + aPair.get("5a");
        }

        if(aPair.get("6a") != null && bPair.get("6b") != null){
            f = f + aPair.get("6a") + bPair.get("6b");
        }else if(aPair.get("6a") == null && bPair.get("6b") != null){
            f = f + bPair.get("6b");
        }else if(aPair.get("6a") != null && bPair.get("6b") == null){
            f = f + aPair.get("6a");
        }

        if(aPair.get("7a") != null && bPair.get("7b") != null){
            g = g + aPair.get("7a") + bPair.get("7b");
        }else if(aPair.get("7a") == null && bPair.get("7b") != null){
            g = g + bPair.get("7b");
        }else if(aPair.get("7a") != null && bPair.get("7b") == null){
            g = g + aPair.get("7a");
        }


        if(aPair.get("8a") != null && bPair.get("8b") != null){
            h = h + aPair.get("8a") + bPair.get("8b");
        }else if(aPair.get("8a") == null && bPair.get("8b") != null){
            h = h + bPair.get("8b");
        }else if(aPair.get("8a") != null && bPair.get("8b") == null){
            h = h + aPair.get("8a");
        }

        int to = c+d+g+h;
        int ro = b+d+f+h;
        int ee = e+f+g+h;
        int o = a;

        TestResult testResult = new TestResult();
        testResult.setResult("a="+a+";b="+b+";c="+c+";d="+d+";e="+e+";f="+f+";g="+g+";h="+h+
                "|"+"to="+to+";ro="+ro+";e="+ee+";o="+o);
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
