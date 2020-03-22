package com.psikku.backend.service;

import com.psikku.backend.dto.test.*;
import com.psikku.backend.entity.*;
import com.psikku.backend.repository.AnswerRepository;
import com.psikku.backend.repository.QuestionRepository;
import com.psikku.backend.repository.SubtestRepository;
import com.psikku.backend.repository.TestRepository;
import com.psikku.backend.exception.TestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

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
        Optional<Test> findTest = testRepository.findTestByName(entityTest.getName());
        if(!findTest.isPresent()){
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
            return entityTest;
        }else{
            throw new TestException("test name already exist, choose another name");
        }
    }

    @Override
    public Test findTestById(int id){
        return testRepository.findById(id).orElseThrow(()->new TestException("test not found"));
    }

    @Override
    public Test findTestByName(String name) { 	
        return  testRepository.findTestByName(name).orElseThrow(()->new TestException("test with name: "+ name +"not found"));
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
            subtest.setDuration(subtestDto.getDuration());
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
                StringBuilder wholeQuestion= new StringBuilder();
                for(int i = 0;i<questionDto.getQuestionContent().size(); i++){
                    if(i==questionDto.getQuestionContent().size()-1){
                        wholeQuestion.append(questionDto.getQuestionContent().get(i));
                    }else{
                        wholeQuestion.append(questionDto.getQuestionContent().get(i)).append(",");
                    }
                }
                question.setQuestionContent(new String(wholeQuestion));
                questionList.add(question);
                List<Answer> answerList = new ArrayList<>();
                if(questionDto.getAnswers() != null){
                    for(AnswerDto answerDto:questionDto.getAnswers()){
                        Answer answer = new Answer();
                        answer.setId(question.getId()+"_"+answerDto.getId());
                        answer.setAnswerContent(answerDto.getAnswerContent());
                        if(subtestDto.getTestType().equalsIgnoreCase("survey")){
                            answer.setAnswerCategory(answerDto.getAnswerCategory());
                            answer.setIsCorrect(-1);
                        }else{
                            answer.setIsCorrect(answerDto.getIsCorrect());
                        }
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
            subtestDto.setDuration(subtest.getDuration());
            subtestDto.setGuide(subtest.getGuide());
            subtestDto.setQuestions(new ArrayList<>());
            for(Question question:subtest.getQuestionList()){
                QuestionDto questionDto = new QuestionDto();
                questionDto.setId(question.getId());
                questionDto.setQuestionContent(new ArrayList<>());
                String[] questionArray = question.getQuestionContent().split(",");
                for(String tempQuestion : questionArray){
                    questionDto.getQuestionContent().add(tempQuestion);
                }
                questionDto.setAnswers(new ArrayList<>());
                for(Answer answer: question.getAnswerList()){
                    AnswerDto answerDto = new AnswerDto();
                    answerDto.setId(answer.getId());
                    answerDto.setAnswerContent(answer.getAnswerContent());
                    answerDto.setIsCorrect(answer.getIsCorrect());
                    answerDto.setAnswerCategory(answer.getAnswerCategory());
                    questionDto.getAnswers().add(answerDto);
                }
                subtestDto.getQuestions().add(questionDto);

                // shuffle the question to display in the endpoint
                Collections.shuffle(subtestDto.getQuestions());
                // sort the questions to display with the correct order
//                subtestDto.getQuestions().sort((x,y) -> {
//                    String[] xId = x.getId().split("_");
//                    String[] yId = y.getId().split("_");
//                    Integer a = Integer.parseInt(xId[2]);
//                    Integer b = Integer.parseInt(yId[2]);
//                    return a.compareTo(b);
//                });
//                subtestDto.getQuestions().stream().map(QuestionDto::getId)
//                                                    .forEach(System.out::println);
            }
            fullTestDto.getSubtests().add(subtestDto);
            // sort the subtest to display with the correct order
            fullTestDto.getSubtests().sort((x,y)->{
                String[] xId = x.getId().split("_");
                String[] yId = y.getId().split("_");
                Integer a = Integer.parseInt(xId[1]);
                Integer b = Integer.parseInt(yId[1]);
                return a.compareTo(b);
            });
        }
        return fullTestDto;
    }


    @Override
    public MinimalTestDto convertToMinimalTestDto(Test test) {
        MinimalTestDto minimalTestDto = new MinimalTestDto();
        minimalTestDto.setId(test.getId());
        minimalTestDto.setName(test.getName());
        return minimalTestDto;
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
