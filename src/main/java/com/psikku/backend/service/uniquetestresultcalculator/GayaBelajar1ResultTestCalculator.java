package com.psikku.backend.service.uniquetestresultcalculator;

import com.psikku.backend.dto.test.SubmittedAnswerDto;
import com.psikku.backend.entity.Answer;
import com.psikku.backend.repository.AnswerRepository;
import com.psikku.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GayaBelajar1ResultTestCalculator implements UniqueResultTestCalculator {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Override
    public void calculateNewResult(List<SubmittedAnswerDto> gayaBelajar1Only) {

        List<Answer> gayaBelajar1AnswerFromDb = answerRepository.findByIdStartingWith(gayaBelajar1Only.get(0).getQuestionId());

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
    }

}
