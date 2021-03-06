package com.psikku.backend.service.uniquetestresultcalculator;

import com.psikku.backend.dto.useranswer.SubmittedAnswerDto;
import com.psikku.backend.entity.Answer;
import com.psikku.backend.entity.TestResult;
import com.psikku.backend.entity.User;
import com.psikku.backend.repository.AnswerRepository;
import com.psikku.backend.repository.TestRepository;
import com.psikku.backend.repository.TestResultRepository;
import com.psikku.backend.repository.UserRepository;
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

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CfitResultTestCalculator implements UniqueResultTestCalculator {

    private final UserService userService;
    private final AnswerService answerService;
    private final TestService testService;
    private final ResourceLoader resourceLoader;

    private final Logger logger;
    private String result;
    private final String name;

    @Value("${cfit-pku.location}")
    private String cfitPkuLocation;

    @Autowired
    public CfitResultTestCalculator(UserService userService,
                                    AnswerService answerService,
                                    TestService testService,
                                    ResourceLoader resourceLoader) {
        this.userService = userService;
        this.answerService = answerService;
        this.testService = testService;
        this.resourceLoader = resourceLoader;
        this.logger = LoggerFactory.getLogger(CfitResultTestCalculator.class);
        this.result = "";
        this.name = "cfit";
    }

    @Override
    public String getResult() {
        return this.result;
    }

    @Override
    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Transactional
    @Override
    public TestResult calculateNewResult(List<SubmittedAnswerDto> cfitAnswer) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userService.findByUsername(username);
        int ageInMonth = user.getAge();

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
            Answer tempAnswer = answerService.findById(answerSub134Dto.getAnswers().get(0).toLowerCase());
            Answer[] answers = {tempAnswer};
            if(tempAnswer.getIsCorrect() == 1){
                correctAnswer.add(answers);
            }
        });

        // subtest 2 calculation (2 right answers)

        if(!subtest2.isEmpty() && subtest2.get(0).getQuestionId().contains("cfit3")){
            subtest2.forEach(answerSub2Dto -> {
                String answer1String = "";
                String answer2String = "";
                if(answerSub2Dto.getAnswers()!= null && answerSub2Dto.getAnswers().get(0) != null){
                    answer1String = Optional.of(answerSub2Dto.getAnswers().get(0).toLowerCase()).orElse("");
                }

                if(answerSub2Dto.getAnswers()!=null && answerSub2Dto.getAnswers().size() >1){
                    answer2String = Optional.of(answerSub2Dto.getAnswers().get(1).toLowerCase()).orElse("");
                }

                if(!answer1String.equals("") && !answer2String.equals("")){
                    Answer answer1 = answerService.findById(answer1String);
                    Answer answer2 = answerService.findById(answer2String);

                    // check if all the answers are correct then add the list of the correctAnswer
                    if(answer1.getIsCorrect() == 1 && answer2.getIsCorrect() ==1){
                        Answer[] answers = {answer1,answer2};
                        correctAnswer.add(answers);
                    }
                }
            });
        }else if(!subtest2.isEmpty() && subtest2.get(0).getQuestionId().contains("cfit2")){

            List<String> answerDtoId =
                    subtest2.stream()
                            .filter(answer -> !answer.getAnswers().isEmpty())
                            .map(answerDto->answerDto.getAnswers().get(0))
                            .collect(Collectors.toList());

            for (String answerDto : answerDtoId) {

                Answer answer = answerService.findById(answerDto);
                if(answer.getIsCorrect()==1){
                    Answer[] answers = {answer};
                    correctAnswer.add(answers);
                }
            }

        }

        int points = correctAnswer.size();

        Map<Integer,Integer> resultMapping;


        if(ageInMonth < 95){
            resultMapping = getAgeResultKeyValue("7.4-7.10");
        }else if(ageInMonth < 100){
            resultMapping = getAgeResultKeyValue("7.11-8.3");
        }else if(ageInMonth < 107){
            resultMapping = getAgeResultKeyValue("8.4-8.10");
        }else if(ageInMonth < 112){
            resultMapping = getAgeResultKeyValue("8.11-9.3");
        }else if(ageInMonth < 119){
            resultMapping = getAgeResultKeyValue("9.4-9.10");
        }else if(ageInMonth < 124){
            resultMapping = getAgeResultKeyValue("9.11-10.3");
        }else if(ageInMonth < 131){
            resultMapping = getAgeResultKeyValue("10.4-10.10");
        }else if(ageInMonth < 136){
            resultMapping = getAgeResultKeyValue("10.11-11.3");
        }else if(ageInMonth < 143){
            resultMapping = getAgeResultKeyValue("11.4-11.10");
        }else if(ageInMonth < 148){
            resultMapping = getAgeResultKeyValue("11.11-12.3");
        }else if(ageInMonth < 155){
            resultMapping = getAgeResultKeyValue("12.4-12.10");
        }else if(ageInMonth < 161){
            resultMapping = getAgeResultKeyValue("12.11-13.3");
        }else if(ageInMonth < 168){
            resultMapping = getAgeResultKeyValue("13.4-13.11");
        }else if(ageInMonth < 180){
            resultMapping = getAgeResultKeyValue("14.0-14.11");
        }else if(ageInMonth < 192){
            resultMapping = getAgeResultKeyValue("15.0-15.11");
        }else if(ageInMonth < 204){
            resultMapping = getAgeResultKeyValue("16.0-16.11");
        }else{
            resultMapping = getAgeResultKeyValue("17.0-more");
        }
        System.out.println(points+","+correctAnswer.size());

        // the result output(iq)
        if(points > 50){
            points = 50;
        }
        int iq = resultMapping.get(points);

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
            predicate = "boderline mental retardation";
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
        testResult.setResultCalculation("jumlah benar:"+points);
        testResult.setTest(testService.findTestByInternalName("cfit3"));
        testResult.setResult(getResult());
//        testResultRepository.save(testResult);
        logger.info("username: '"+username+"' CFIT answer calculated successfully");
        return testResult;
    }

    private Map<Integer,Integer> getAgeResultKeyValue(String ageRange){

        Resource resource = resourceLoader.getResource(cfitPkuLocation);

        try(Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(resource.getInputStream())))){
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
            throw new RuntimeException("error loading file cfit.pku: "+e.getMessage());
        }
    }
}
