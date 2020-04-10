package com.psikku.backend.dto.testresult;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Locale;

public class Coba {


    public static void main(String[] args) {
        LocalDate localDate1 = LocalDate.parse("1990-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate localDate2 = LocalDate.parse("2007-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Calendar calendar = Calendar.getInstance(Locale.US);
        long year = ChronoUnit.DAYS.between(localDate1,localDate2);
        System.out.println(year);
    }

}
