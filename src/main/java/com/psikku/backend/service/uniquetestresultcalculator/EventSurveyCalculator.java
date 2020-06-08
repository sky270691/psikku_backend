package com.psikku.backend.service.uniquetestresultcalculator;

import com.psikku.backend.dto.useranswer.SubmittedAnswerDto;
import com.psikku.backend.entity.Answer;
import com.psikku.backend.entity.Test;
import com.psikku.backend.entity.TestResult;
import com.psikku.backend.entity.User;
import com.psikku.backend.service.answer.AnswerService;
import com.psikku.backend.service.test.TestService;
import com.psikku.backend.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EventSurveyCalculator implements UniqueResultTestCalculator{

    private TestService testService;
    private UserService userService;
    private AnswerService answerService;
    private String result;

    @Autowired
    public EventSurveyCalculator(TestService testService, UserService userService, AnswerService answerService) {
        this.testService = testService;
        this.userService = userService;
        this.answerService = answerService;
        this.result = "";
    }


    @Override
        public TestResult calculateNewResult(List<SubmittedAnswerDto> submittedAnswerDtoList) {

            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findByUsername(username);

            String internalTestName = submittedAnswerDtoList.get(0).getQuestionId().split("_")[0];
            Test test = testService.findTestByInternalName(internalTestName);

            List<Answer> answersFromDb = answerService.findByIdStartingWith(internalTestName);

            int answerPoints = 0;

            for (SubmittedAnswerDto answerDto : submittedAnswerDtoList) {
                for (Answer ansFromDb : answersFromDb) {
                    if(answerDto.getAnswers().get(0).equals(ansFromDb.getId())){
                        answerPoints += Integer.parseInt(ansFromDb.getAnswerCategory());
                    }
                }
            }

            StringBuilder sb = new StringBuilder();
            sb.append(perCategoryPredicate(answerPoints));
            setResult(sb.toString());

            TestResult testResult = new TestResult();
            testResult.setResult("Terima Kasih");
            testResult.setUser(user);
            testResult.setTest(test);
            testResult.setResultCalculation("evaulation point:"+answerPoints);

            return testResult;
        }

        private String perCategoryPredicate(int resultValue){
            if(resultValue < 4){
                return "Tidak antusias";
            }else if(resultValue < 7){
                return "cukup antusias";
            }else {
                return "sangat antusias";
            }
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

