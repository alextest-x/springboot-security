package com.curso.springboot.jpa.repository;


import com.curso.springboot.jpa.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {


    //metodo  para el ExistByUsername

    boolean existsByUsername(String username);

     //SpringSecurity
     //metodo para el JpaUserDetalisService

    //se optiene el objeto username
    Optional<User> findByUsername(String username);

}
