package com.psikku.backend.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class AgeValidator implements ConstraintValidator<AgeValidation, LocalDate> {

    private final static double YEARS = 17;
    private final static double TOTAL_DAYS_PER_YEAR = 365.25;

    @Override
    public void initialize(AgeValidation constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext context) {
        return ChronoUnit.DAYS.between(localDate,LocalDate.now()) > (YEARS * TOTAL_DAYS_PER_YEAR);
    }
}
