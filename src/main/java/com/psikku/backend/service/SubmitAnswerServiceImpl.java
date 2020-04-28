package com.psikku.backend.service;

import com.psikku.backend.dto.test.SubmittedAnswerDto;
import com.psikku.backend.entity.*;
import com.psikku.backend.exception.TestException;
import com.psikku.backend.repository.*;
import com.psikku.backend.service.uniquetestresultcalculator.CfitResultTestCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SubmitAnswerServiceImpl implements SubmitAnswerService {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    SubmitAnswerRepository submitAnswerRepository;

    @Autowired
    SubtestRepository subtestRepository;

    @Autowired
    TestRepository testRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    TestResultRepository testResultRepository;

    @Autowired
    CfitResultTestCalculator cfitResultTestCalculator;

    @Transactional
    @Override
    public List<SubmittedAnswer> saveUserAnswer(List<SubmittedAnswer> answerList) {
        answerList.forEach(submitAnswerRepository::save);
        return answerList;
    }

    @Override
    public List<SubmittedAnswer> convertToSubmittedAnswerList(List<SubmittedAnswerDto> submittedAnswerDto, User user){
        List<SubmittedAnswer> submittedAnswerList = new ArrayList<>();
//        long submittedAnswerId = 1;
        for(SubmittedAnswerDto answerDto : submittedAnswerDto){
            SubmittedAnswer submittedAnswer = new SubmittedAnswer();
//            submittedAnswer.setId(user.getId()+ "_" +submittedAnswerId++);
            submittedAnswer.setQuestion(questionRepository.findQuestionByIdEquals(answerDto.getQuestionId()).orElse(null));
            StringBuilder stringBuilder = new StringBuilder();

            String[] questionIdSplit = answerDto.getQuestionId().split("_");
            String subtestId = questionIdSplit[0]+"_"+questionIdSplit[1];
            String testType = subtestRepository.findById(subtestId).orElseThrow(()-> new RuntimeException("Subtest Not Found")).getTestType();

            if(testType.equalsIgnoreCase("user_input_string") || testType.equalsIgnoreCase("user_input_number")){
                submittedAnswer.setAnswers(answerDto.getAnswers().get(0));
            }else{
                for(int i = 0 ; i<answerDto.getAnswers().size();i++){
                    String[] answerSplit = answerDto.getAnswers().get(i).split("_");
                    if(i==answerDto.getAnswers().size()-1){
                        stringBuilder.append(answerSplit[3]);
                    }else{
                        stringBuilder.append(answerSplit[3]).append(",");
                    }
                }
                submittedAnswer.setAnswers(new String(stringBuilder));
            }
            submittedAnswer.setUser(user);
            submittedAnswerList.add(submittedAnswer);
        }
        return submittedAnswerList;
    }

    @Override
    public List<SubmittedAnswerDto> convertToSubmittedAnswerDtoList(List<SubmittedAnswer> submittedAnswerList){
        List<SubmittedAnswerDto> submittedAnswerDtoList = new ArrayList<>();
        for(SubmittedAnswer answer: submittedAnswerList){
            SubmittedAnswerDto answerDto = new SubmittedAnswerDto();
            answerDto.setId(answer.getId());
            String[] answerEntitySplit = answer.getAnswers().split(",");
            answerDto.setAnswers(new ArrayList<>());
            for (String answerEntSplit : answerEntitySplit) {
                answerDto.getAnswers().add(answerEntSplit);
            }
            answerDto.setQuestionId(answer.getQuestion().getId());
            answerDto.setUserId(answer.getUser().getId());
            submittedAnswerDtoList.add(answerDto);
        }
        return submittedAnswerDtoList;
    }

    @Transactional
    public String getTestType(SubmittedAnswer submittedAnswer){
        System.out.println(submittedAnswer.getQuestion().getId());
        String[] answerId = submittedAnswer.getQuestion().getId().split("_");
        String subtestId = answerId[0]+"_"+answerId[1];
        String testType = subtestRepository.findById(subtestId).orElse(new Subtest()).getTestType();
        return testType;
    }

    // calculate result test conventional
//    @Transactional
//    @Override
//    public void calculateResultTest(List<SubmittedAnswer> submittedAnswerList) {
//
//        List<Test> testList = submittedAnswerList.stream()
//                .map(x -> {
//                    String[] questionIdSplit = x.getQuestion().getId().split("_");
////                    return testRepository.findTestByName(questionIdSplit[0]).orElseThrow(() -> new TestException("Test Not found"));
//                    return questionIdSplit[0];
//                })
//                .distinct()
//                .map(testName-> testRepository.findTestByName(testName).orElseThrow(() -> new TestException("Test Not found")))
//                .collect(Collectors.toList());
//
//        User user = submittedAnswerList.stream()
//                        .map(SubmittedAnswer::getUser)
//                        .findFirst().orElse(null);
//
//        int totalNumOfCorrectAnswer = 0;
//        Map<Integer, Integer> surveyCategoryPair = new HashMap<>();
//        Map<Integer, Integer> totalSurveyCategoryPair = new HashMap<>();
//
//        for (Test test : testList) {
//            System.out.println("Test name: " + test.getName());
//            for (Subtest subTest : test.getSubtestList()) {
//
//                // variable init for several subtest type purposes
//                int numOfCorrectAnswer = 0;
//                int counter = 0;
//
//                if (subTest.getId().startsWith(test.getName())) {
//                    String[] subtestIdSplit = subTest.getId().split("_");
//                    System.out.println("subtest = " + subtestIdSplit[1]);
//                    for (SubmittedAnswer submittedAnswer : submittedAnswerList) {
//                        if (submittedAnswer.getQuestion().getId().startsWith(subTest.getId())) {
//                            String[] submittedAnswerSplit = submittedAnswer.getAnswers().split(",");
//                            int numOfAnswers = 0;
//                            for (String tempSubmittedAnswer : submittedAnswerSplit) {
//                                Answer tempAnswer = null;
//                                if(!((subTest.getTestType().equalsIgnoreCase("user_input_string"))||
//                                        subTest.getTestType().equalsIgnoreCase("user_input_number"))) {
//                                    tempAnswer = answerRepository.findById(submittedAnswer.getQuestion().getId() + "_" + tempSubmittedAnswer)
//                                            .orElseThrow(() -> new RuntimeException("no answer found"));
//                                }else{
//                                    String questionId = submittedAnswer.getQuestion().getId();
//                                    Question tempQuestion = questionRepository.findQuestionByIdEquals(questionId).orElseThrow(()-> new RuntimeException("question not found"));
//                                    tempAnswer = tempQuestion.getAnswerList().get(0);
//                                }
//                                switch (subTest.getTestType()) {
//                                    case "right_or_wrong":
//                                        if (tempAnswer.getIsCorrect() == 1) {
//                                            numOfCorrectAnswer++;
//                                        }
//                                        break;
//                                    case "two_answers":
//                                        if (tempAnswer.getIsCorrect() == 1 && numOfAnswers < 1) {
//                                            numOfAnswers++;
//                                        } else if (tempAnswer.getIsCorrect() == 1 && numOfAnswers == 1) {
//                                            numOfCorrectAnswer++;
//                                        }
//                                        break;
////                                    case "three_answers":
////                                        if (tempAnswer.getIsCorrect() == 1 && numOfAnswers < 2) {
////                                            numOfAnswers++;
////                                        } else if (tempAnswer.getIsCorrect() == 1 && numOfAnswers == 2) {
////                                            numOfCorrectAnswer++;
////                                        }
////                                        break;
//                                    case "survey":
////                                        List<Integer> answerCategoryList = subTest.getQuestionList().stream()
////                                                                        .flatMap(question -> question.getAnswerList().stream())
////                                                                        .map(Answer::getAnswerCategory)
////                                                                        .distinct()
////                                                                        .collect(Collectors.toList());
////                                        int maxCategory = Collections.max(answerCategoryList);
////                                        for(int i = maxCategory;i>0;i--){
////                                            if(tempAnswer.getAnswerCategory()==i){
////                                                if(surveyCategoryPair.containsKey(i)){
////                                                    counter = surveyCategoryPair.get(i)+1;
////                                                }else{
////                                                    counter = 1;
////                                                }
////                                                surveyCategoryPair.put(i,counter);
////                                            }
////                                        }
//                                        break;
//                                    case "user_input_string":
//                                    case "user_input_number":
//                                        if(tempSubmittedAnswer.equalsIgnoreCase(tempAnswer.getAnswerContent())){
//                                            numOfCorrectAnswer++;
//                                        }
//                                        break;
//                                    default:
//                                        break;
//                                }
//                            }
//                        }
//                    }
//                    System.out.println("total correct number: " + numOfCorrectAnswer);
//                    totalNumOfCorrectAnswer += numOfCorrectAnswer;
//                    if(subTest.getTestType().equalsIgnoreCase("survey")){
//                        for(int key : surveyCategoryPair.keySet()){
//                            totalSurveyCategoryPair.put(key,surveyCategoryPair.get(key));
//                        }
//                        System.out.println("List of Survey answer: "+surveyCategoryPair);
//                    }
//                }
//            }
//            System.out.println("===================================================================================");
//            TestResult testResult = new TestResult();
//            testResult.setTest(test);
//            System.out.println("total correct number: " + totalNumOfCorrectAnswer);
//            if(test.getIsSurvey()){
//                testResult.setSurveyCategoryAnswer(surveyCategoryPair.toString());
//                System.out.println("List of Survey answer: "+surveyCategoryPair);
//            }else{
//                testResult.setTotalRightAnswer(totalNumOfCorrectAnswer);
//            }
//            testResult.setUser(user);
//            testResultRepository.save(testResult);
//        }
//    }

    public String calculateResultTest(List<SubmittedAnswerDto> submittedAnswerDtoList){

        // get cfit3 answer only
        List<SubmittedAnswerDto> cfitAnswer = submittedAnswerDtoList.stream()
                .filter(answer -> answer.getQuestionId().contains("cfit3"))
                .collect(Collectors.toList());

        // get gaya belajar1 test only
        List<SubmittedAnswerDto> gayaBelajar1Only =
                submittedAnswerDtoList.stream()
                        .filter(answerDto -> answerDto.getQuestionId().contains("belajar1".toLowerCase()))
                        .collect(Collectors.toList());


        cfitResultTestCalculator.calculateNewResult(cfitAnswer);
        return cfitResultTestCalculator.getResult();
    }
}

