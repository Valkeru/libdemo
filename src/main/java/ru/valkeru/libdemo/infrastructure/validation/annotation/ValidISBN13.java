package ru.valkeru.libdemo.infrastructure.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.valkeru.libdemo.infrastructure.validation.validator.ISBNValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {
    ISBNValidator.class
})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidISBN13 {

    String message() default "Invalid ISBN";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
