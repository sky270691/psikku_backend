package com.psikku.backend.service.uniquetestresultcalculator;

import com.psikku.backend.dto.test.SubmittedAnswerDto;
import com.psikku.backend.entity.Answer;
import com.psikku.backend.entity.User;
import com.psikku.backend.repository.AnswerRepository;
import com.psikku.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CfitResultTestCalculator implements UniqueResultTestCalculator {

    private String result;

    @Autowired
    UserService userService;

    @Autowired
    AnswerRepository answerRepository;

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }


    //ToDo
    // testing the output with the real test

    @Override
    public void calculateNewResult(List<SubmittedAnswerDto> submittedAnswerDtoList) {
        List<SubmittedAnswerDto> cfitAnswer = submittedAnswerDtoList.stream()
                                                                  .filter(answer -> answer.getQuestionId().contains("cfit"))
                                                                  .collect(Collectors.toList());
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userService.findByUsername(username);
        int ageInMonth = user.getAge(LocalDate.now());

        // answers data from DB contains correct answer based on user's input
        List<Answer[]> correctAnswer = new ArrayList<>();

        List<SubmittedAnswerDto> subtest2 = cfitAnswer.stream()
                                                      .filter(answer -> {
                                                          String[] questionIdSplit = answer.getQuestionId().split("_");
                                                          return Integer.parseInt(questionIdSplit[1]) == 2;
                                                      }).collect(Collectors.toList());
        // cfitAnswer contains only subtest 1, 3, 4
        cfitAnswer.removeAll(subtest2);

        // subtest 1 3 4 calculation
        cfitAnswer.forEach(answerSub134Dto -> {
            Answer tempAnswer = answerRepository.findById(answerSub134Dto.getAnswers().get(0)).orElseThrow(()->
                    new RuntimeException("tempAnswer not Found"));
            Answer[] answers = {tempAnswer};
            if(tempAnswer.getIsCorrect() == 1){
                correctAnswer.add(answers);
            }
        });

        // subtest 2 calculation (2 right answers)
        subtest2.forEach(answerSub2Dto -> {
            String answer1String = Optional.of(answerSub2Dto.getAnswers().get(0)).orElse("");
            String answer2String = Optional.of(answerSub2Dto.getAnswers().get(1)).orElse("");
            if(!answer1String.equals("") && !answer2String.equals("")){
                Answer answer1 = answerRepository.findById(answer1String).orElse(null);
                Answer answer2 = answerRepository.findById(answer2String).orElse(null);

                // check if all the answers are correct then add the list of the correctAnswer
                if(answer1.getIsCorrect() == 1 && answer2.getIsCorrect() ==1){
                    Answer[] answers = {answer1,answer2};
                    correctAnswer.add(answers);
                }
            }
        });

        Map<Integer,Integer> resultMapping;

        if(ageInMonth < 161){
            resultMapping = getAgeResultKeyValue("13.0-13.4");
        }else if(ageInMonth < 168){
            resultMapping = getAgeResultKeyValue("13.5-13.11");
        }else if(ageInMonth < 180){
            resultMapping = getAgeResultKeyValue("14.0-14.11");
        }else if(ageInMonth < 192){
            resultMapping = getAgeResultKeyValue("15.0-15.11");
        }else if(ageInMonth < 204){
            resultMapping = getAgeResultKeyValue("16.0-16.11");
        }else{
            resultMapping = getAgeResultKeyValue("17.0-more");
        }

        // the result output(iq)
        int iq = resultMapping.get(correctAnswer.size());
        setResult("hasil test iq: " + iq +"\nusia: "+ (ageInMonth));
        System.out.println("iq");
    }

    private Map<Integer,Integer> getAgeResultKeyValue(String ageRange){
        try(Scanner scanner = new Scanner(new BufferedReader(new FileReader("src/main/resources/static/testresultdata/cfit/cfit3.pku")))){

            Map<Integer,Integer> map = new LinkedHashMap<>();
            String testParameterKeyValue;
            while(scanner.hasNextLine()){
                testParameterKeyValue = scanner.nextLine();
                if(testParameterKeyValue.equals(ageRange)){
                    String parameter;
                    while(scanner.hasNextLine()){
                        parameter = scanner.nextLine();
                        if(parameter.equals("---")){
                            break;
                        }
                        String[] parameterSplit = parameter.split(",");
                        map.put(Integer.parseInt(parameterSplit[0]),Integer.parseInt(parameterSplit[1]));
                    }
                }
            }
            System.out.println(map);
            return map;
        }catch (IOException e){
            throw new RuntimeException("Cfit calculation error: "+e.getMessage());
        }
    }
}
