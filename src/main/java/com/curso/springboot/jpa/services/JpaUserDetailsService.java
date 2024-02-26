package com.curso.springboot.jpa.services;


/*implementar una interface UserDetailsService
* de springSecurity
* */

/*
Es un servicio que permite buscar la usuario por el userbame cunado usa el login
se implementa como se obtiene el usuario
de una BD JPA, JDBC, Otra  API otro servicio
implemnetar una interface implements UserDetailsService
*/

import com.curso.springboot.jpa.entities.User;
import com.curso.springboot.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JpaUserDetailsService implements UserDetailsService  {


    /*
        username viene del login del formulario del frontend
     */

    @Autowired
    private UserRepository repository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userOptional = repository.findByUsername(username);

        //%s nombre de parametro que pone el valor de username el nombre del usuario
        if(userOptional.isEmpty()){
            throw new UsernameNotFoundException(String.format("Username %s no existe en el sistema!",  username));
        }

        //si esta presente se obtiene la instancia del usuario
        User user= userOptional.orElseThrow();

        //obtener los roles
        //hay que regresar el user desde el service
        //entonces hay que pasarle una lista de roles de tipo GrandAuthority

        //con el stream convertimos a una lista de roles a grandAuthorities
        //que viene de user.getRoles()
        //con el metodo map convertimos el map es un string
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList()); //convierte de una lista a un string con el map

        //lo convertimos el map a una lista de GrantedAuthority List<GrantedAuthority>

        //aqui usamos el user del spring security
        //creamos la instacia con new y le agrgamos los parametros user.getUsername(),
        return new org.springframework.security.core.userdetails.User
                (user.getUsername(),
                        user.getPassword(),     //lo encripta lo compara con el password que esta en la bd
                        user.isEnabled(),
                        true,    //la cuenta no expira
                        true,                    //las credenciales no expiran
                        true,                    //la cuenta no se bloquea
                        authorities);            //los roles que estan en el map role ->

    }

}
