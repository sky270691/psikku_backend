package com.psikku.backend.service.uniquetestresultcalculator;

import com.psikku.backend.dto.test.SubmittedAnswerDto;
import com.psikku.backend.entity.Answer;
import com.psikku.backend.entity.Question;
import com.psikku.backend.repository.AnswerRepository;
import com.psikku.backend.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class EQResultTestCalculator implements UniqueResultTestCalculator{

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Override
    public void calculateNewResult(List<SubmittedAnswerDto> EQAnsOnly) {

        List<Answer> EQansFromDb =
                answerRepository.findByIdStartingWith(EQAnsOnly.get(0).getQuestionId());
        String[] EQAnsOnlyIdSplit = EQAnsOnly.get(0).getQuestionId().split("_");
        String testName = EQAnsOnlyIdSplit[0];
        List<Question> questionsFromDb = questionRepository.findByIdStartingWith(testName);
        int mengenalEmosiDiri = 0;
        int mengelolaEmosi = 0;
        int memotivasiDiri = 0;
        int mengenalEmosiOrgLain = 0;
        int membinaHubungan = 0;

        double maxPointEveryCategory = 20;

        for (SubmittedAnswerDto EQAnsDto : EQAnsOnly) {
            for (Question questionFromDb : questionsFromDb) {
                if(EQAnsDto.getQuestionId().equals(questionFromDb.getId())){
                    for (Answer EQAnsDb : EQansFromDb) {
                        if(EQAnsDb.getId().startsWith(questionFromDb.getId())){
                            if(questionFromDb.getQuestionCategory().equalsIgnoreCase("mengenalemosidiri")){
                                mengenalEmosiDiri += Integer.parseInt(EQAnsDb.getAnswerCategory());
                            }else if(questionFromDb.getQuestionCategory().equalsIgnoreCase("mengelolaemosi")){
                                mengelolaEmosi += Integer.parseInt(EQAnsDb.getAnswerCategory());
                            }else if(questionFromDb.getQuestionCategory().equalsIgnoreCase("memotivasidiri")){
                                memotivasiDiri += Integer.parseInt(EQAnsDb.getAnswerCategory());
                            }else if(questionFromDb.getQuestionCategory().equalsIgnoreCase("mengenalemosioranglain")){
                                mengenalEmosiOrgLain += Integer.parseInt(EQAnsDb.getAnswerCategory());
                            }else{
                                membinaHubungan += Integer.parseInt(EQAnsDb.getAnswerCategory());
                            }
                        }
                    }
                }
            }
        }

        double mengenalEmosiDiriPercentage = (double) mengenalEmosiDiri / maxPointEveryCategory;
        double mengelolaEmosiPercentage = (double) mengelolaEmosi / maxPointEveryCategory;
        double memotivasiDiriPercentage = (double) memotivasiDiri / maxPointEveryCategory;
        double mengenalEmosiOrgLainPercentage = (double) mengenalEmosiOrgLain / maxPointEveryCategory;
        double membinaHubunganPercentage = (double) membinaHubungan / maxPointEveryCategory;
    }

}
