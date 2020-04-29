package com.psikku.backend.service.uniquetestresultcalculator;

import com.psikku.backend.dto.test.SubmittedAnswerDto;
import com.psikku.backend.entity.Answer;
import com.psikku.backend.entity.Question;
import com.psikku.backend.entity.TestResult;
import com.psikku.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class EQResultTestCalculator implements UniqueResultTestCalculator{

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestResultRepository testResultRepository;

    @Autowired
    TestRepository testRepository;

    private String result;

    @Transactional
    @Override
    public void calculateNewResult(List<SubmittedAnswerDto> EQAnsDtoOnly) {
        String[] EQAnsOnlyIdSplit = EQAnsDtoOnly.get(0).getQuestionId().split("_");
        String testName = EQAnsOnlyIdSplit[0];
        List<Answer> EQansFromDb =
                answerRepository.findByIdStartingWith(testName);
        List<Question> questionsFromDb = questionRepository.findByIdStartingWith(testName);
        int mengenalEmosiDiri = 0;
        int mengelolaEmosi = 0;
        int memotivasiDiri = 0;
        int mengenalEmosiOrgLain = 0;
        int membinaHubungan = 0;

        double maxPointEveryCategory = 20;
        // looping thru the submitted answer
        for (SubmittedAnswerDto ansDto : EQAnsDtoOnly) {
            for (Answer ansFromDb : EQansFromDb) {
                if(ansDto.getAnswers().get(0).equals(ansFromDb.getId())){
                    for (Question questionFromDb : questionsFromDb) {
                        if(ansDto.getQuestionId().equals(questionFromDb.getId())){
                            int value = Integer.parseInt(ansFromDb.getAnswerCategory());
                            if(questionFromDb.getQuestionCategory().equalsIgnoreCase("kenalemosidiri")){
                                mengenalEmosiDiri += value;
                            }else if(questionFromDb.getQuestionCategory().equalsIgnoreCase("kelolaemosidiri")){
                                mengelolaEmosi += value;
                            }else if(questionFromDb.getQuestionCategory().equalsIgnoreCase("memotivasidiri")){
                                memotivasiDiri += value;
                            }else if(questionFromDb.getQuestionCategory().equalsIgnoreCase("kenalemosioranglain")){
                                mengenalEmosiOrgLain += value;
                            }else{
                                membinaHubungan += value;
                            }
                        }
                    }
                }
            }
        }

        double mengenalEmosiDiriPercentage = (double) mengenalEmosiDiri / maxPointEveryCategory * 100;
        double mengelolaEmosiPercentage = (double) mengelolaEmosi / maxPointEveryCategory * 100;
        double memotivasiDiriPercentage = (double) memotivasiDiri / maxPointEveryCategory * 100;
        double mengenalEmosiOrgLainPercentage = (double) mengenalEmosiOrgLain / maxPointEveryCategory * 100;
        double membinaHubunganPercentage = (double) membinaHubungan / maxPointEveryCategory * 100;

        double eqTotal = (mengenalEmosiDiriPercentage + mengelolaEmosiPercentage + memotivasiDiriPercentage + mengenalEmosiOrgLainPercentage + membinaHubunganPercentage)/5;

        StringBuilder sb = new StringBuilder();
        sb.append("mengenal emosi diri: ").append((int)mengenalEmosiDiriPercentage).append(",");
        sb.append("mengelola emosi diri: ").append((int)mengelolaEmosiPercentage).append(",");
        sb.append("memotivasi diri: ").append((int)memotivasiDiriPercentage).append(",");
        sb.append("mengenal emosi orang lain: ").append((int)mengenalEmosiOrgLainPercentage).append(",");
        sb.append("membina hubungan: ").append((int)membinaHubunganPercentage).append(",");
        sb.append("EQ: ").append(eqTotal);

        setResult(sb.toString());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        TestResult testResult = new TestResult();
        testResult.setUser(userRepository.findUserByUsername(username));
        testResult.setTest(testRepository.findTestByName(testName).orElseThrow(()->new RuntimeException(getClass().getSimpleName()+"Test not found")));
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
