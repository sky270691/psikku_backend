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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class MinatBakatResultTestCalculator implements UniqueResultTestCalculator {

    private final Logger logger;
    private final AnswerService answerService;
    private final UserService userService;
    private final TestService testService;
    private final String name;

    private String result;

    @Autowired
    public MinatBakatResultTestCalculator(AnswerService answerService, UserService userService, TestService testService) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.answerService = answerService;
        this.userService = userService;
        this.testService = testService;
        this.result = "";
        this.name = "bakat";
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public TestResult calculateNewResult(List<SubmittedAnswerDto> submittedAnswerDtoList) {

        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userService.findByUsername(username);
        int userAgeInMonth = user.getAge();


        List<SubmittedAnswerDto> an =
                submittedAnswerDtoList.stream()
                        .filter(st1 -> {
                            String[] questionIdSplit = st1.getQuestionId().split("_");
                            return Integer.parseInt(questionIdSplit[1]) == 1;
                        })
                        .collect(Collectors.toList());

        List<SubmittedAnswerDto> ge =
                submittedAnswerDtoList.stream()
                        .filter(st2 -> {
                            String[] questionIdSplit = st2.getQuestionId().split("_");
                            return Integer.parseInt(questionIdSplit[1]) == 2;
                        })
                        .collect(Collectors.toList());

        List<SubmittedAnswerDto> ra =
                submittedAnswerDtoList.stream()
                        .filter(st3 -> {
                            String[] questionIdSplit = st3.getQuestionId().split("_");
                            return Integer.parseInt(questionIdSplit[1]) == 3;
                        })
                        .peek(zrTest -> zrTest.setQuestionId(zrTest.getQuestionId().toLowerCase()))
                        .collect(Collectors.toList());

        List<SubmittedAnswerDto> zr = submittedAnswerDtoList;
        zr.removeAll(an);
        zr.removeAll(ge);
        zr.removeAll(ra);
        zr.forEach(zrTest -> zrTest.setQuestionId(zrTest.getQuestionId().toLowerCase()));

        int anCorrectAnswers = 0;
        int geCorrectAnswers = 0;
        int raCorrectAnswers = 0;
        int zrCorrectAnswers = 0;

        int anMaxCorrectByAge;
        int geMaxCorrectByAge;
        int raMaxCorrectByAge;
        int zrMaxCorrectByAge;

//        String[] answerDtoQuestionIdSplit = submittedAnswerDtoList.get(0).getQuestionId().split("_");

        String[] answerDtoQuestionIdSplit = submittedAnswerDtoList.stream()
                .findAny()
                .orElseGet(() -> {
                    SubmittedAnswerDto a = new SubmittedAnswerDto();
                    a.setQuestionId("bakat_1_1");
                    return a;
                }).getQuestionId().split("_");

        String testName = answerDtoQuestionIdSplit[0];
        List<Answer> allMinatBakatAnswerFromDb = answerService.findByIdStartingWith(testName);

        // calculate "an" answer

        if(!an.isEmpty()) {
            for (SubmittedAnswerDto anAnswerDto : an) {
                if (anAnswerDto.getAnswers() != null && !anAnswerDto.getAnswers().isEmpty()) {
                    for (Answer ansFromDb : allMinatBakatAnswerFromDb) {
                        if (anAnswerDto.getAnswers().get(0).equalsIgnoreCase(ansFromDb.getId())) {
                            if (ansFromDb.getIsCorrect() == 1) {
                                anCorrectAnswers++;
                            }
                        }
                    }
                }
            }
        }


        // calculate "ge" answer need to sum the answer category 1 or 2
        if(!ge.isEmpty()) {
            for (SubmittedAnswerDto geAnswerDto : ge) {
                if (geAnswerDto.getAnswers() != null && !geAnswerDto.getAnswers().isEmpty()) {
                    for (Answer ansFromDb : allMinatBakatAnswerFromDb) {
                        if (ansFromDb.getId().startsWith(testName + "_2")) {
                            if (ansFromDb.getAnswerContent().equals(geAnswerDto.getAnswers().get(0))) {
                                geCorrectAnswers += Integer.parseInt(ansFromDb.getAnswerCategory());
                            }
                        }
                    }
                }
            }
        }


        // calculate "ra" answer
        if(!ra.isEmpty()) {
            for (SubmittedAnswerDto raAnswerDto : ra) {
                if (raAnswerDto.getAnswers() != null && !raAnswerDto.getAnswers().isEmpty()) {
                    for (Answer ansFromDb : allMinatBakatAnswerFromDb) {
                        if (ansFromDb.getAnswerContent().equals(raAnswerDto.getAnswers().get(0))) {
                            raCorrectAnswers++;
                        }
                    }
                }
            }
        }

        // calculate "zr" answer
        if(!zr.isEmpty()) {
            for (SubmittedAnswerDto zrAnswerDto : zr) {
                if (zrAnswerDto.getAnswers() != null && !zrAnswerDto.getAnswers().isEmpty()) {
                    for (Answer ansFromDb : allMinatBakatAnswerFromDb) {
                        if (ansFromDb.getAnswerContent().equals(zrAnswerDto.getAnswers().get(0))) {
                            zrCorrectAnswers++;
                        }
                    }
                }
            }
        }

        if (userAgeInMonth < 156) {
            raMaxCorrectByAge = geMaxCorrectByAge = anMaxCorrectByAge = 15;
            zrMaxCorrectByAge = 16;
        } else if (userAgeInMonth < 168) {
            anMaxCorrectByAge = 15;
            geMaxCorrectByAge = 16;
            raMaxCorrectByAge = 17;
            zrMaxCorrectByAge = 18;
        } else if (userAgeInMonth < 192) {
            anMaxCorrectByAge = 16;
            geMaxCorrectByAge = raMaxCorrectByAge = 17;
            zrMaxCorrectByAge = 18;
        } else {
            anMaxCorrectByAge = geMaxCorrectByAge = raMaxCorrectByAge = zrMaxCorrectByAge = 20;
        }

        //final output per subtest
        if (anCorrectAnswers > anMaxCorrectByAge) {
            anCorrectAnswers = anMaxCorrectByAge;
        }
        if (raCorrectAnswers > raMaxCorrectByAge) {
            raCorrectAnswers = raMaxCorrectByAge;
        }
        if (geCorrectAnswers > geMaxCorrectByAge) {
            geCorrectAnswers = geMaxCorrectByAge;
        }
        if (zrCorrectAnswers > zrMaxCorrectByAge) {
            zrCorrectAnswers = zrMaxCorrectByAge;
        }

        double anPercentage = (double) anCorrectAnswers / (double) anMaxCorrectByAge * 100;
        double gePercentage = (double) geCorrectAnswers / (double) geMaxCorrectByAge * 100;
        double raPercentage = (double) raCorrectAnswers / (double) raMaxCorrectByAge * 100;
        double zrPercentage = (double) zrCorrectAnswers / (double) zrMaxCorrectByAge * 100;

        double testEksak = (double) (geCorrectAnswers + raCorrectAnswers) / (geCorrectAnswers + raCorrectAnswers + anCorrectAnswers + zrCorrectAnswers) * 100;
        double testNonEksak = (double) (anCorrectAnswers + zrCorrectAnswers) / (geCorrectAnswers + raCorrectAnswers + anCorrectAnswers + zrCorrectAnswers) * 100;
        double testLiterasi = (double) (anCorrectAnswers + geCorrectAnswers) / (anCorrectAnswers + geCorrectAnswers + raCorrectAnswers + zrCorrectAnswers) * 100;
        double testNumerasi = (double) (raCorrectAnswers + zrCorrectAnswers) / (anCorrectAnswers + geCorrectAnswers + raCorrectAnswers + zrCorrectAnswers) * 100;

        System.out.println("eksak: " + String.format("%.2f", testEksak));
        System.out.println("non Eksak: " + String.format("%.2f", testNonEksak));
        System.out.println("literasi: " + String.format("%.2f", testLiterasi));
        System.out.println("numerasi: " + String.format("%.2f", testNumerasi));

        double eksak = gePercentage + raPercentage;
        double nonEksak = anPercentage + zrPercentage;
        double literasi = anPercentage + gePercentage;
        double numerasi = raPercentage + zrPercentage;

        StringBuilder sb = new StringBuilder();
//        sb.append("Minat bakat").append("\n");
        sb.append("an:").append((int) anPercentage).append(",");
        sb.append("ge:").append((int) gePercentage).append(",");
        sb.append("ra:").append((int) raPercentage).append(",");
        sb.append("zr:").append((int) zrPercentage).append(",");
        sb.append("eksak:").append(String.format("%.2f", testEksak)).append(",");
        sb.append("nonEksak:").append(String.format("%.2f", testNonEksak)).append(",");
        sb.append("literasi:").append(String.format("%.2f", testLiterasi)).append(",");
        sb.append("numerasi:").append(String.format("%.2f", testNumerasi));

        setResult(sb.toString());

        TestResult testResult = new TestResult();
        testResult.setUser(user);
        testResult.setTest(testService.findTestByInternalName(testName));
        testResult.setResult(getResult());
        testResult.setResultCalculation("an:" + anPercentage + ",ge:" + gePercentage + ",ra:" + raPercentage + ",zr:" + zrPercentage);
//        testResultRepository.save(testResult);
        logger.info("username: '" + username + "' MINATBAKAT answer calculated successfully");
        return testResult;

//        boolean exact = false;
//        boolean nonExact = false;
//        boolean literate = false;
//        boolean numerate = false;
//
//
//        //exact vs non exact
//        if((gePercentage + raPercentage) > (anPercentage + zrPercentage)){
//            exact = true;
//        }else if((gePercentage + raPercentage) < (anPercentage + zrPercentage)){
//            nonExact = true;
//        }
//
//        if((anPercentage + gePercentage) > (raPercentage + zrPercentage)){
//            literate = true;
//        }else if((anPercentage + gePercentage) < (raPercentage + zrPercentage)){
//            numerate = true;
//        }


        // define age range
//        String ageRange = "";
//        if(userAgeInMonth < 156){
//            ageRange ="12yo";
//        }else if(userAgeInMonth < 168){
//            ageRange = "13yo";
//        }else if(userAgeInMonth < 192){
//            ageRange = "14-15yo";
//        }else if(userAgeInMonth < 204){
//            ageRange = "16yo";
//        }else if(userAgeInMonth < 216){
//            ageRange = "17yo";
//        }else if(userAgeInMonth < 228){
//            ageRange = "18yo";
//        }else if(userAgeInMonth < 252){
//            ageRange = "19-20yo";
//        }else{
//            ageRange = "21-25yo";
//        }

//        // "an" answer map
//        Map<Integer, Integer> anMapKeyValueResult = getAgeResultKeyValue(ageRange,"an");
//        int anPoint = anMapKeyValueResult.get(anCorrectAnswers);
//
//        // "ge" answer map
//        Map<Integer, Integer> geMapKeyValueResult = getAgeResultKeyValue(ageRange,"ge");
//        int gePoint = geMapKeyValueResult.get(anCorrectAnswers);
//
//        // "ra" answer map
//        Map<Integer, Integer> raMapKeyValueResult = getAgeResultKeyValue(ageRange,"ra");
//        int raPoint = raMapKeyValueResult.get(anCorrectAnswers);
//
//        // "zr" answer map
//        Map<Integer, Integer> zrMapKeyValueResult = getAgeResultKeyValue(ageRange,"zr");
//        int zrPoint = zrMapKeyValueResult.get(anCorrectAnswers);

    }

    @Override
    public String getResult() {
        return result;
    }

    @Override
    public void setResult(String result) {
        this.result = result;
    }

    //    private Map<Integer,Integer> getAgeResultKeyValue(String ageRange, String subtestName){
//        try(Scanner scanner = new Scanner(new BufferedReader(new FileReader("src/main/resources/static/testresultdata/bakat/minatbakat.pku")))){
//
//            Map<Integer,Integer> subTestMap = new LinkedHashMap<>();
//            String testName;
//            while(scanner.hasNextLine()){
//                testName = scanner.nextLine();
//                if(testName.equals(ageRange)){ // age range String
//                    String parameter;
//                    while(scanner.hasNextLine()){
//                        parameter = scanner.nextLine();
//                        if(parameter.equals("---")){
//                            break;
//                        }
//                        // the startwith value should be same with the subtestName parameter
//                        if(parameter.startsWith(subtestName)){
//                            String[] keyValueSplit = parameter.split(",");
//                            subTestMap.put(Integer.parseInt(keyValueSplit[1]),Integer.parseInt(keyValueSplit[2]));
//                        }
//                    }
//                }
//            }
//            return subTestMap;
//        }catch (IOException e){
//            throw new RuntimeException(getClass().getEnclosingMethod()+" "+getClass().getEnclosingMethod()
//                    +"-->  error to find the calculation file");
//        }
//    }
}
