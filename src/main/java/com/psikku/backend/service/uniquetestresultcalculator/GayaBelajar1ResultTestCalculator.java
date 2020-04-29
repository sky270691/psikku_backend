package com.psikku.backend.service.uniquetestresultcalculator;

import com.psikku.backend.dto.test.SubmittedAnswerDto;
import com.psikku.backend.entity.Answer;
import com.psikku.backend.entity.TestResult;
import com.psikku.backend.repository.AnswerRepository;
import com.psikku.backend.repository.TestRepository;
import com.psikku.backend.repository.TestResultRepository;
import com.psikku.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GayaBelajar1ResultTestCalculator implements UniqueResultTestCalculator {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestResultRepository testResultRepository;

    @Autowired
    TestRepository testRepository;

    @Autowired
    AnswerRepository answerRepository;

    private String result;

    @Override
    public void calculateNewResult(List<SubmittedAnswerDto> gayaBelajar1Only) {

        String[] gayaBelajar1QuestionIdSplit = gayaBelajar1Only.get(0).getQuestionId().split("_");
        String testName = gayaBelajar1QuestionIdSplit[0];
        List<Answer> gayaBelajar1AnswerFromDb = answerRepository.findByIdStartingWith(testName);

        int visual = 0;
        int auditori = 0;
        int kinestetik = 0;

        for (Answer answerFromDb : gayaBelajar1AnswerFromDb) {
            for (SubmittedAnswerDto answerDto : gayaBelajar1Only) {
                if(answerFromDb.getId().startsWith(answerDto.getQuestionId())){
                    for (String matchQuestionAnswers : answerDto.getAnswers()) {
                        if(answerFromDb.getId().equals(matchQuestionAnswers)){
                            if(answerFromDb.getAnswerCategory().equals("visual")){
                                visual++;
                            }else if(answerFromDb.getAnswerCategory().equals("auditori")){
                                auditori++;
                            }else{
                                kinestetik++;
                            }
                        }
                    }
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("visual: ").append(visual).append(",");
        sb.append("auditori: ").append(auditori).append(",");
        sb.append("kinestetik: ").append(kinestetik).append(",");
        setResult(sb.toString());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        TestResult testResult = new TestResult();
        testResult.setUser(userRepository.findUserByUsername(username));
        testResult.setTest(testRepository.findTestByName(testName.toLowerCase()).orElseThrow(()->new RuntimeException(getClass().getSimpleName()+"Test not found")));
        testResult.setResult(getResult());
        testResultRepository.save(testResult);

    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
