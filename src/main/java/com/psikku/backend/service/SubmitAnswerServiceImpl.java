package com.psikku.backend.service;

import com.psikku.backend.dto.test.SubmittedAnswerDto;
import com.psikku.backend.entity.Question;
import com.psikku.backend.entity.SubmittedAnswer;
import com.psikku.backend.entity.Subtest;
import com.psikku.backend.entity.User;
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
        List<String> answersContentList = submittedAnswerList.stream()
                .map(x-> x.getQuestion().getId())
                .collect(Collectors.toList());

        Map<String, String> result = new HashMap<>();
        for (SubmittedAnswer z : submittedAnswerList) {
            result.put(z.getQuestion().getId(), z.getAnswers());
        }
        for (String str : result.keySet()) {
            result.get(str);
            System.out.println(str);
        }

        Map<String, List<String>> subtestIdAnswerListPair = new HashMap<>();
        submittedAnswerList.forEach(x -> {
            String questionId = x.getQuestion().getId();
            String[] questionIdSplit = questionId.split("_");
            String subtestId = questionIdSplit[0] + "_" + questionIdSplit[1];
            Subtest subtest = subtestRepository.findById(subtestId).orElse(new Subtest());
            String answer = x.getAnswers();
            if (!subtestIdAnswerListPair.containsKey(subtest.getId())) {
                subtestIdAnswerListPair.put(subtest.getId(), new ArrayList<>());
            }
            subtestIdAnswerListPair.get(subtest.getId()).add(answer);
        });
        System.out.println("subtest id - answer list pair: " + subtestIdAnswerListPair);

        Map<String,List<String>> questionIdAnswerListPair = new HashMap<>();
        submittedAnswerList.forEach((x -> {
            String questionId = x.getQuestion().getId();
            String answer = x.getAnswers();
            if(!questionIdAnswerListPair.containsKey(questionId)){
                questionIdAnswerListPair.put(questionId,new ArrayList<>());
            }
            questionIdAnswerListPair.get(questionId).add(answer);

        }));
        System.out.println("Question id - answer list pair: " + questionIdAnswerListPair);

        List<String> testName = submittedAnswerList.stream()
                                    .map(x->{
                                        String[] questionIdSplit = x.getQuestion().getId().split("_");
                                        return questionIdSplit[0];
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


        for(String test: testName){
            System.out.println("Test name: " + test);
            for(Subtest subTest : subtestList){
                if(subTest.getId().startsWith(test)){
                    String[] subtestIdSplit = subTest.getId().split("_");
                    System.out.println("subtest = " + subtestIdSplit[1]);
                    for(Question question : questionList){
                        if(question.getId().startsWith(subTest.getId())){
                            String[] questionIdSplit = question.getId().split("_");
                            System.out.println("question number: " + questionIdSplit[2]);
                        }
                    }
                }
            }
        }
    }

}

