package com.psikku.backend.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AgeValidator.class)
public @interface AgeValidation {

    String message() default "{com.psikku.backend.testtype.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default{};
}
