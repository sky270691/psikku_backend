package com.psikku.backend.service.uniquetestresultcalculator;

import com.psikku.backend.dto.test.SubmittedAnswerDto;
import com.psikku.backend.entity.Answer;
import com.psikku.backend.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class GayaBelajar2ResultTestCalculator implements UniqueResultTestCalculator{

    @Autowired
    AnswerRepository answerRepository;


    @Override
    public void calculateNewResult(List<SubmittedAnswerDto> gayaBelajar2AnsOnly) {

        List<Answer> gayaBelajar2AnsFromDb =
                answerRepository.findByIdStartingWith(gayaBelajar2AnsOnly.get(0).getQuestionId());


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

        //Todo
        // update the final output for this test
        List<String> sortedHighestResultKey =
                resultKeyValue.entrySet().stream()
                                         .sorted((x,y)->y.getValue().compareTo(x.getValue()))
                                         .map(x->x.getKey())
                                         .collect(Collectors.toList());
        String highestCategory = sortedHighestResultKey.get(0);
        String secondHighestCategory = sortedHighestResultKey.get(1);
    }
}
