package com.curso.springboot.jpa.repository;

import com.curso.springboot.jpa.entities.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository <Product, Long>{

    //hace una consulta personalizada que valida el campo sku
    //metodo basado en el nombre
    boolean existsBySku(String sku);
}
