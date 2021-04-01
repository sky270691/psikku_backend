package com.psikku.backend.dto.testresult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class Coba {


    public static void main(String[] args) {

        LocalDate d = LocalDate.of(2020,10,15);
        long test = ChronoUnit.DAYS.between(LocalDate.now(),d);
        System.out.println(test);

//        Map<String,Integer> testMap = new HashMap<>();
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
//        Map<String,Integer> test =
//                testMap.entrySet().stream()
//                    .sorted((x,y)->x.getValue()-y.getValue())
//                    .collect(Collectors.toMap(x->x.getKey(),y->y.getValue(),(x,y)->x,()->new LinkedHashMap<>()));
//        System.out.println(test);

//
//
//        List<String> text;
//        String pathString = "C:/Users/langi/Desktop";
//        try {
//
//            Path readFilePath = Paths.get(pathString).resolve("biodata.txt");
//            System.out.println(Files.createDirectories(Paths.get(pathString).resolve("myNewFolder")));
//            text = Files.readAllLines(readFilePath);
//            String tinggi =text.stream()
//                    .filter(tulisan -> tulisan.startsWith("tinggi"))
//                    .findAny().orElse("");
//            int indexTinggi = text.indexOf(tinggi);
//            text.set(indexTinggi,"TINGGI: 180 CMs");
//            Path writeFile = Paths.get(pathString).resolve("bangkes.txt");
//            System.out.println(text);
//
//            Files.write(writeFile,text,StandardOpenOption.CREATE);

//            Path path = Paths.get(pathString).resolve("bengek.txt");
//            Files.createFile(path);
//            Files.write(path,text, StandardOpenOption.CREATE);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

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
