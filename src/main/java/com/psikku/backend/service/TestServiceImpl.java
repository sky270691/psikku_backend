package com.psikku.backend.service;

import com.psikku.backend.dto.AnswerDto;
import com.psikku.backend.dto.QuestionDto;
import com.psikku.backend.dto.SubtestDto;
import com.psikku.backend.dto.TestDto;
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
    public Test addNewTest(TestDto testDto) {
        Test entityTest = convertToTestEntity(testDto);
        try {
            testRepository.save(entityTest);
            for(Subtest subtest:entityTest.getSubtestList()){
                subtestRepository.save(subtest);
                for(Question question: subtest.getQuestionList()){
                    questionRepository.save(question);
                    for(Answer answer: question.getAnswersList()){
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
    public Test convertToTestEntity(TestDto testDto) {
        Test test = new Test();
        test.setName(testDto.getName());

        List<SubtestDto> subtestDtoList = testDto.getSubtests();
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
                question.setAnswersList(answerList);
            }
            subtest.setQuestionList(questionList);
            entitySubtestList.add(subtest);
        }
        test.setSubtestList(entitySubtestList);
        return test;
    }
}
