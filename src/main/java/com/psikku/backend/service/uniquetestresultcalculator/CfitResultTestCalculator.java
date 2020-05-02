package com.psikku.backend.service.uniquetestresultcalculator;

import com.psikku.backend.dto.test.SubmittedAnswerDto;
import com.psikku.backend.entity.Answer;
import com.psikku.backend.entity.TestResult;
import com.psikku.backend.entity.User;
import com.psikku.backend.repository.AnswerRepository;
import com.psikku.backend.repository.TestRepository;
import com.psikku.backend.repository.TestResultRepository;
import com.psikku.backend.repository.UserRepository;
import com.psikku.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestRepository testRepository;

    @Autowired
    TestResultRepository testResultRepository;

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }


    //ToDo
    // testing the output with the real test

    @Transactional
    @Override
    public void calculateNewResult(List<SubmittedAnswerDto> cfitAnswer) {
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
        for (SubmittedAnswerDto answerDto : cfitAnswer) {
            System.out.println(answerDto.getAnswers());
        }

        // subtest 1 3 4 calculation
        cfitAnswer.forEach(answerSub134Dto -> {
            Answer tempAnswer = answerRepository.findById(answerSub134Dto.getAnswers().get(0).toLowerCase()).orElseThrow(()->
                    new RuntimeException("tempAnswer not Found"));
            Answer[] answers = {tempAnswer};
            if(tempAnswer.getIsCorrect() == 1){
                correctAnswer.add(answers);
            }
        });

        // subtest 2 calculation (2 right answers)
        subtest2.forEach(answerSub2Dto -> {
            String answer1String = Optional.of(answerSub2Dto.getAnswers().get(0).toLowerCase()).orElse("");
            String answer2String = Optional.of(answerSub2Dto.getAnswers().get(1).toLowerCase()).orElse("");

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

        String predicate;
        if(iq>170){
            predicate = "genius";
        }else if(iq>139){
            predicate = "very superior";
        }else if(iq>119){
            predicate = "superior";
        }else if(iq>109){
            predicate = "high average";
        }else if(iq>89){
            predicate = "average";
        }else if(iq>79){
            predicate = "below average";
        }else if(iq>67){
            predicate = "boderlinemental retardation";
        }else if(iq>51){
            predicate = "mild mental retardation";
        }else if(iq>19){
            predicate = "mentally defective";
        }else{
            predicate = "profund mental retardation";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("iq:").append(iq).append(",");
        sb.append("predicate:").append(predicate);
        setResult(sb.toString());

        TestResult testResult = new TestResult();
        testResult.setUser(user);
        testResult.setTest(testRepository.findTestByName("cfit3").orElseThrow(()->new RuntimeException(getClass().getSimpleName()+" Test not found")));
        testResult.setResult(getResult());
        testResultRepository.save(testResult);
    }

    private Map<Integer,Integer> getAgeResultKeyValue(String ageRange){
        // /opt/tomcat/webapps/backend/WEB-INF/classes/static/testresultdata/cfit/cfit3.pku -->> for live apps
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
