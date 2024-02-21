package com.curso.springboot.jpa.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = RequiredValidation.class)
@Retention(RetentionPolicy.RUNTIME) // se ejecuta en tiempo de ejecucion
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface IsRequired {

    //se copio de la inteface de NotBlank.java
    //hay que hacer una clase RequiredValidation
    //para validar usar la anotacion @Retention, @Target

    String message() default "Es requerido usando anotaciones";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
