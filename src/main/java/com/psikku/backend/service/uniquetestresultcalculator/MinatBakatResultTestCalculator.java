package com.psikku.backend.service.uniquetestresultcalculator;

import com.psikku.backend.dto.test.SubmittedAnswerDto;
import com.psikku.backend.entity.Answer;
import com.psikku.backend.entity.User;
import com.psikku.backend.repository.AnswerRepository;
import com.psikku.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class MinatBakatResultTestCalculator implements UniqueResultTestCalculator {

    //Todo
    // Make it same with the minat bakat test name
    private final String MINATBAKAT_TEST_NAME = "minatbakat";

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public void calculateNewResult(List<SubmittedAnswerDto> submittedAnswerDtoList) {

        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userRepository.findUserByUsername(username);
        int userAgeInMonth = user.getAge(LocalDate.now());


        List<SubmittedAnswerDto> minatBakatOnly = submittedAnswerDtoList.stream()
                                                                        .filter(answers -> answers.getQuestionId().contains("minat"))
                                                                        .collect(Collectors.toList());
        List<SubmittedAnswerDto> an =
                minatBakatOnly.stream()
                              .filter(st1 -> {
                                 String[] questionIdSplit = st1.getQuestionId().split("_");
                                 return Integer.parseInt(questionIdSplit[1]) == 1;
                              })
                              .collect(Collectors.toList());

        List<SubmittedAnswerDto> ge =
                minatBakatOnly.stream()
                                .filter(st2 -> {
                                    String[] questionIdSplit = st2.getQuestionId().split("_");
                                    return Integer.parseInt(questionIdSplit[1]) == 2;
                                })
                                .collect(Collectors.toList());

        List<SubmittedAnswerDto> ra =
                minatBakatOnly.stream()
                               .filter(st3 -> {
                                   String[] questionIdSplit = st3.getQuestionId().split("_");
                                   return Integer.parseInt(questionIdSplit[1]) == 3;
                               })
                               .collect(Collectors.toList());

        List<SubmittedAnswerDto> zr = minatBakatOnly;
        zr.removeAll(an);
        zr.removeAll(ge);
        zr.removeAll(ra);

        //Todo
        // setup percentage for outputting the result
        int anCorrectAnswers = 0;
        int geCorrectAnswers = 0;
        int raCorrectAnswers = 0;
        int zrCorrectAnswers = 0;

        int anMaxCorrectByAge = 0;
        int geMaxCorrectByAge = 0;
        int raMaxCorrectByAge = 0;
        int zrMaxCorrectByAge = 0;

        List<Answer> allMinatBakatAnswerFromDb = answerRepository.findByIdStartingWith(minatBakatOnly.get(0).getQuestionId());

        // calculate "an" answer
        for (SubmittedAnswerDto anAnswerDto : an) {
            for (Answer ansFromDb : allMinatBakatAnswerFromDb) {
                if (ansFromDb.getId().equals(anAnswerDto.getAnswers().get(0))) {
                    if (ansFromDb.getIsCorrect() == 1) {
                        anCorrectAnswers++;
                    }
                }
            }
        }


        // calculate "ge" answer need to sum the answer category 1 or 2
        for (SubmittedAnswerDto geAnswerDto : ge) {
            for (Answer ansFromDb : allMinatBakatAnswerFromDb) {
                if(ansFromDb.getId().startsWith(MINATBAKAT_TEST_NAME+"_2")){
                    if(ansFromDb.getAnswerContent().contains(geAnswerDto.getAnswers().get(0))){
                        geCorrectAnswers += Integer.parseInt(ansFromDb.getAnswerCategory());
                    }
                }
            }
        }


        // calculate "ra" answer
        for (SubmittedAnswerDto raAnswerDto : ra){
            for(Answer ansFromDb : allMinatBakatAnswerFromDb){
                if(ansFromDb.getId().equals(raAnswerDto.getAnswers().get(0))){
                    String[] splitAnsFromDbContent = ansFromDb.getAnswerContent().split("");
                    List<Integer> dbAnsList = new ArrayList<>();
                    for (String s : splitAnsFromDbContent) {
                        dbAnsList.add(Integer.parseInt(s));
                    }
                    List<Integer> userAnsList = new ArrayList<>();
                    String[] splitAnsFromUser = raAnswerDto.getAnswers().get(0).split("");
                    for (String s : splitAnsFromUser) {
                        userAnsList.add(Integer.parseInt(s));
                    }
                    dbAnsList.sort(Comparator.naturalOrder());
                    userAnsList.sort(Comparator.naturalOrder());

                    if(Arrays.equals(dbAnsList.toArray(), userAnsList.toArray())){
                        raCorrectAnswers++;
                    }
                }
            }
        }

        // calculate "zr" answer
        for (SubmittedAnswerDto zrAnswerDto : zr){
            for(Answer ansFromDb : allMinatBakatAnswerFromDb){
                if(ansFromDb.getId().equals(zrAnswerDto.getAnswers().get(0))){
                    String[] splitAnsFromDbContent = ansFromDb.getAnswerContent().split("");
                    List<Integer> dbAnsList = new ArrayList<>();
                    for (String s : splitAnsFromDbContent) {
                        dbAnsList.add(Integer.parseInt(s));
                    }
                    List<Integer> userAnsList = new ArrayList<>();
                    String[] splitAnsFromUser = zrAnswerDto.getAnswers().get(0).split("");
                    for (String s : splitAnsFromUser) {
                        userAnsList.add(Integer.parseInt(s));
                    }
                    dbAnsList.sort(Comparator.naturalOrder());
                    userAnsList.sort(Comparator.naturalOrder());

                    if(Arrays.equals(dbAnsList.toArray(), userAnsList.toArray())){
                        zrCorrectAnswers++;
                    }
                }
            }
        }

        if(userAgeInMonth < 156){
            raMaxCorrectByAge = geMaxCorrectByAge = anMaxCorrectByAge = 15;
            zrMaxCorrectByAge = 16;
        }else if(userAgeInMonth < 168){
            anMaxCorrectByAge = 15;
            geMaxCorrectByAge = 16;
            raMaxCorrectByAge = 17;
            zrMaxCorrectByAge = 18;
        }else if(userAgeInMonth < 192){
            anMaxCorrectByAge = 16;
            geMaxCorrectByAge = raMaxCorrectByAge = 17;
            zrMaxCorrectByAge = 18;
        }else {
            anMaxCorrectByAge = geMaxCorrectByAge = raMaxCorrectByAge = zrMaxCorrectByAge = 20;
        }

        //final output per subtest
        double anPercentage = (double) anCorrectAnswers / (double) anMaxCorrectByAge;
        double gePercentage = (double) geCorrectAnswers / (double) geMaxCorrectByAge;
        double raPercentage = (double) raCorrectAnswers / (double) raMaxCorrectByAge;
        double zrPercentage = (double) zrCorrectAnswers / (double) zrMaxCorrectByAge;

        boolean exact = false;
        boolean nonExact = false;
        boolean literate = false;
        boolean numerate = false;


        //exact vs non exact
        if((gePercentage + raPercentage) > (anPercentage + zrPercentage)){
            exact = true;
        }else if((gePercentage + raPercentage) < (anPercentage + zrPercentage)){
            nonExact = true;
        }

        if((anPercentage + gePercentage) > (raPercentage + zrPercentage)){
            literate = true;
        }else if((anPercentage + gePercentage) < (raPercentage + zrPercentage)){
            numerate = true;
        }


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
