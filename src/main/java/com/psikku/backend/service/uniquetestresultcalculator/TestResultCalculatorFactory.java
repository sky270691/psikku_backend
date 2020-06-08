package com.psikku.backend.service.uniquetestresultcalculator;

import com.psikku.backend.service.answer.AnswerService;
import com.psikku.backend.service.test.TestService;
import com.psikku.backend.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestResultCalculatorFactory {

    private final TestService testService;
    private final UserService userService;
    private final AnswerService answerService;

    @Autowired
    public TestResultCalculatorFactory(TestService testService, UserService userService, AnswerService answerService) {
        this.testService = testService;
        this.userService = userService;
        this.answerService = answerService;
    }

    public UniqueResultTestCalculator getTestCalculator(String testName){
        if(testName.equalsIgnoreCase("cfit")){
            return new CfitResultTestCalculator();
        }else if(testName.equalsIgnoreCase("gayabelajar1")){
            return new GayaBelajar1ResultTestCalculator();
        }else if(testName.equalsIgnoreCase("gayabelajar2")){
            return new GayaBelajar2ResultTestCalculator();
        }else if(testName.equalsIgnoreCase("bully")){
            return new BullyResultTestCalculator();
        }else if(testName.equalsIgnoreCase("eq")) {
            return new EQResultTestCalculator();
        }else if(testName.equalsIgnoreCase("bakat")) {
            return new MinatBakatResultTestCalculator();
        }else if(testName.equalsIgnoreCase("surveykarakter")) {
            return new SurveyKarakterResultTestCalculator();
        }else if(testName.equalsIgnoreCase("covid")) {
            return new EQResultTestCalculator();
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
