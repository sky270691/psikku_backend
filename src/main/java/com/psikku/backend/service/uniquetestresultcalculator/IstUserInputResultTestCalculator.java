package com.psikku.backend.service.uniquetestresultcalculator;

import com.psikku.backend.dto.useranswer.SubmittedAnswerDto;
import com.psikku.backend.entity.Answer;
import com.psikku.backend.entity.Test;
import com.psikku.backend.entity.TestResult;
import com.psikku.backend.entity.User;
import com.psikku.backend.service.answer.AnswerService;
import com.psikku.backend.service.test.TestService;
import com.psikku.backend.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class IstUserInputResultTestCalculator implements UniqueResultTestCalculator{

    private String result;
    private final TestService testService;
    private final UserService userService;
    private final AnswerService answerService;
    private final String name;
    private final ResourceLoader resourceLoader;
    private final String normaCsvLocation;
    private final Logger logger;

    @Autowired
    public IstUserInputResultTestCalculator(TestService testService,
                                            UserService userService,
                                            AnswerService answerService,
                                            ResourceLoader resourceLoader,

                                            @Value("${ist-csv.location}")
                                                    String normaCsvLocation) {
        this.testService = testService;
        this.userService = userService;
        this.answerService = answerService;
        this.resourceLoader = resourceLoader;
        this.normaCsvLocation = normaCsvLocation;
        this.logger = LoggerFactory.getLogger(this.getClass());
        result = "";
        this.name = "istUserInput";
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public TestResult calculateNewResult(List<SubmittedAnswerDto> submittedAnswerDtoList) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        int userAgeInYear = user.getAge()/12;
        String testname = submittedAnswerDtoList.get(0).getQuestionId().split("_")[0];
        List<Answer> answersFromDb = answerService.findByIdStartingWith(testname);
        Test test = testService.findTestByInternalName(testname);

        // Real implementation
        int rw = 0;

        if(!submittedAnswerDtoList.isEmpty()) {
            for (SubmittedAnswerDto answerDto : submittedAnswerDtoList) {
                if(!answerDto.getAnswers().isEmpty()) {
                    for (Answer ansFromDb : answersFromDb) {
                        if (ansFromDb.getAnswerContent().equals(answerDto.getAnswers().get(0))) {
                            rw++;
                        }
                    }
                }
            }
        }

        if(rw > 20){
            rw = 20;
        }
        if(rw < 0 ){
            rw = 0;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("sw=");
        String[] norma = loadNorma(Math.max(userAgeInYear,12),rw,testname).split(",");
        int sw = Integer.parseInt(norma[6]);
        sb.append(sw);
        setResult(sb.toString());

        TestResult testResult = new TestResult();
        testResult.setTest(test);
        testResult.setUser(user);
        testResult.setResult(getResult());
        return testResult;
    }


    private String loadNorma(int ageInYear, int rwValue, String testName) {

        Resource resource = resourceLoader.getResource(normaCsvLocation);
        List<String> allNorma = null;
        try {
            allNorma = Files.readAllLines(Paths.get(resource.getURI()));
        } catch (IOException e) {
            logger.error("norma ist not found");
        }

        String ageInYearString = Integer.toString(ageInYear);

        if(ageInYear > 18 && ageInYear < 21){
            ageInYearString = "19-20";
        }else if(ageInYear > 21 && ageInYear < 26){
            ageInYearString = "21-25";
        }else if(ageInYear > 25 && ageInYear < 31){
            ageInYearString = "26-30";
        }else if(ageInYear > 30 && ageInYear < 36){
            ageInYearString = "31-35";
        }else if(ageInYear > 35 && ageInYear < 41){
            ageInYearString = "36-40";
        }else if(ageInYear > 40 && ageInYear < 46){
            ageInYearString = "41-45";
        }else if(ageInYear > 45 && ageInYear < 51){
            ageInYearString = "46-50";
        }else if(ageInYear > 50){
            ageInYearString = "51-60";
        }

        String lookupResult = "age," + ageInYearString + "," + testName + ",rw," + rwValue + ",sw";
        String finalResult = null;
        if (allNorma != null) {
            for (String scannedNorma : allNorma) {
                if (scannedNorma.startsWith(lookupResult)) {
                    finalResult = scannedNorma;
                }
            }
        }
        return finalResult;
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
