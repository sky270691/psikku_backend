package com.psikku.backend.service;

import com.psikku.backend.dto.test.SubmittedAnswerDto;
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
            submittedAnswer.setQuestion(questionRepository.findQuestionByIdEquals(answerDto.getQuestionId()));
            submittedAnswer.setAnswer1(answerDto.getAnswer1());
            submittedAnswer.setAnswer2(answerDto.getAnswer2());
            submittedAnswer.setAnswer3(answerDto.getAnswer3());
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
            answerDto.setAnswer1(answer.getAnswer1());
            answerDto.setAnswer2(answer.getAnswer2());
            answerDto.setAnswer3(answer.getAnswer3());
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
        List<String> answer1ContentList = submittedAnswerList.stream()
                .map(SubmittedAnswer::getAnswer1)
                .collect(Collectors.toList());
        List<String> answer2ContentList = submittedAnswerList.stream()
                .map(SubmittedAnswer::getAnswer2)
                .collect(Collectors.toList());
        List<String> answer3ContentList = submittedAnswerList.stream()
                .map(SubmittedAnswer::getAnswer3)
                .collect(Collectors.toList());

        Map<String, String> result = new HashMap<>();
        for (String answerContent : answer1ContentList) {
            String[] answerContentSplitted = answerContent.split("_");
            String questionId = answerContentSplitted[0] + "_" + answerContentSplitted[1] + "_" + answerContentSplitted[2];
            result.put(questionId, answerContent);
        }
        for (String str : result.keySet()) {
            result.get(str);
            System.out.println(str);
        }

        Map<Subtest, List<String>> subtestIdAnswerListPair = new HashMap<>();
        submittedAnswerList.forEach(x -> {
            String questionId = x.getQuestion().getId();
            String[] questionIdSplit = questionId.split("_");
            String subtestId = questionIdSplit[0] + "_" + questionIdSplit[1];
            Subtest subtest = subtestRepository.findById(subtestId).orElse(new Subtest());
            String answer = x.getAnswer1();
            if (!subtestIdAnswerListPair.containsKey(subtest)) {
                subtestIdAnswerListPair.put(subtest, new ArrayList<>());
            }
            subtestIdAnswerListPair.get(subtest).add(answer);
        });

        System.out.println("subtest id - answer list pair: " + subtestIdAnswerListPair);
    }

}

