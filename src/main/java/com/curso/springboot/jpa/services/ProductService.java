package com.curso.springboot.jpa.services;

import com.curso.springboot.jpa.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> findAll();

    Optional<Product> findById(Long id);  //busca por id

    Product save(Product product);

    Optional<Product> update(Long id, Product product);

    Optional<Product> delete(Long id);

    //Optional<Product> delete(Product product);

    boolean existsBySku(String sku);

}
