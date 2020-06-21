package com.psikku.backend.service.uniquetestresultcalculator;

import com.psikku.backend.dto.useranswer.SubmittedAnswerDto;
import com.psikku.backend.entity.*;
import com.psikku.backend.exception.TestException;
import com.psikku.backend.service.answer.AnswerService;
import com.psikku.backend.service.test.TestService;
import com.psikku.backend.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BelaNegaraResultTestCalculator implements UniqueResultTestCalculator{

    private String result;
    private final TestService testService;
    private final UserService userService;
    private final AnswerService answerService;
    private final String name;

    @Autowired
    public BelaNegaraResultTestCalculator(TestService testService,
                                          UserService userService,
                                          AnswerService answerService) {
        this.testService = testService;
        this.userService = userService;
        this.answerService = answerService;
        result = "";
        this.name = "belanegara";
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public TestResult calculateNewResult(List<SubmittedAnswerDto> submittedAnswerDtoList) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        String testname = submittedAnswerDtoList.get(0).getQuestionId().split("_")[0];
        List<Answer> answersFromDb = answerService.findByIdStartingWith(testname);
        Test test = testService.findTestByInternalName(testname);

        // Real implementation
        int correctAnswer = 0;

        for(SubmittedAnswerDto answerDto : submittedAnswerDtoList){
            for(Answer ansFromDb : answersFromDb){
                if(ansFromDb.getId().equals(answerDto.getAnswers().get(0))){
                    correctAnswer += ansFromDb.getIsCorrect();
                }
            }
        }

        double points = (double) correctAnswer / (double)getCountQuestion(test) * 100;

//        // fake implementation
//        Random random = new Random();
//        int correctAnswer = random.nextInt(7)+31;
//        int points = correctAnswer*2;

        StringBuilder sb = new StringBuilder();
        sb.append("Terima kasih.\n" +
                "Anda telah terdaftar sebagai Kader Bela Negara Sulut.\n" +
                "E-sertifikat dapat di download oleh seluruh Kader Bela Negara pada puncak kegiatan di bulan Desember 2020 melalui aplikasi ini.");

//        sb.append("Jumlah Jawaban Benar:");
//        sb.append(correctAnswer);
//        sb.append(",");
//        sb.append("Nilai Test:");
//        sb.append(points);
        setResult(sb.toString());

        TestResult testResult = new TestResult();
        testResult.setTest(test);
        testResult.setUser(user);
        testResult.setResult(getResult());
        testResult.setResultCalculation("points:"+points);
        return testResult;
    }

    private int getCountQuestion(Test test){
        int totalQuestion = (int) test.getSubtestList().stream().flatMap(subtest -> subtest.getQuestionList().stream())
                                                                .count();

        return totalQuestion;
    }

    @Override
    public String getResult() {
        return result;
    }

    @Override
    public void setResult(String result) {
        this.result = result;
    }

}
