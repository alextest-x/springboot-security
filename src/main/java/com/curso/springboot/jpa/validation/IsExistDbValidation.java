package com.curso.springboot.jpa.validation;

import com.curso.springboot.jpa.services.ProductService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//<tipo de dato, tipo de campo validar>
@Component
public class IsExistDbValidation implements ConstraintValidator<IsExistDb, String> {


    @Autowired
    private ProductService service;

    /*
    //valida en la base ded datos el campo
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && !service.existsBySku(value);
    }
    */

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(!service.existsBySku(value)){
            return true;
        }
        return false;
    }


}
