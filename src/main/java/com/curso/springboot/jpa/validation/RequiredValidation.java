package com.curso.springboot.jpa.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;


//<> generic tipo  de anotacion, tipo de campo que se va validar String
public class RequiredValidation implements ConstraintValidator<IsRequired, String> {

/*
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value != null && !value.isEmpty() && !value.isBlank()){
            return true;
        }
        return false;
    }

 */


@Override
public boolean isValid(String value, ConstraintValidatorContext context) {
    //en una sola linea optimizando
    //return  (value != null && !value.isEmpty() && !value.isBlank());

    //de otra forma hacer validaciones con anotaciones
    //valida un string que sea diferente de null que no tanga espacios en blanco
    return StringUtils.hasText(value);


    }

}


