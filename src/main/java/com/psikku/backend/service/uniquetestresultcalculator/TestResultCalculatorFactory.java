package com.psikku.backend.service.uniquetestresultcalculator;

import com.psikku.backend.service.answer.AnswerService;
import com.psikku.backend.service.question.QuestionService;
import com.psikku.backend.service.test.TestService;
import com.psikku.backend.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class TestResultCalculatorFactory {

    private final TestService testService;
    private final UserService userService;
    private final AnswerService answerService;
    private final QuestionService questionService;
    private final ResourceLoader resourceLoader;

    @Autowired
    public TestResultCalculatorFactory(TestService testService,
                                       UserService userService,
                                       AnswerService answerService,
                                       QuestionService questionService,
                                       ResourceLoader resourceLoader) {
        this.testService = testService;
        this.userService = userService;
        this.answerService = answerService;
        this.questionService = questionService;
        this.resourceLoader = resourceLoader;
    }

    public UniqueResultTestCalculator getTestCalculator(String testName){
        if(testName.equalsIgnoreCase("cfit")){
            return new CfitResultTestCalculator(userService,answerService,testService,resourceLoader);
        }else if(testName.equalsIgnoreCase("gayabelajar1")){
            return new GayaBelajar1ResultTestCalculator(userService,testService,answerService);
        }else if(testName.equalsIgnoreCase("gayabelajar2")){
            return new GayaBelajar2ResultTestCalculator(answerService,testService,userService);
        }else if(testName.equalsIgnoreCase("bully")){
            return new BullyResultTestCalculator(answerService,questionService,testService,userService);
        }else if(testName.equalsIgnoreCase("eq")) {
            return new EQResultTestCalculator(answerService,questionService,userService,testService);
        }else if(testName.equalsIgnoreCase("bakat")) {
            return new MinatBakatResultTestCalculator(answerService,userService,testService);
        }else if(testName.equalsIgnoreCase("surveykarakter")) {
            return new SurveyKarakterResultTestCalculator();
        }else if(testName.equalsIgnoreCase("covid")) {
            return new EQResultTestCalculator(answerService,questionService,userService,testService);
        }else if(testName.equalsIgnoreCase("stateanxiety")) {
            return new StateAnxietyTestResultCalculator(testService, userService, answerService);
        }else if(testName.equalsIgnoreCase("belanegara")){
            return new BelaNegaraResultTestCalculator(testService, userService, answerService);
        }else if(testName.equalsIgnoreCase("depression")){
            return new DepressionTestResultCalculator(testService, userService, answerService);
        }else if(testName.equalsIgnoreCase("evaluasievent")){
            return new EventSurveyCalculator(testService, userService, answerService);
        }else{
            return null;
        }
    }


}
