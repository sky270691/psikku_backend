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

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GayaBelajar2ResultTestCalculator implements UniqueResultTestCalculator{

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    TestRepository testRepository;

    @Autowired
    TestResultRepository testResultRepository;

    @Autowired
    UserRepository userRepository;

    private String result;


    @Override
    public void calculateNewResult(List<SubmittedAnswerDto> gayaBelajar2AnsOnly) {

        String[] gayaBelajar2DtoSplit = gayaBelajar2AnsOnly.get(0).getQuestionId().split("_");
        String testName = gayaBelajar2DtoSplit[0].toLowerCase();
        List<Answer> gayaBelajar2AnsFromDb =
                answerRepository.findByIdStartingWith(testName);
        int ce = 0;
        int ac = 0;
        int ro = 0;
        int ae = 0;

        for (SubmittedAnswerDto gb2AnsDto : gayaBelajar2AnsOnly) {
            for (Answer gb2AnsFromDb : gayaBelajar2AnsFromDb) {
                if(gb2AnsFromDb.getId().startsWith(gb2AnsDto.getQuestionId())){
                    for (String gb2AnsDtoEachAnswer : gb2AnsDto.getAnswers()) {
                        if(gb2AnsDtoEachAnswer.equalsIgnoreCase(gb2AnsFromDb.getId())){
                            if(gb2AnsFromDb.getAnswerCategory().equals("ce")){
                                ce++;
                            }else if(gb2AnsFromDb.getAnswerCategory().equals("ac")){
                                ac++;
                            }else if(gb2AnsFromDb.getAnswerCategory().equals("ro")){
                                ro++;
                            }else {
                                ae++;
                            }
                        }
                    }
                }
            }
        }
        Map<String,Integer> resultKeyValue = new HashMap<>();
        resultKeyValue.put("ce",ce);
        resultKeyValue.put("ac",ac);
        resultKeyValue.put("ro",ro);
        resultKeyValue.put("ae",ae);

        List<String> ceAc = new ArrayList<>();
        if(resultKeyValue.get("ce") > resultKeyValue.get("ac")){
            ceAc.add("ce");
        }else if(resultKeyValue.get("ce") < resultKeyValue.get("ac")){
            ceAc.add("ac");
        }

        List<String> roAe = new ArrayList<>();
        if(resultKeyValue.get("ro") > resultKeyValue.get("ae")){
            roAe.add("ro");
        }else if(resultKeyValue.get("ro") < resultKeyValue.get("ae")){
            roAe.add("ae");
        }


        //Todo
        // update the final output for this test
        List<String> sortedHighestResultKey =
                resultKeyValue.entrySet().stream()
                                         .sorted((x,y)->y.getValue().compareTo(x.getValue()))
                                         .map(x->x.getKey())
                                         .collect(Collectors.toList());
        String highestCategory = sortedHighestResultKey.get(0);
        String secondHighestCategory = sortedHighestResultKey.get(1);
        String thirdHighestCategory = sortedHighestResultKey.get(2);
        String fourthHighestCategory = sortedHighestResultKey.get(3);

        StringBuilder sb = new StringBuilder();
//        sb.append(resultKeyValue).append("\n");
        sb.append("Highest Category:").append(highestCategory).append(",");
        sb.append("Combination Category:").append(ceAc.get(0)).append("+").append(roAe.get(0));

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
