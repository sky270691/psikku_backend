package com.psikku.backend.service.submitanswer;

import com.psikku.backend.dto.testresult.TestFinalResultDto;
import com.psikku.backend.dto.useranswer.*;
import com.psikku.backend.entity.*;
import com.psikku.backend.exception.VoucherException;
import com.psikku.backend.repository.SubmitAnswerRepository;
import com.psikku.backend.service.filestorage.FileStorageService;
import com.psikku.backend.service.question.QuestionService;
import com.psikku.backend.service.subtest.SubtestService;
import com.psikku.backend.service.test.TestService;
import com.psikku.backend.service.testresult.TestResultService;
import com.psikku.backend.service.uniquetestresultcalculator.GenericObjectiveResultTestCalculator;
import com.psikku.backend.service.uniquetestresultcalculator.TestResultCalculatorFactory;
import com.psikku.backend.service.uniquetestresultcalculator.UniqueResultTestCalculator;
import com.psikku.backend.service.user.UserService;
import com.psikku.backend.service.voucher.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
    private final FileStorageService fileStorageService;

    @Autowired
    public SubmitAnswerServiceImpl(QuestionService questionService,
                                   SubmitAnswerRepository submitAnswerRepository,
                                   SubtestService subtestService,
                                   TestResultService testResultService,
                                   GenericObjectiveResultTestCalculator genericObjectiveResultTestCalculator,
                                   VoucherService voucherService,
                                   TestService testService,
                                   UserService userService,
                                   TestResultCalculatorFactory testResultCalculatorFactory,
                                   @Lazy FileStorageService fileStorageService) {

        this.questionService = questionService;
        this.submitAnswerRepository = submitAnswerRepository;
        this.subtestService = subtestService;
        this.testResultService = testResultService;
        this.genericObjectiveResultTestCalculator = genericObjectiveResultTestCalculator;
        this.voucherService = voucherService;
        this.testService = testService;
        this.userService = userService;
        this.testResultCalculatorFactory = testResultCalculatorFactory;
        this.fileStorageService = fileStorageService;
    }


    @Override
    @Transactional
    public SubmittedAnswer extractUserAnswer(SubmittedAnswerDto dto,
                                             LocalDateTime creationDate,
                                             Voucher voucher,
                                             User user) {
        SubmittedAnswer submittedAnswer = new SubmittedAnswer();
        if (dto.getAnswers() != null && !dto.getAnswers().isEmpty()) {
            String answerLong = String.join(";", dto.getAnswers());
            submittedAnswer.setAnswers(answerLong);
        }else{
            submittedAnswer.setAnswers("");
        }
        submittedAnswer.setVoucher(voucher);
        submittedAnswer.setCreateTime(creationDate);
        questionService.findOptQuestionByIdEquals(dto.getQuestionId()).ifPresent(submittedAnswer::setQuestion);
        if (dto.getQuestionId() != null) {
            Question que = questionService.findQuestionByIdEquals(dto.getQuestionId());
            submittedAnswer.setQuestion(que);
        }
        submittedAnswer.setUser(user);

        return submittedAnswer;
    }

    @Override
    public List<SubmittedAnswer> convertToSubmittedAnswerList(List<SubmittedAnswerDto> submittedAnswerDto, User user) {
        List<SubmittedAnswer> submittedAnswerList = new ArrayList<>();
        for (SubmittedAnswerDto answerDto : submittedAnswerDto) {
            SubmittedAnswer submittedAnswer = new SubmittedAnswer();
            submittedAnswer.setQuestion(questionService.findQuestionByIdEquals(answerDto.getQuestionId()));
            StringBuilder stringBuilder = new StringBuilder();

            String[] questionIdSplit = answerDto.getQuestionId().split("_");
            String subtestId = questionIdSplit[0] + "_" + questionIdSplit[1];
            String testType = subtestService.findById(subtestId).getTestType();

            if (testType.equalsIgnoreCase("user_input_string") || testType.equalsIgnoreCase("user_input_number")) {
                submittedAnswer.setAnswers(answerDto.getAnswers().get(0));
            } else {
                for (int i = 0; i < answerDto.getAnswers().size(); i++) {
                    String[] answerSplit = answerDto.getAnswers().get(i).split("_");
                    if (i == answerDto.getAnswers().size() - 1) {
                        stringBuilder.append(answerSplit[3]);
                    } else {
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
    public List<SubmittedAnswerDto> convertToSubmittedAnswerDtoList(List<SubmittedAnswer> submittedAnswerList) {
        List<SubmittedAnswerDto> submittedAnswerDtoList = new ArrayList<>();
        for (SubmittedAnswer answer : submittedAnswerList) {
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
    public String getTestType(SubmittedAnswer submittedAnswer) {
        System.out.println(submittedAnswer.getQuestion().getId());
        String[] answerId = submittedAnswer.getQuestion().getId().split("_");
        String subtestId = answerId[0] + "_" + answerId[1];
        String testType = subtestService.findById(subtestId).getTestType();
        return testType;
    }


    @Override
    public TestFinalResultDto calculateGenericTest(UserAnswerDto userAnswerDto, String voucherCode) {
        List<SubmittedAnswerDto> submittedAnswerDtosList = userAnswerDto.getSubmittedAnswerDtoList();
        LocalDateTime creationDate = formatLdt(userAnswerDto.getCreationDateTime());
        Voucher voucher = voucherService.getVoucherByCode(voucherCode);
        String testId = submittedAnswerDtosList.get(0).getQuestionId().split("_")[0];
        Test test = testService.findTestByInternalName(testId);
        List<Test> voucherTestList = voucher.getTestPackage().getTestPackageTestList().stream()
                .map(TestPackageTest::getTest)
                .collect(Collectors.toList());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);

        List<SubmittedAnswer> saList = new ArrayList<>();
        for (SubmittedAnswerDto submittedAnswerDto : submittedAnswerDtosList) {
            SubmittedAnswer sa = extractUserAnswer(submittedAnswerDto, creationDate, voucher, user);
            saList.add(sa);
        }

        submitAnswerRepository.saveAll(saList);


        if (voucherTestList.contains(test)) {
            TestResult testResult = genericObjectiveResultTestCalculator.calculateNewResult(submittedAnswerDtosList);
            testResult.setVoucher(voucher);
            testResult.setDateOfTest(creationDate);
            testResultService.saveTestResult(testResult);
            return testResultService.convertToTestResultDto(testResult);
        } else {
            throw new VoucherException("Voucher did not valid for the submitted Test");
        }
    }


    @Transactional
    @Override
    public void saveRawAnswer(RawAnswerDto dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Test test = testService.findTestByInternalName(dto.getTestInternalName());
        Voucher voucher = voucherService.getVoucherByCode(dto.getVoucher());
//        testResultService.findTestResultByVoucherCodeUsernameAndTest(voucher.getVoucherCode(),username,test.getId());
        User user = userService.findByUsername(username);
        LocalDateTime ldt = formatLdt(dto.getCreationDateTime());

        SubmittedAnswer sa = new SubmittedAnswer();
        sa.setAnswers(String.join("|",dto.getResult(),dto.getTestInternalName()));
        sa.setVoucher(voucher);
        sa.setCreateTime(ldt);
        sa.setUser(user);
//        Optional<Question> questionOpt = questionService.findOptQuestionByIdEquals(sa.getQuestion().getId());
//        questionOpt.ifPresent(sa::setQuestion);

        submitAnswerRepository.save(sa);

        TestResult testResult = new TestResult();
        testResult.setTest(test);
        testResult.setResult(dto.getResult());
        testResult.setVoucher(voucher);
        testResult.setUser(user);
        testResult.setDateOfTest(ldt);
        testResultService.saveTestResult(testResult);
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
        System.out.println(testInternalName);


        List<SubmittedAnswer> saList = new ArrayList<>();
        for (SubmittedAnswerDto saDto : submittedAnswerDtoList) {
            SubmittedAnswer sa = extractUserAnswer(saDto,creationDate,voucher,user);
            saList.add(sa);
        }

        submitAnswerRepository.saveAll(saList);


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

    @Override
    @Transactional
    public void saveUserPictureAnswer(String voucher, String internalTestName, MultipartFile pictFile) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        String fileName = fileStorageService.storeUserAnswerPicture(pictFile);
        TestResult testResult = new TestResult();
        Test test = testService.findTestByInternalName(internalTestName);
        testResult.setTest(test);
        testResult.setDateOfTest(LocalDateTime.now());
        String result = "/api/content/answer-picture/" + fileName;
        testResult.setResult(result);
        testResult.setUser(user);
        Voucher voucher1 = voucherService.getVoucherByCode(voucher);
        testResult.setVoucher(voucher1);


        SubmittedAnswer sa = new SubmittedAnswer();
        sa.setUser(user);
        sa.setVoucher(voucher1);
        sa.setAnswers(String.join("|",result,internalTestName));
        sa.setCreateTime(LocalDateTime.now());
        sa.setQuestion(test.getSubtestList().get(0).getQuestionList().get(0));
        submitAnswerRepository.save(sa);

        testResultService.saveTestResult(testResult);
    }

    @Override
    @Transactional
    public void saveKraepelinResult(KraepelinResultDto dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Voucher voucher = voucherService.getVoucherByCode(dto.getVoucher());
        Test test = testService.findTestByInternalName("kraepelin");
        User user = userService.findByUsername(username);

        LocalDateTime ldt = formatLdt(dto.getCreationDateTime());

        SubmittedAnswer sa = new SubmittedAnswer();
        sa.setUser(user);
        sa.setVoucher(voucher);
        sa.setAnswers(String.join("|",dto.getResult(),"kraepelin"));
        sa.setCreateTime(ldt);
        sa.setQuestion(test.getSubtestList().get(0).getQuestionList().get(0));
        submitAnswerRepository.save(sa);

        TestResult testResult = new TestResult();
        testResult.setUser(user);
        testResult.setVoucher(voucher);
        testResult.setResult(dto.getResult());
        testResult.setDateOfTest(ldt);
        testResult.setTest(test);

        testResultService.saveTestResult(testResult);
    }

    @Override
    @Transactional
    public void savePauliResult(PauliResultDto dto) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Voucher voucher = voucherService.getVoucherByCode(dto.getVoucher());
        Test test = testService.findTestByInternalName("pauli");
        User user = userService.findByUsername(username);

        LocalDateTime ldt = formatLdt(dto.getCreationDateTime());

        SubmittedAnswer sa = new SubmittedAnswer();
        sa.setUser(user);
        sa.setVoucher(voucher);
        sa.setAnswers(String.join("|",dto.getResult(),"pauli"));
        sa.setCreateTime(ldt);
        sa.setQuestion(test.getSubtestList().get(0).getQuestionList().get(0));
        submitAnswerRepository.save(sa);


        TestResult testResult = new TestResult();
        testResult.setUser(user);
        testResult.setVoucher(voucher);
        testResult.setResult(dto.getResult());
        testResult.setDateOfTest(ldt);
        testResult.setTest(test);

        testResultService.saveTestResult(testResult);
    }


    private LocalDateTime formatLdt(String customLocalDateTime) {
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

