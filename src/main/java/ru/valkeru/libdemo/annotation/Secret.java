package ru.valkeru.libdemo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Этой аннотацией должны быть помечены поля DTO, подлежащие маскированию в логе
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Secret {

    /**
     * Указывает, что строка должна быть маскирована полностью
     */
    boolean absolute() default false;
}
