package com.curso.springboot.jpa.controllers;

//import com.curso.springboot.jpa.ProductValidation;
import com.curso.springboot.jpa.entities.Product;
import com.curso.springboot.jpa.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@CrossOrigin(origins = "http://localhost:4200", originPatterns = "*")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService service;


    // @Autowired
    //private ProductValidation validation;

    //se ejecuta despues del llamado de un metodo

    //regresa una lista
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<Product> list(){
        return service.findAll();
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> view(@PathVariable Long id){
        Optional<Product> productOptional = service.findById(id);
        //validando si el producto esta presente

        if(productOptional.isPresent()){
            return ResponseEntity.ok(productOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@Valid @RequestBody Product product, BindingResult result){
        //Product productNew = service.save(product);
        //return (ResponseEntity<Product>) ResponseEntity.status(HttpStatus.CREATED).body(productNew);


        //se comenta porque se va a validar con anotaciones
        //validation.validate(product, result); //metodo implementado en ProductValidation que valida
        if(result.hasFieldErrors()){
            return  validation(result);

        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(product));

    }


    //va a la bd busca con el objeto completo obtiene los datos con los campos actualizados.
    //BindingResult tiene que estar junto al objeto que vamos a validar
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Product product, BindingResult result, @PathVariable Long id){
        //product.setId(id);


       //se comenta porque se va a validar con anotaciones
       // validation.validate(product, result); //metodo implementado en ProductValidation que valida

        if(result.hasFieldErrors()){
            return  validation(result);

        }
        Optional<Product> productOptional = service.update(id, product);
        if(productOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(productOptional.orElseThrow());
        }

        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        //Product product = new Product();
        //product.setId(id);
        //Optional<Product> productOptional = service.delete(product);

        Optional<Product> productOptional = service.delete(id);

        //validando si el producto esta presente
        if(productOptional.isPresent()){
            return ResponseEntity.ok(productOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> validation(BindingResult result) {

        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            //put pone (key, valor)
            errors.put(err.getField(), "El Campo " +  err.getField()+ " "+ err.getDefaultMessage());
        });
        //return ResponseEntity.badRequest().body(errors);
        //return ResponseEntity.status(400).body(errors);
        //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        return ResponseEntity.badRequest().body(errors);

    }


}
