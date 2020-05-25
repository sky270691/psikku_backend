package com.psikku.backend.service.test;

import com.psikku.backend.dto.test.*;
import com.psikku.backend.dto.test.MinimalTestDto;
import com.psikku.backend.entity.*;
import com.psikku.backend.repository.*;
import com.psikku.backend.exception.TestException;
import com.psikku.backend.service.voucher.VoucherService;
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
    UserRepository userRepository;

    @Autowired
    SurveyCategoryRepository surveyCategoryRepository;

    @Autowired
    SubtestRepository subtestRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    VoucherService voucherService;


    @Transactional
    @Override
    public Test addNewTest(FullTestDto fullTestDto) {
        Test entityTest = convertToTestEntity(fullTestDto);
        Optional<Test> findTest = testRepository.findTestByInternalName(entityTest.getInternalName());
        if(!findTest.isPresent()){
            testRepository.save(entityTest);
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
        test.setInternalName(fullTestDto.getInternalName());
        test.setDescription(fullTestDto.getDescription());

        List<SubtestDto> subtestDtoList = fullTestDto.getSubtests();
        List<Subtest> entitySubtestList = new ArrayList<>();
        int subtestNumber = 1;
        for(SubtestDto subtestDto : subtestDtoList){
            Subtest subtest = new Subtest();
            subtest.setId(test.getInternalName() + "_" + subtestNumber++);

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
        fullTestDto.setInternalName(test.getInternalName());
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

                // sort the question ascending
                subtestDto.getQuestions().sort(Comparator.comparingInt(x -> Integer.parseInt(x.getId().split("_")[2])));

                // shuffle the question to display in the endpoint
//                Collections.shuffle(subtestDto.getQuestions());
            }
            fullTestDto.getSubtests().add(subtestDto);


        }
        return fullTestDto;
    }


    @Override
    public MinimalTestDto convertToMinimalTestDto(Test test) {
        MinimalTestDto minimalTestDto = new MinimalTestDto();
        minimalTestDto.setId(test.getId());
        minimalTestDto.setInternalName(test.getInternalName());
        minimalTestDto.setName(test.getName());
        minimalTestDto.setDescription(test.getDescription());
        int duration = test.getSubtestList().stream()
                                            .mapToInt(Subtest::getDuration)
                                            .sum();
        minimalTestDto.setDuration(duration);
        return minimalTestDto;
    }

    public List<MinimalTestDto> getMinTestByVoucher(String voucherCode){
        Voucher voucher = voucherService.getVoucherByCode(voucherCode);
        List<Test> testList = voucher.getTestPackage().getTestList();
        List<MinimalTestDto> minimalTestDtoList = new ArrayList<>();
        testList.forEach(test -> minimalTestDtoList.add(convertToMinimalTestDto(test)));
        minimalTestDtoList.forEach(x -> {
            if(!x.getInternalName().contains("depression")){
                x.setView(true);
            }
        });
        return minimalTestDtoList;
    }

    @Override
    public Test findTestByInternalName(String internalName) {
        return  testRepository.findTestByInternalName(internalName).orElseThrow(()->new TestException("test with internal_name: "+ internalName +"not found"));
    }

    @Override
    public List<MinimalTestDto> getAllMinTestList() {

        List<Test> testList = testRepository.findAll();
        List<MinimalTestDto> minimalTestDtoList = new ArrayList<>();
        testList.forEach(test -> {
            MinimalTestDto tempMinTestDto = convertToMinimalTestDto(test);
            minimalTestDtoList.add(tempMinTestDto);
        });
        return minimalTestDtoList;
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
