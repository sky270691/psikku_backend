package com.psikku.backend.dto.testresult;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Coba {


    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(new BufferedReader(new FileReader("src/main/resources/static/testresultdata/bakat/minatbakat.pku")))){

            Map<String,String> anMap = new LinkedHashMap<>();
            Map<String,String> geMap = new LinkedHashMap<>();
            Map<String,String> raMap = new LinkedHashMap<>();
            Map<String,String> zrMap = new LinkedHashMap<>();
            String testName;
            List<String> inside =new ArrayList<>();
            while(scanner.hasNextLine()){
                testName = scanner.nextLine();
                if(testName.equals("18yo")){ // age range String
                    String parameter;
                    while(scanner.hasNextLine()){
                        parameter = scanner.nextLine();
                        if(parameter.equals("---")){
                            break;
                        }
                        // the startwith value should be same with the subtestName parameter
                        if(parameter.startsWith("an")){
                            String[] keyValueSplit = parameter.split(",");
                            anMap.put(keyValueSplit[1],keyValueSplit[2]);
                        } else if(parameter.startsWith("ge")){
                            String[] keyValueSplit = parameter.split(",");
                            geMap.put(keyValueSplit[1],keyValueSplit[2]);
                        } else if(parameter.startsWith("ra")){
                            String[] keyValueSplit = parameter.split(",");
                            raMap.put(keyValueSplit[1],keyValueSplit[2]);
                        } else if(parameter.startsWith("zr")){
                            String[] keyValueSplit = parameter.split(",");
                            zrMap.put(keyValueSplit[1],keyValueSplit[2]);
                        }
                    }
                }
            }
            System.out.println("an: "+anMap);
            System.out.println("ge: "+geMap);
            System.out.println("ra: "+raMap);
            System.out.println("zr: "+zrMap);
        }catch (IOException e){
        }

//        int numOcorrectAnswer = 0;
//        List<Integer> correctAnswers = Arrays.asList(3,2,5);
//        List<Integer> inputAnswers = Arrays.asList(5,3,2);
//
//        correctAnswers.sort(Comparator.naturalOrder());
//        inputAnswers.sort(Comparator.naturalOrder());
//
//        System.out.println(Arrays.equals(correctAnswers.toArray(),inputAnswers.toArray()));



    }

}
