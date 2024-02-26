package com.curso.springboot.jpa.validation;


import com.curso.springboot.jpa.services.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExistsByUsernameValidation implements ConstraintValidator<ExistsByUsername, String> {

    @Autowired
    private UserService service;



    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if(service == null){
            return true;
        }

        return !service.existsByUsername(username);

    }
}
