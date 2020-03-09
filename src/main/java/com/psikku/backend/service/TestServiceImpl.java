package com.psikku.backend.service;

import com.psikku.backend.dto.*;
import com.psikku.backend.entity.Answer;
import com.psikku.backend.entity.Question;
import com.psikku.backend.entity.Subtest;
import com.psikku.backend.entity.Test;
import com.psikku.backend.repository.AnswerRepository;
import com.psikku.backend.repository.QuestionRepository;
import com.psikku.backend.repository.SubtestRepository;
import com.psikku.backend.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class TestServiceImpl implements TestService{

    @Autowired
    TestRepository testRepository;

    @Autowired
    SubtestRepository subtestRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Transactional
    @Override
    public Test addNewTest(FullTestDto fullTestDto) {
        Test entityTest = convertToTestEntity(fullTestDto);
        try {
            testRepository.save(entityTest);
            for(Subtest subtest:entityTest.getSubtestList()){
                subtestRepository.save(subtest);
                for(Question question: subtest.getQuestionList()){
                    questionRepository.save(question);
                    for(Answer answer: question.getAnswerList()){
                        answerRepository.saveAndFlush(answer);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return entityTest;
    }

    @Override
    public Test convertToTestEntity(FullTestDto fullTestDto) {
        Test test = new Test();
        test.setName(fullTestDto.getName());

        List<SubtestDto> subtestDtoList = fullTestDto.getSubtests();
        List<Subtest> entitySubtestList = new ArrayList<>();
        int subtestNumber = 1;
        for(SubtestDto subtestDto : subtestDtoList){
            Subtest subtest = new Subtest();
            subtest.setId(test.getName() + "_" + subtestNumber++);
            subtest.setGuide(subtestDto.getGuide());
            subtest.setTestType(subtestDto.getTestType());
            List<Question> questionList = new ArrayList<>();
            int questionId = 1;
            for(QuestionDto questionDto:subtestDto.getQuestions()){
                Question question = new Question();
                question.setId(subtest.getId()+"_"+questionId++);
                question.setQuestionContent(questionDto.getQuestionContent());
                questionList.add(question);
                List<Answer> answerList = new ArrayList<>();
                for(AnswerDto answerDto:questionDto.getAnswers()){
                    Answer answer = new Answer();
                    answer.setId(question.getId()+"_"+answerDto.getId());
                    answer.setAnswerContent(answerDto.getAnswerContent());
                    answer.setIsCorrect(answerDto.getIsCorrect());
                    answerList.add(answer);
                }
                question.setAnswerList(answerList);
            }
            subtest.setQuestionList(questionList);
            entitySubtestList.add(subtest);
        }
        test.setSubtestList(entitySubtestList);
        return test;
    }

//    @Override
//    public FullTestDto convertToTestDto(Test test){
//        FullTestDto fullTestDto = new FullTestDto();
//        fullTestDto.setName(test.getName());
//        fullTestDto.setId(test.getId());
//        fullTestDto.setSubtests(new ArrayList<>());
//        for(Subtest subtest : test.getSubtestList()){
//            SubtestDto subtestDto = new SubtestDto();
//            subtestDto.setId(subtest.getId());
//            subtestDto.setTestType(subtest.getTestType());
//            subtestDto.setGuide(subtest.getGuide());
//            subtestDto.setQuestions(new ArrayList<>());
//            for(Question question:subtest.getQuestionList()){
//                QuestionDto questionDto = new QuestionDto();
//                questionDto.setId(question.getId());
//                questionDto.setQuestionContent(question.getQuestionContent());
//                questionDto.setAnswers(new ArrayList<>());
//                for(Answer answer: question.getAnswersList()){
//                    AnswerDto answerDto = new AnswerDto();
//                    answerDto.setId(answer.getId());
//                    answerDto.setAnswerContent(answer.getAnswerContent());
//                    answerDto.setIsCorrect(answer.getIsCorrect());
//                    questionDto.getAnswers().add(answerDto);
//                }
//                subtestDto.getQuestions().add(questionDto);
//            }
//            fullTestDto.getSubtests().add(subtestDto);
//        }
//        return fullTestDto;
//    }


    @Override
    public TestDto convertToTestDto(Test test) {
        TestDto testDto = new TestDto();
        testDto.setId(test.getId());
        testDto.setName(test.getName());
        return testDto;
    }

    @Override
    public List<Test> findAll() {
        return testRepository.findAll();
    }
}
