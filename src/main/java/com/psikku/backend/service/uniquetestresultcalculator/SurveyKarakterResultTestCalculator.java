package com.psikku.backend.service.uniquetestresultcalculator;

import com.psikku.backend.dto.useranswer.SubmittedAnswerDto;
import com.psikku.backend.entity.Answer;
import com.psikku.backend.entity.Question;
import com.psikku.backend.entity.TestResult;
import com.psikku.backend.service.answer.AnswerService;
import com.psikku.backend.service.question.QuestionService;
import com.psikku.backend.service.test.TestService;
import com.psikku.backend.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SurveyKarakterResultTestCalculator implements UniqueResultTestCalculator{

    private final Logger logger;
    private final AnswerService answerService;
    private final QuestionService questionService;
    private final TestService testService;
    private final UserService userService;
    private final String name;

    private String testResult;

    public SurveyKarakterResultTestCalculator(AnswerService answerService,
                                              QuestionService questionService,
                                              TestService testService,
                                              UserService userService) {
        this.answerService = answerService;
        this.questionService = questionService;
        this.testService = testService;
        this.userService = userService;
        this.logger = LoggerFactory.getLogger(SurveyKarakterResultTestCalculator.class);
        this.name = "surveykarakter";
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public TestResult calculateNewResult(List<SubmittedAnswerDto> surveyKarakterAnsDtoOnly){

        String[] surveyKarakterAnsSplit = surveyKarakterAnsDtoOnly.get(0).getQuestionId().split("_");
        String testName = surveyKarakterAnsSplit[0];
        List<Answer> surveyKarakterAnsFromDb =
                answerService.findByIdStartingWith(testName);
        List<Question> questionsFromDb = questionService.findByIdStartingWith(testName);

        int toleransi = 0;
        int gotongRoyong = 0;
        int wellbeing = 0;
        int pluralisme = 0;

        double maxCategory = 5;

        // looping thru the submitted answer
        for (SubmittedAnswerDto answerDto : surveyKarakterAnsDtoOnly) {
            for (Answer answerFromDb : surveyKarakterAnsFromDb) {
                if(answerDto.getAnswers().get(0).equals(answerFromDb.getId())){
                    for (Question questionFromDb : questionsFromDb) {
                        if(answerDto.getQuestionId().equals(questionFromDb.getId())){
                            // check the answer category and add the category value (0 or 1) into num of category for outputting
                            if(questionFromDb.getQuestionCategory().equalsIgnoreCase("toleransi")){
                                toleransi += Integer.parseInt(answerFromDb.getAnswerCategory());
                            }else if(questionFromDb.getQuestionCategory().equalsIgnoreCase("gotongroyong")){
                                gotongRoyong += Integer.parseInt(answerFromDb.getAnswerCategory());
                            }else if(questionFromDb.getQuestionCategory().equalsIgnoreCase("wellbeing")){
                                wellbeing += Integer.parseInt(answerFromDb.getAnswerCategory());
                            }else{
                                pluralisme += Integer.parseInt(answerFromDb.getAnswerCategory());
                            }
                        }
                    }
                }
            }
        }

        //outputting this result

        double toleransiPercentage = (double) toleransi / maxCategory * 100;
        double gotongRoyongPercentage = (double) gotongRoyong / maxCategory * 100;
        double wellbeingPercentage = (double) wellbeing / maxCategory * 100;
        double pluralismePercentage = (double) pluralisme / maxCategory * 100;

        StringBuilder sb = new StringBuilder();
//        sb.append("SURVEY KARAKTER").append("\n");
        sb.append("Toleransi:").append((int)toleransiPercentage).append(":").append(perCategoryPredicate(toleransiPercentage)).append(",");
        sb.append("Gotong Royong:").append((int)gotongRoyongPercentage).append(":").append(perCategoryPredicate(gotongRoyongPercentage)).append(",");
        sb.append("Nasionalisme:").append((int)wellbeingPercentage).append(":").append(perCategoryPredicate(wellbeingPercentage)).append(",");
        sb.append("pluralisme:").append((int)pluralismePercentage).append(":").append(perCategoryPredicate(pluralismePercentage));

        setResult(sb.toString());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        TestResult testResult = new TestResult();
        testResult.setUser(userService.findByUsername(username));
        testResult.setTest(testService.findTestByInternalName(testName));
        testResult.setResult(getResult());
        testResult.setResultCalculation("toleransi:"+toleransiPercentage+",gotong royong:"+gotongRoyongPercentage+",nasionalisme:"+wellbeingPercentage+
                ",pluralisme:"+pluralismePercentage);
        logger.info("username: '"+username+"' SURVEYKARAKTER answer calculated successfully");
        return testResult;
    }

    private String perCategoryPredicate(double resultValue){
        if(resultValue < 20){
            return "kurang sekali";
        }else if(resultValue < 40){
            return "kurang";
        }else if(resultValue < 60){
            return "cukup";
        }else if(resultValue < 80){
            return "cukup baik";
        }else {
            return "baik";
        }
    }

    @Override
    public String getResult() {
        return testResult;
    }

    @Override
    public void setResult(String testResult) {
        this.testResult = testResult;
    }
}
