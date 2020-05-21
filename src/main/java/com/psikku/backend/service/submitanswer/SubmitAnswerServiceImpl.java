package com.psikku.backend.service.submitanswer;

import com.psikku.backend.dto.testresult.TestFinalResultDto;
import com.psikku.backend.dto.useranswer.SubmittedAnswerDto;
import com.psikku.backend.dto.useranswer.UserAnswerDto;
import com.psikku.backend.entity.*;
import com.psikku.backend.exception.TestResultException;
import com.psikku.backend.repository.*;
import com.psikku.backend.service.testresult.TestResultService;
import com.psikku.backend.service.uniquetestresultcalculator.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    TestResultRepository testResultRepository;

    @Autowired
    CfitResultTestCalculator cfitResultTestCalculator;

    @Autowired
    BullyResultTestCalculator bullyResultTestCalculator;

    @Autowired
    EQResultTestCalculator eqResultTestCalculator;

    @Autowired
    GayaBelajar1ResultTestCalculator gayaBelajar1ResultTestCalculator;

    @Autowired
    GayaBelajar2ResultTestCalculator gayaBelajar2ResultTestCalculator;

    @Autowired
    MinatBakatResultTestCalculator minatBakatResultTestCalculator;

    @Autowired
    SurveyKarakterResultTestCalculator surveyKarakterResultTestCalculator;

    @Autowired
    CovidResultTestCalculator covidResultTestCalculator;

    @Autowired
    StateAnxietyTestResultCalculator stateAnxietyTestResultCalculator;

    @Autowired
    TestResultService testResultService;

    @Autowired
    GenericResultTestCalculator genericResultTestCalculator;

    @Autowired
    DepressionTestResultCalculator depressionTestResultCalculator;


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

    // calculate result test generic
    @Override
    public TestFinalResultDto calculateGenericTest(UserAnswerDto userAnswerDto) {
        List<SubmittedAnswerDto> answerFromUser = userAnswerDto.getSubmittedAnswerDtoList();
        TestResult testResult = genericResultTestCalculator.calculateNewResult(answerFromUser);
        TestFinalResultDto testFinalResultDto = testResultService.convertToTestResultDto(testResult);
        testResult.setDateOfTest(formatLdt(userAnswerDto.getCreationDateTime()));
        testResultService.saveTestResult(testResult);
        return testFinalResultDto;
    }

