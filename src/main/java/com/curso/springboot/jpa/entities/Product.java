package com.curso.springboot.jpa.entities;


import com.curso.springboot.jpa.validation.IsExistDb;
import com.curso.springboot.jpa.validation.IsRequired;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name= "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @IsRequired
    @IsExistDb
    private String sku;



    //@NotBlank  // valida que no este vacio y que no tenga espacios en blanco
    //@NotEmpty  //para string
    //@NotEmpty(message = "{NotEmpty.product.name}")    //mensaje personalizado desde el properties
    @IsRequired (message = "{IsRequired.product.name}") //validando con anotaciones desde otra clase y el properties
    @Size(min=3, max=20)
    private String name;

    @Min(500)
    //@NotNull  //para objetos de cualquier tipo
    @NotNull(message = "{NotNull.product.price}")
    @Min(value=500, message = "{Min.product.price}")
    private Integer price;


    //@NotBlank(message = "{NotBlank.product.description}")
    @IsRequired // validando con anotacion desde una clase IsRequired
    private String description;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
