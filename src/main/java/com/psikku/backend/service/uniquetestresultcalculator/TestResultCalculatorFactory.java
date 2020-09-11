package com.psikku.backend.service.uniquetestresultcalculator;

import com.psikku.backend.exception.AnswerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestResultCalculatorFactory {

    private final List<UniqueResultTestCalculator> uniqueResultTestCalculator;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public TestResultCalculatorFactory(List<UniqueResultTestCalculator> uniqueResultTestCalculator) {
        this.uniqueResultTestCalculator = uniqueResultTestCalculator;
    }

//    public UniqueResultTestCalculator getTestCalculator(String testName){
//        if(testName.equalsIgnoreCase("cfit")){
//            return new CfitResultTestCalculator(userService,answerService,testService,resourceLoader);
//        }else if(testName.equalsIgnoreCase("gayabelajar1")){
//            return new GayaBelajar1ResultTestCalculator(userService,testService,answerService);
//        }else if(testName.equalsIgnoreCase("gayabelajar2")){
//            return new GayaBelajar2ResultTestCalculator(answerService,testService,userService);
//        }else if(testName.equalsIgnoreCase("bully")){
//            return new BullyResultTestCalculator(answerService,questionService,testService,userService);
//        }else if(testName.equalsIgnoreCase("eq")) {
//            return new EQResultTestCalculator(answerService,questionService,userService,testService);
//        }else if(testName.equalsIgnoreCase("bakat")) {
//            return new MinatBakatResultTestCalculator(answerService,userService,testService);
//        }else if(testName.equalsIgnoreCase("surveykarakter")) {
//            return new SurveyKarakterResultTestCalculator(answerService,questionService,testService,userService);
//        }else if(testName.equalsIgnoreCase("covid")) {
//            return new EQResultTestCalculator(answerService,questionService,userService,testService);
//        }else if(testName.equalsIgnoreCase("stateanxiety")) {
//            return new StateAnxietyTestResultCalculator(testService, userService, answerService);
//        }else if(testName.equalsIgnoreCase("belanegara")){
//            return new BelaNegaraResultTestCalculator(testService, userService, answerService);
//        }else if(testName.equalsIgnoreCase("depression")){
//            return new DepressionTestResultCalculator(testService, userService, answerService);
//        }else if(testName.equalsIgnoreCase("evaluasievent")){
//            return new EventSurveyCalculator(testService,userService,answerService);
//        }else{
//            return null;
//        }
//    }

    public UniqueResultTestCalculator getTestCalculator(String testName){
        if(testName.contains("cfit")||testName.equalsIgnoreCase("cfit")){
            return getCalculator("cfit");
        }else if(testName.equalsIgnoreCase("gayabelajar1")){
            return getCalculator("gayabelajar1");
        }else if(testName.equalsIgnoreCase("gayabelajar2")){
            return getCalculator("gayabelajar2");
        }else if(testName.equalsIgnoreCase("bully")){
            return getCalculator("bully");
        }else if(testName.equalsIgnoreCase("eq")) {
            return getCalculator("eq");
        }else if(testName.equalsIgnoreCase("bakat")) {
            return getCalculator("bakat");
        }else if(testName.equalsIgnoreCase("surveykarakter")) {
            return getCalculator("surveykarakter");
        }else if(testName.equalsIgnoreCase("covid")) {
            return getCalculator("covid");
        }else if(testName.equalsIgnoreCase("stateanxiety")) {
            return getCalculator("stateanxiety");
        }else if(testName.equalsIgnoreCase("belanegara")){
            return getCalculator("belanegara");
        }else if(testName.equalsIgnoreCase("depression")){
            return getCalculator("depression");
        }else if(testName.equalsIgnoreCase("evaluasievent")){
            return getCalculator("evaluasievent");
        }else if(testName.equalsIgnoreCase("riasec")){
            return getCalculator("riasec");
        }else if(testName.equalsIgnoreCase("papi")){
            return getCalculator("papi");
        }else{
            logger.error("can't find result calculator for the testname: "+testName);
            throw new AnswerException("answer is not valid, please re-check your answer");
        }
    }

    private UniqueResultTestCalculator getCalculator(String name){
        return uniqueResultTestCalculator.stream()
                .filter(calculator -> calculator.getName().equalsIgnoreCase(name))
                .findAny()
                .orElseThrow(()-> new AnswerException("answer is not valid"));
    }


}
