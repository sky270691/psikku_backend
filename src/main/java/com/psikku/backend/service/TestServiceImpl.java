package com.psikku.backend.service;

import com.psikku.backend.dto.test.*;
import com.psikku.backend.dto.test.MinimalTestDto;
import com.psikku.backend.entity.*;
import com.psikku.backend.repository.*;
import com.psikku.backend.exception.TestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class TestServiceImpl implements TestService{

    @Autowired
    TestRepository testRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SurveyCategoryRepository surveyCategoryRepository;

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
            // check if the question is survey or not
//            if(entityTest.getIsSurvey()){
//                for(SurveyCategory surveyCategory : entityTest.getSurveyCategoryList()){
//                    surveyCategoryRepository.save(surveyCategory);
//                }
//            }
            for(Subtest subtest:entityTest.getSubtestList()){
                subtestRepository.save(subtest);
                for(Question question: subtest.getQuestionList()){
                    questionRepository.save(question);
                    for(Answer answer: question.getAnswerList()){
                        answerRepository.save(answer);
                    }
                }
            }
            return entityTest;
        }else{
            throw new TestException("test name already exist, choose another name");
        }
    }

    @Override
    @Transactional
    public Test findTestById(int id){
        return testRepository.findById(id).orElseThrow(()->new TestException("test not found"));
    }

    @Override
    @Transactional
    public Test findTestByName(String name) { 	
        return  testRepository.findTestByName(name).orElseThrow(()->new TestException("test with name: "+ name +"not found"));
    }

    @Override
    public Test convertToTestEntity(FullTestDto fullTestDto) {
        Test test = new Test();
        test.setName(fullTestDto.getName());
        test.setDescription(fullTestDto.getDescription());
//        List<Test> testList = testRepository.findAll();
//        int maxTestId = testList.stream()
//                                .map(Test::getId)
//                                .max(Comparator.naturalOrder())
//                                .orElse(0);

//        if(fullTestDto.getIsSurvey()){
//            test.setIsSurvey(true);
//            test.setSurveyCategoryList(new ArrayList<>());
//            for(SurveyCategoryDto surveyCategoryDto : fullTestDto.getSurveyCategoryDto()){
//                SurveyCategory tempEntitySurveyCategory = new SurveyCategory();
//                tempEntitySurveyCategory.setId((maxTestId+1)+"_"+surveyCategoryDto.getCategoryNumber());
//                tempEntitySurveyCategory.setCategory(surveyCategoryDto.getCategory());
//                test.getSurveyCategoryList().add(tempEntitySurveyCategory);
//            }
//        }
        List<SubtestDto> subtestDtoList = fullTestDto.getSubtests();
        List<Subtest> entitySubtestList = new ArrayList<>();
        int subtestNumber = 1;
        for(SubtestDto subtestDto : subtestDtoList){
            Subtest subtest = new Subtest();
            subtest.setId(test.getName() + "_" + subtestNumber++);

            // list of guide convert to whole string to save in db
            StringBuilder guideSb = new StringBuilder();
            for(int i = 0; i<subtestDto.getGuides().size(); i++){
                if(i == subtestDto.getGuides().size()-1){
                    guideSb.append(subtestDto.getGuides().get(i));
                }else{
                    guideSb.append(subtestDto.getGuides().get(i)).append(";");
                }
            }

            subtest.setGuide(guideSb.toString());
            subtest.setTestType(subtestDto.getTestType());
            subtest.setDuration(subtestDto.getDuration());
            List<Question> questionList = new ArrayList<>();
            int questionId = 0;
            for(QuestionDto questionDto:subtestDto.getQuestions()){
                Question question = new Question();
                if(fullTestDto.getIsSurvey()){
                    question.setQuestionCategory(questionDto.getQuestionCategory());
                }else{
                    question.setQuestionCategory("");
                }
//                System.out.println(questionDto.getId());
                if(questionDto.getId() == null){
                    question.setId(subtest.getId() + "_" + questionId++);
                }else{
                    question.setId(questionDto.getId());
                }
                StringBuilder wholeQuestion= new StringBuilder();
                for(int i = 0;i<questionDto.getQuestionContent().size(); i++){
                    if(i==questionDto.getQuestionContent().size()-1){
                        wholeQuestion.append(questionDto.getQuestionContent().get(i));
                    }else{
                        wholeQuestion.append(questionDto.getQuestionContent().get(i)).append(";");
                    }
                }
                question.setQuestionContent(new String(wholeQuestion));


//                if(subtestDto.getTestType().equalsIgnoreCase("survey")){
//                    question.setQuestionCategory(fullTestDto.getId()+"_"+questionDto.getQuestionCategory());
//                }else{
//                    question.setQuestionCategory(fullTestDto.getId()+"_"+-1);
//                }
                questionList.add(question);
                List<Answer> answerList = new ArrayList<>();
                if(questionDto.getAnswers() != null){
                    for(AnswerDto answerDto:questionDto.getAnswers()){
                        Answer answer = new Answer();
                        answer.setId(question.getId()+"_"+answerDto.getId());
                        answer.setAnswerContent(answerDto.getAnswerContent());
                        answer.setAnswerCategory(answerDto.getAnswerCategory());
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
        fullTestDto.setDescription(test.getDescription());
        fullTestDto.setSubtests(new ArrayList<>());
        for(Subtest subtest : test.getSubtestList()){
            SubtestDto subtestDto = new SubtestDto();
            subtestDto.setId(subtest.getId());
            subtestDto.setTestType(subtest.getTestType());
            subtestDto.setDuration(subtest.getDuration());
            String[] guideArray = subtest.getGuide().split(";");
            subtestDto.setGuides(new ArrayList<>());
            for(String tempGuide :guideArray){
                subtestDto.getGuides().add(tempGuide);
            }
            subtestDto.setQuestions(new ArrayList<>());
            for(Question question:subtest.getQuestionList()){
                QuestionDto questionDto = new QuestionDto();
                questionDto.setId(question.getId());
                questionDto.setQuestionContent(new ArrayList<>());
                String[] questionArray = question.getQuestionContent().split(";");
                for(String tempQuestion : questionArray){
                    questionDto.getQuestionContent().add(tempQuestion);
                }
                questionDto.setQuestionCategory(question.getQuestionCategory());
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
    @Transactional
    public List<Test> findAll() {
        return testRepository.findAll();
    }

//_______________________________________________________________________________________________________________
    @Override
    @Transactional
    public Subtest findSubtestById(String id) {
        return subtestRepository.findById(id).orElseThrow(()->new TestException("Subtest not found"));
    }

}
