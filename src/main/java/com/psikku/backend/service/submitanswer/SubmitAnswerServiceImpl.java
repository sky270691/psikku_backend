package com.psikku.backend.service.submitanswer;

import com.psikku.backend.dto.testresult.TestFinalResultDto;
import com.psikku.backend.dto.useranswer.SubmittedAnswerDto;
import com.psikku.backend.dto.useranswer.UserAnswerDto;
import com.psikku.backend.entity.*;
import com.psikku.backend.exception.VoucherException;
import com.psikku.backend.repository.*;
import com.psikku.backend.service.question.QuestionService;
import com.psikku.backend.service.subtest.SubtestService;
import com.psikku.backend.service.test.TestService;
import com.psikku.backend.service.testresult.TestResultService;
import com.psikku.backend.service.uniquetestresultcalculator.*;
import com.psikku.backend.service.user.UserService;
import com.psikku.backend.service.voucher.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SubmitAnswerServiceImpl implements SubmitAnswerService {

    private final QuestionService questionService;
    private final SubmitAnswerRepository submitAnswerRepository;
    private final SubtestService subtestService;
    private final TestResultService testResultService;
    private final GenericObjectiveResultTestCalculator genericObjectiveResultTestCalculator;
    private final VoucherService voucherService;
    private final TestService testService;
    private final UserService userService;
    private final TestResultCalculatorFactory testResultCalculatorFactory;

    @Autowired
    public SubmitAnswerServiceImpl(QuestionService questionService,
                                   SubmitAnswerRepository submitAnswerRepository,
                                   SubtestService subtestService,
                                   TestResultService testResultService,
                                   GenericObjectiveResultTestCalculator genericObjectiveResultTestCalculator,
                                   VoucherService voucherService,
                                   TestService testService,
                                   UserService userService,
                                   TestResultCalculatorFactory testResultCalculatorFactory) {

        this.questionService = questionService;
        this.submitAnswerRepository = submitAnswerRepository;
        this.subtestService = subtestService;
        this.testResultService = testResultService;
        this.genericObjectiveResultTestCalculator = genericObjectiveResultTestCalculator;
        this.voucherService = voucherService;
        this.testService = testService;
        this.userService = userService;
        this.testResultCalculatorFactory = testResultCalculatorFactory;
    }

    @Transactional
    @Override
    public List<SubmittedAnswer> saveUserAnswer(List<SubmittedAnswer> answerList) {
        answerList.forEach(submitAnswerRepository::save);
        return answerList;
    }

    @Override
    public List<SubmittedAnswer> convertToSubmittedAnswerList(List<SubmittedAnswerDto> submittedAnswerDto, User user){
        List<SubmittedAnswer> submittedAnswerList = new ArrayList<>();
        for(SubmittedAnswerDto answerDto : submittedAnswerDto){
            SubmittedAnswer submittedAnswer = new SubmittedAnswer();
            submittedAnswer.setQuestion(questionService.findQuestionByIdEquals(answerDto.getQuestionId()));
            StringBuilder stringBuilder = new StringBuilder();

            String[] questionIdSplit = answerDto.getQuestionId().split("_");
            String subtestId = questionIdSplit[0]+"_"+questionIdSplit[1];
            String testType = subtestService.findById(subtestId).getTestType();

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
        String testType = subtestService.findById(subtestId).getTestType();
        return testType;
    }


    @Override
    public TestFinalResultDto calculateGenericTest(UserAnswerDto userAnswerDto, String voucherCode){
        List<SubmittedAnswerDto> submittedAnswerDtosList = userAnswerDto.getSubmittedAnswerDtoList();
        LocalDateTime creationDate = formatLdt(userAnswerDto.getCreationDateTime());
        Voucher voucher = voucherService.getVoucherByCode(voucherCode);
        String testId = submittedAnswerDtosList.get(0).getQuestionId().split("_")[0];
        Test test = testService.findTestByInternalName(testId);
        List<Test> voucherTestList = voucher.getTestPackage().getTestList();
        if(voucherTestList.contains(test)){
            TestResult testResult = genericObjectiveResultTestCalculator.calculateNewResult(submittedAnswerDtosList,test);
            testResult.setVoucher(voucher);
            testResult.setDateOfTest(creationDate);
            testResultService.saveTestResult(testResult);
            return testResultService.convertToTestResultDto(testResult);
        }else{
            throw new VoucherException("Voucher did not valid for the submitted Test");
        }
    }

    @Override
    @Transactional
    public TestFinalResultDto calculateResultTestV2(UserAnswerDto userAnswerDto, String voucherCode) {

        List<SubmittedAnswerDto> submittedAnswerDtoList = userAnswerDto.getSubmittedAnswerDtoList();
        LocalDateTime creationDate = formatLdt(userAnswerDto.getCreationDateTime());
        Voucher voucher = voucherService.getVoucherByCode(voucherCode);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        String testInternalName = userAnswerDto.getSubmittedAnswerDtoList().get(0).getQuestionId().split("_")[0];

        TestResult testResult;

        UniqueResultTestCalculator testCalculator = testResultCalculatorFactory.getTestCalculator(testInternalName);
        testResult = testCalculator.calculateNewResult(submittedAnswerDtoList);
        testResult.setVoucher(voucher);
        testResult.setUser(user);
        testResult.setDateOfTest(creationDate);
        testResultService.saveTestResult(testResult);

        System.out.println("reach here");
        return testResultService.convertToTestResultDto(testResult);

    }


    private LocalDateTime formatLdt(String customLocalDateTime){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        LocalDateTime ldt = LocalDateTime.parse(customLocalDateTime, dtf);
        return ldt;
    }


    @Deprecated
    private List<SubmittedAnswerDto> getSpecificAnswerDtoList(List<SubmittedAnswerDto> submittedAnswerDtoList, String testInternalName) {
        return submittedAnswerDtoList.stream()
                .filter(answer -> answer.getQuestionId().contains(testInternalName))
                .collect(Collectors.toList());
    }
}

