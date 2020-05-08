package com.psikku.backend.service.uniquetestresultcalculator;

import com.psikku.backend.dto.useranswer.SubmittedAnswerDto;
import com.psikku.backend.entity.Answer;
import com.psikku.backend.entity.Question;
import com.psikku.backend.entity.TestResult;
import com.psikku.backend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BullyResultTestCalculator implements UniqueResultTestCalculator{

    public final Logger logger = LoggerFactory.getLogger(BullyResultTestCalculator.class);

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    TestRepository testRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestResultRepository resultRepository;

    private String testResult;

    @Transactional
    @Override
    public TestResult calculateNewResult(List<SubmittedAnswerDto> bullyAnsDtoOnly) {

        String[] bullyAnsDtoOnlySplit = bullyAnsDtoOnly.get(0).getQuestionId().split("_");
        String testName = bullyAnsDtoOnlySplit[0];

        List<Answer> bullyAnsFromDb =
                answerRepository.findByIdStartingWith(testName);

        String[] bullyAnsSplit = bullyAnsDtoOnly.get(0).getQuestionId().split("_");


        List<Question> questionsFromDb = questionRepository.findByIdStartingWith(bullyAnsSplit[0].toLowerCase());
//        for (Question question : questionsFromDb) {
//            System.out.println(question.getId());
//        }

//        questionsFromDb.forEach(x -> System.out.println(x.getQuestionCategory()));

        int fisik = 0;
        int verbal = 0;
        int nonVerbal = 0;
        int relasional = 0;
        int cyber = 0;

        int maxVerbalAndCyber = 12;
        int maxFisik = 18;
        int maxNonVerbal = 21;
        int maxRelasional = 27;



        for (SubmittedAnswerDto ansDto : bullyAnsDtoOnly) {
            for (Answer ansFromDb : bullyAnsFromDb) {
                if(ansDto.getAnswers().get(0).equalsIgnoreCase(ansFromDb.getId())){
                    for (Question questionFromDb : questionsFromDb) {
                        int value = Integer.parseInt(ansFromDb.getAnswerCategory());
                        if(ansDto.getQuestionId().equalsIgnoreCase(questionFromDb.getId())){
                            if(questionFromDb.getQuestionCategory().equalsIgnoreCase("fisik")){
                                fisik += value;
                            }else if(questionFromDb.getQuestionCategory().equalsIgnoreCase("verbal")){
                                verbal += value;
                            }else if(questionFromDb.getQuestionCategory().equalsIgnoreCase("nonverbal")){
                                nonVerbal += value;
                            }else if(questionFromDb.getQuestionCategory().equalsIgnoreCase("relasional")){
                                relasional += value;
                            }else if(questionFromDb.getQuestionCategory().equalsIgnoreCase("cyber")){
                                cyber += value;
                                System.out.println(ansDto.getAnswers().get(0));
                            }
                        }
                    }
                }
            }
        }

        double fisikPercentage = (double)  fisik / maxFisik * 100;
        double verbalPercentage = (double)  verbal /maxVerbalAndCyber * 100;
        double nonVerbalPercentage = (double)  nonVerbal/maxNonVerbal * 100;
        double relasionalPercentage = (double)  relasional/maxRelasional * 100;
        double cyberPercentage = (double)  cyber/maxVerbalAndCyber * 100;

        StringBuilder sb = new StringBuilder();
        sb.append("fisik:"+(int)fisikPercentage).append(":").append(perCategoryPredicate(fisikPercentage)).append(",");
        sb.append("verbal:"+(int)verbalPercentage).append(":").append(perCategoryPredicate(verbalPercentage)).append(",");
        sb.append("nonVerbal:"+(int)nonVerbalPercentage).append(":").append(perCategoryPredicate(nonVerbalPercentage)).append(",");
        sb.append("relasional:"+(int)relasionalPercentage).append(":").append(perCategoryPredicate(relasionalPercentage)).append(",");
        sb.append("cyber:"+(int)cyberPercentage).append(":").append(perCategoryPredicate(cyberPercentage));

        setTestResult(sb.toString());

        TestResult testResult = new TestResult();
        testResult.setTest(testRepository.findTestByName("bully").orElseThrow(()-> new RuntimeException(getClass().getName()+": Test Not Found")));
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        testResult.setUser(userRepository.findUserByUsername(username));
        testResult.setResult(getTestResult());
        logger.info("username: '"+username+"' BULLY answer calculated successfully");
        return testResult;
//        resultRepository.save(testResult);
    }

    private String perCategoryPredicate(double resultValue){
        if(resultValue < 33.33){
            return "rendah";
        }else if(resultValue < 66.66){
            return "sedang";
        }else {
            return "tinggi";
        }
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }
}
