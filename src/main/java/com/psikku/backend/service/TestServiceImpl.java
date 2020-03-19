package com.psikku.backend.service;

import com.psikku.backend.dto.Test.*;
import com.psikku.backend.entity.*;
import com.psikku.backend.repository.AnswerRepository;
import com.psikku.backend.repository.QuestionRepository;
import com.psikku.backend.repository.SubtestRepository;
import com.psikku.backend.repository.TestRepository;
import com.psikku.backend.exception.TestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        Test findTest = testRepository.findTestByName(entityTest.getName()).orElse(null);
        if(findTest==null){
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
        }else{
            throw new TestException("Test name already exist, choose another name");
        }
    }

    @Override
    public Test findTestById(int id){
        return testRepository.findById(id).orElseThrow(()->new TestException("Test not found"));
    }

    @Override
    public Test findTestByName(String name) { 	
        return  testRepository.findTestByName(name).orElseThrow(()->new TestException("Test with name: "+ name +"not found"));
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
//                System.out.println(questionDto.getId());
                if(questionDto.getId()!=null && Integer.parseInt(questionDto.getId())<0){
                    question.setId(subtest.getId() + "_" + questionDto.getId());
                }else{
                    question.setId(subtest.getId() + "_" + questionId++);
                }
                question.setQuestionContent1(questionDto.getQuestionContent1());
                question.setQuestionContent2(questionDto.getQuestionContent2());
                question.setQuestionContent3(questionDto.getQuestionContent3());
                questionList.add(question);
                List<Answer> answerList = new ArrayList<>();
                if(questionDto.getAnswers() != null){
                    for(AnswerDto answerDto:questionDto.getAnswers()){
                        Answer answer = new Answer();
                        answer.setId(question.getId()+"_"+answerDto.getId());
                        answer.setAnswerContent(answerDto.getAnswerContent());
                        answer.setIsCorrect(answerDto.getIsCorrect());
                        answerList.add(answer);
                    }
                }
                question.setAnswerList(answerList);
            }
            subtest.setQuestionList(questionList);
            entitySubtestList.add(subtest);
        }
        test.setSubtestList(entitySubtestList);
        return test;
    }

    @Override
    public FullTestDto convertToFullTestDto(Test test){
        FullTestDto fullTestDto = new FullTestDto();
        fullTestDto.setName(test.getName());
        fullTestDto.setId(test.getId());
        fullTestDto.setSubtests(new ArrayList<>());
        for(Subtest subtest : test.getSubtestList()){
            SubtestDto subtestDto = new SubtestDto();
            subtestDto.setId(subtest.getId());
            subtestDto.setTestType(subtest.getTestType());
            subtestDto.setGuide(subtest.getGuide());
            subtestDto.setQuestions(new ArrayList<>());
            for(Question question:subtest.getQuestionList()){
                QuestionDto questionDto = new QuestionDto();
                questionDto.setId(question.getId());
                questionDto.setQuestionContent1(question.getQuestionContent1());
                questionDto.setQuestionContent2(question.getQuestionContent2());
                questionDto.setQuestionContent3(question.getQuestionContent3());
                questionDto.setAnswers(new ArrayList<>());
                for(Answer answer: question.getAnswerList()){
                    AnswerDto answerDto = new AnswerDto();
                    answerDto.setId(answer.getId());
                    answerDto.setAnswerContent(answer.getAnswerContent());
                    answerDto.setIsCorrect(answer.getIsCorrect());
                    questionDto.getAnswers().add(answerDto);
                }
                subtestDto.getQuestions().add(questionDto);
            }
            fullTestDto.getSubtests().add(subtestDto);
        }
        return fullTestDto;
    }


    @Override
    public TestDto convertToMinimalTestDto(Test test) {
        TestDto testDto = new TestDto();
        testDto.setId(test.getId());
        testDto.setName(test.getName());
        return testDto;
    }

    @Override
    public List<Test> findAll() {
        return testRepository.findAll();
    }

//_______________________________________________________________________________________________________________
    @Override
    public Subtest findSubtestById(String id) {
        return subtestRepository.findById(id).orElseThrow(()->new TestException("Subtest not found"));
    }

}
