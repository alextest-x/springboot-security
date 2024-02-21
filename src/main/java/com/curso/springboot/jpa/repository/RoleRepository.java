package com.curso.springboot.jpa.repository;


import com.curso.springboot.jpa.entities.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long>{

    //metodo con una consulta personalizada

    Optional<Role> findByName(String name);



}
