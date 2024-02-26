package com.curso.springboot.jpa.services;

import com.curso.springboot.jpa.entities.User;


import java.util.List;


public interface UserService {

    List<User> findAll();

    User save(User user);

    boolean existsByUsername(String username);
}
