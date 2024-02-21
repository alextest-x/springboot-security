package com.curso.springboot.jpa.repository;


import com.curso.springboot.jpa.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {


}
