package com.psikku.backend.service;

import com.psikku.backend.dto.test.SubmittedAnswerDto;
import com.psikku.backend.entity.*;
import com.psikku.backend.exception.TestException;
import com.psikku.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Transactional
    @Override
    public List<SubmittedAnswer> saveUserAnswer(List<SubmittedAnswer> answerList) {
        answerList.forEach(submitAnswerRepository::save);
        return answerList;
    }

    @Override
    public List<SubmittedAnswer> convertToSubmittedAnswerList(List<SubmittedAnswerDto> submittedAnswerDto, User user){
        List<SubmittedAnswer> submittedAnswerList = new ArrayList<>();
        long submittedAnswerId = 1;
        for(SubmittedAnswerDto answerDto : submittedAnswerDto){
            SubmittedAnswer submittedAnswer = new SubmittedAnswer();
            submittedAnswer.setId(user.getId()+ "_" +submittedAnswerId++);
            submittedAnswer.setQuestion(questionRepository.findQuestionByIdEquals(answerDto.getQuestionId()).orElse(null));
            StringBuilder stringBuilder = new StringBuilder();
            for(int i = 0 ; i<answerDto.getAnswers().size();i++){
                String[] answerSplit = answerDto.getAnswers().get(i).split("_");
                if(i==answerDto.getAnswers().size()-1){
                    stringBuilder.append(answerSplit[3]);
                }else{
                    stringBuilder.append(answerSplit[3]).append(",");
                }
            }
            submittedAnswer.setAnswers(new String(stringBuilder));
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

    public String getTestType(SubmittedAnswer submittedAnswer){
        System.out.println(submittedAnswer.getQuestion().getId());
        String[] answerId = submittedAnswer.getQuestion().getId().split("_");
        String subtestId = answerId[0]+"_"+answerId[1];
        String testType = subtestRepository.findById(subtestId).orElse(new Subtest()).getTestType();
        return testType;
    }

    @Override
    public void calculateResultTest(List<SubmittedAnswer> submittedAnswerList) {

        List<Test> testList = submittedAnswerList.stream()
                                    .map(x->{
                                        String[] questionIdSplit = x.getQuestion().getId().split("_");
                                        return testRepository.findTestByName(questionIdSplit[0]).orElseThrow(() -> new TestException("Test Not found"));
                                    })
                                    .distinct()
                                    .collect(Collectors.toList());

        List<Subtest> subtestList = submittedAnswerList.stream()
                                        .map(submittedAnswer -> {
                                            String[] questionIdSplit = submittedAnswer.getQuestion().getId().split("_");
                                            return subtestRepository.findById(questionIdSplit[0]+"_"+questionIdSplit[1]).orElse(null);
                                        })
                                        .distinct()
                                        .collect(Collectors.toList());

        List<Question> questionList = submittedAnswerList.stream()
                                        .map(submittedAnswer -> {
                                            String questionId = submittedAnswer.getQuestion().getId();
                                            return questionRepository.findQuestionByIdEquals(questionId).orElse(null);
                                        })
                                        .distinct()
                                        .collect(Collectors.toList());

        List<String> answer = submittedAnswerList.stream()
                                .map(SubmittedAnswer::getAnswers)
                                .collect(Collectors.toList());



        for(Test test: testList){
            System.out.println("Test name: " + test.getName());
            for(Subtest subTest : subtestList){
                int numOfCorrectAnswer = 0;
                int numOfWrongAnswer = 0;
                if(subTest.getId().startsWith(test.getName())){
                    String[] subtestIdSplit = subTest.getId().split("_");
                    System.out.println("subtest = " + subtestIdSplit[1]);
                    for(SubmittedAnswer submittedAnswer : submittedAnswerList){
                        if(submittedAnswer.getQuestion().getId().startsWith(subTest.getId())){
                            String[] submittedAnswerSplit = submittedAnswer.getAnswers().split(",");
                            for(String string: submittedAnswerSplit){
                                Answer tempAnswer = answerRepository.findById(submittedAnswer.getQuestion().getId()+"_"+string)
                                        .orElseThrow(()->new RuntimeException("no answer found"));
                                switch (tempAnswer.getIsCorrect()){
                                    case 0 : numOfWrongAnswer++;
                                    break;
                                    case 1 : numOfCorrectAnswer++;
                                    break;
                                    default :
                                        System.out.println("no right or wrong answer");
                                    break;
                                }
                            }
                        }

                    }

                }
                System.out.println("total correct number: " + numOfCorrectAnswer);
            }
        }
    }

}