//    public String calculateResultTest(List<SubmittedAnswerDto> submittedAnswerDtoList){
//
//        // get cfit3 answer only
//        List<SubmittedAnswerDto> cfitAnswer =
//                getSpecificAnswerDtoList(submittedAnswerDtoList, "cfit");
//
//        // get gaya belajar1 test only
//        List<SubmittedAnswerDto> gayaBelajar1Only =
//                getSpecificAnswerDtoList(submittedAnswerDtoList, "gayaBelajar1".toLowerCase());
//
//        // get gaya belajar2 test only
//        List<SubmittedAnswerDto> gayaBelajar2Only =
//                getSpecificAnswerDtoList(submittedAnswerDtoList, "gayaBelajar2".toLowerCase());
//
//        // get bully test only
//        List<SubmittedAnswerDto> bullyTestOnly =
//                getSpecificAnswerDtoList(submittedAnswerDtoList, "bully".toLowerCase());
//
//        // get eq test only
//        List<SubmittedAnswerDto> eqTestOnly =
//                getSpecificAnswerDtoList(submittedAnswerDtoList, "eq".toLowerCase());
//
//        // minatbakat test only
//        List<SubmittedAnswerDto> minatBakatTestOnly =
//                getSpecificAnswerDtoList(submittedAnswerDtoList, "bakat".toLowerCase());
//
//        // survey karakter test only
//        List<SubmittedAnswerDto> surveyKarakterOnly =
//                getSpecificAnswerDtoList(submittedAnswerDtoList, "surveyKarakter".toLowerCase());
//
//        // covid test only
//        List<SubmittedAnswerDto> covidOnly =
//                getSpecificAnswerDtoList(submittedAnswerDtoList, "covid".toLowerCase());
//
//
//        if(!cfitAnswer.isEmpty()){
//            cfitResultTestCalculator.calculateNewResult(cfitAnswer);
//        }
//        if(!bullyTestOnly.isEmpty()){
//            bullyResultTestCalculator.calculateNewResult(bullyTestOnly);
//        }
//        if(!eqTestOnly.isEmpty()){
//            eqResultTestCalculator.calculateNewResult(eqTestOnly);
//        }
//        if(!gayaBelajar1Only.isEmpty()){
//            gayaBelajar1ResultTestCalculator.calculateNewResult(gayaBelajar1Only);
//        }
//        if(!gayaBelajar2Only.isEmpty()){
//            gayaBelajar2ResultTestCalculator.calculateNewResult(gayaBelajar2Only);
//        }
//        if(!minatBakatTestOnly.isEmpty()){
//            minatBakatResultTestCalculator.calculateNewResult(minatBakatTestOnly);
//        }
//        if(!surveyKarakterOnly.isEmpty()){
//            surveyKarakterResultTestCalculator.calculateNewResult(surveyKarakterOnly);
//        }
//        if(!covidOnly.isEmpty()){
//            covidResultTestCalculator.calculateNewResult(covidOnly);
//        }
//
//
//        return eqResultTestCalculator.getResult()
//                +"\n\n"+bullyResultTestCalculator.getTestResult()
//                +"\n\n"+gayaBelajar1ResultTestCalculator.getResult()
//                +"\n\n"+gayaBelajar2ResultTestCalculator.getResult()
//                +"\n\n"+minatBakatResultTestCalculator.getResult()
//                +"\n\n"+surveyKarakterResultTestCalculator.getTestResult()
//                +"\n\n"+cfitResultTestCalculator.getResult()
//                +"\n\n"+covidResultTestCalculator.getResult();
//}


    @Override
    @Transactional
    public TestFinalResultDto calculateResultTestV2(UserAnswerDto userAnswerDto) {

        List<SubmittedAnswerDto> submittedAnswerDtoList = userAnswerDto.getSubmittedAnswerDtoList();
        LocalDateTime creationDate = formatLdt(userAnswerDto.getCreationDateTime());
        TestResult testResult = null;

        //filter the specific answer only from the submission

        // get cfit3 answer only
        List<SubmittedAnswerDto> cfitAnswer =
                getSpecificAnswerDtoList(submittedAnswerDtoList, "cfit");

        // get gaya belajar1 test only
        List<SubmittedAnswerDto> gayaBelajar1Only =
                getSpecificAnswerDtoList(submittedAnswerDtoList, "gayaBelajar1".toLowerCase());

        // get gaya belajar2 test only
        List<SubmittedAnswerDto> gayaBelajar2Only =
                getSpecificAnswerDtoList(submittedAnswerDtoList, "gayaBelajar2".toLowerCase());

        // get bully test only
        List<SubmittedAnswerDto> bullyTestOnly =
                getSpecificAnswerDtoList(submittedAnswerDtoList, "bully".toLowerCase());

        // get eq test only
        List<SubmittedAnswerDto> eqTestOnly =
                getSpecificAnswerDtoList(submittedAnswerDtoList, "eq".toLowerCase());

        // minatbakat test only
        List<SubmittedAnswerDto> minatBakatTestOnly =
                getSpecificAnswerDtoList(submittedAnswerDtoList, "bakat".toLowerCase());

        // survey karakter test only
        List<SubmittedAnswerDto> surveyKarakterOnly =
                getSpecificAnswerDtoList(submittedAnswerDtoList, "surveyKarakter".toLowerCase());

        // covid test only
        List<SubmittedAnswerDto> covidOnly =
                getSpecificAnswerDtoList(submittedAnswerDtoList, "covid".toLowerCase());

        // stateAnxiety test only
        List<SubmittedAnswerDto> stateAnxietyOnly =
                getSpecificAnswerDtoList(submittedAnswerDtoList, "stateAnxiety".toLowerCase());

        // kesbang test only
        List<SubmittedAnswerDto> belaNegaraOnly =
                getSpecificAnswerDtoList(submittedAnswerDtoList, "belaNegara".toLowerCase());

        // Depression test only
        List<SubmittedAnswerDto> depressionOnly =
                getSpecificAnswerDtoList(submittedAnswerDtoList, "depression".toLowerCase());

        // calculate each unique test independently

        if(!cfitAnswer.isEmpty()){
            testResult = cfitResultTestCalculator.calculateNewResult(cfitAnswer);
            testResult.setDateOfTest(creationDate);
            testResultRepository.save(testResult);
        }
        if(!bullyTestOnly.isEmpty()){
            testResult = bullyResultTestCalculator.calculateNewResult(bullyTestOnly);
            testResult.setDateOfTest(creationDate);
            testResultRepository.save(testResult);
        }
        if(!eqTestOnly.isEmpty()){
            testResult = eqResultTestCalculator.calculateNewResult(eqTestOnly);
            testResult.setDateOfTest(creationDate);
            testResultRepository.save(testResult);
        }
        if(!gayaBelajar1Only.isEmpty()){
            testResult = gayaBelajar1ResultTestCalculator.calculateNewResult(gayaBelajar1Only);
            testResult.setDateOfTest(creationDate);
            testResultRepository.save(testResult);
        }
        if(!gayaBelajar2Only.isEmpty()){
            testResult = gayaBelajar2ResultTestCalculator.calculateNewResult(gayaBelajar2Only);
            testResult.setDateOfTest(creationDate);
            testResultRepository.save(testResult);
        }
        if(!minatBakatTestOnly.isEmpty()){
            testResult = minatBakatResultTestCalculator.calculateNewResult(minatBakatTestOnly);
            testResult.setDateOfTest(creationDate);
            testResultRepository.save(testResult);
        }
        if(!surveyKarakterOnly.isEmpty()){
            testResult = surveyKarakterResultTestCalculator.calculateNewResult(surveyKarakterOnly);
            testResult.setDateOfTest(creationDate);
            testResultRepository.save(testResult);
        }
        if(!covidOnly.isEmpty()){
            testResult = covidResultTestCalculator.calculateNewResult(covidOnly);
            testResult.setDateOfTest(creationDate);
            testResultRepository.save(testResult);
        }
        if(!stateAnxietyOnly.isEmpty()){
            testResult = stateAnxietyTestResultCalculator.calculateNewResult(stateAnxietyOnly);
            testResult.setDateOfTest(creationDate);
            testResultRepository.save(testResult);
        }
        if(!belaNegaraOnly.isEmpty()){
            testResult = genericResultTestCalculator.calculateNewResult(belaNegaraOnly);
            testResult.setDateOfTest(creationDate);
            testResultRepository.save(testResult);
        }
        if(!depressionOnly.isEmpty()){
            testResult = depressionTestResultCalculator.calculateNewResult(depressionOnly);
            testResult.setDateOfTest(creationDate);
            testResultRepository.save(testResult);
        }

        if(testResult != null){
            System.out.println("reach here");
            return testResultService.convertToTestResultDto(testResult);
        }else{
            throw new TestResultException("Test Result Error");
        }

    }


    private LocalDateTime formatLdt(String customLocalDateTime){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        LocalDateTime ldt = LocalDateTime.parse(customLocalDateTime, dtf);
        return ldt;
    }


    private List<SubmittedAnswerDto> getSpecificAnswerDtoList(List<SubmittedAnswerDto> submittedAnswerDtoList, String testName) {
        return submittedAnswerDtoList.stream()
                .filter(answer -> answer.getQuestionId().contains(testName))
                .collect(Collectors.toList());
    }
}

