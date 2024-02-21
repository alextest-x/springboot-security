package com.curso.springboot.jpa;

import com.curso.springboot.jpa.entities.Product;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


@Component
public class ProductValidation implements Validator {

    //da soprte a la clase que vamos a validar
    @Override
    public boolean supports(Class<?> clazz) {
        return Product.class.isAssignableFrom(clazz);
    }

    //se valida en el controlador
    //target es el objeto Product
    //Errors es el BindeResult en el controlador
    @Override
    public void validate(Object target, Errors errors) {
        Product product = (Product) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "null", "es requerido");
        //ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "NotBlank.product.description");

        //si usamos el if usamos el errors.rejectValue
        //aqui no podemos usar el ValidationUtils
        if(product.getDescription() == null || product.getDescription().isBlank()){
            errors.rejectValue("description", "null", "es requirida la descripcion");
        }

        if (product.getPrice() == null){
            errors.rejectValue("price", "null", "no puede ser nulo el precio");

        } else if (product.getPrice() < 500) {
            errors.rejectValue("price", "null", "debe ser mayor que 500!");

        }

    }


}
