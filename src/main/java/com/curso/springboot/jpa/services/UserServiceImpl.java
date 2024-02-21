package com.curso.springboot.jpa.services;

import com.curso.springboot.jpa.entities.Role;
import com.curso.springboot.jpa.entities.User;
import com.curso.springboot.jpa.repository.RoleRepository;
import com.curso.springboot.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    //busca el rol por el nombre cunado guardamos al usuario
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) repository.findAll();
    }


    @Override
    @Transactional
    public User save(User user) {
        //busca por el nombre en la bd en el campo ROLE_USER
        Optional<Role> optionalRole = roleRepository.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();

        //le pasamos el rol al usuario si esta presente
        //optionalRole.ifPresent(role -> roles.add(role));
        optionalRole.ifPresent(roles::add);   //optimizando

        if(user.isAdmin()){
            //buscamos en la base de datos
            Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
            optionalRoleAdmin.ifPresent(role ->  roles.add(role));
        }


        //le pasamos al user la lista
        //user.setRoles(roles);

        // String passwordEncoder = user.getPassword();
        //codificar el password
        //String passwordEncoder = passwordEncoder.encode(user.getPassword());

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);



    }





}
