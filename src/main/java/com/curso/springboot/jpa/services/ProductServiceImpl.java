package com.curso.springboot.jpa.services;


import com.curso.springboot.jpa.entities.Product;
import com.curso.springboot.jpa.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
/*
   @Service logica de negocio
   trabaja con los datos que bienen de la Base de Datos

 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repository;


    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return (List<Product>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Product save(Product product) {
        return repository.save(product);
    }

    @Override
    @Transactional
    public Optional<Product> update(Long id, Product product) {
        Optional<Product> productOptional = repository.findById(id);

        //se actualiza en la base de datos
        if(productOptional.isPresent()) {
            Product productDb = productOptional.orElseThrow();

            productDb.setSku(product.getSku());
            productDb.setName(product.getName());
            productDb.setDescription(product.getDescription());
            productDb.setPrice(product.getPrice());

            //lo persistimos
            return Optional.of(repository.save(productDb));
        }
        return productOptional;
    }



    @Override
    @Transactional
    //public Optional<Product> delete(Product product) {
      public Optional<Product> delete(Long  id) {
        Optional<Product> productOptional= repository.findById(id);
        productOptional.ifPresent(productDb -> { //se elimina el de la base de datos
            repository.delete(productDb);
        });
         return productOptional;

    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsBySku(String sku) {
       return repository.existsBySku(sku);

    }

}
