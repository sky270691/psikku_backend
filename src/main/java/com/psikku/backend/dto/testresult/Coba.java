package com.psikku.backend.dto.testresult;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class Coba {


    public static void main(String[] args) {

//        Map<String,Integer> testMap = new TreeMap<>();
//        testMap.put("s",4);
//        testMap.put("a",2);
//        testMap.put("c",12);
//        testMap.put("p",9);
//        testMap.put("x",10);
//        testMap.put("b",2);
//        System.out.println(testMap);
//        List<Map.Entry<String,Integer>> test =testMap.entrySet().stream()
//                .sorted((v1,v2)->v1.getValue()-v2.getValue())
//                .skip(4)
//                .collect(Collectors.toList());
//
        String bangke = "bangke";
        System.out.println(bangke.substring(0,1));
    }


    private static String generateAlphaNumeric(int stringLength){
        String alphaLower = "abcdefghijklmnopqrstuvwxyz";
        String alphaUpper = alphaLower.toUpperCase();
        String number = "0123456789";

        String combination = alphaLower + alphaUpper + number;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stringLength; i++) {
            Random random = new SecureRandom();
            sb.append(combination.charAt(random.nextInt(combination.length())));
        }
        return sb.toString();
    }

}
