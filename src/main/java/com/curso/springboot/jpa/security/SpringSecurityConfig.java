package com.curso.springboot.jpa.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {
    /*
       @Bean genera un componente spring con una instancia una referencia de BCryptPasswordEncoder
       un metodo que genera un componente spring que regresa un password encored
    */

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

     /*
        valida los request
        son publicos la ruta user los endpoint para listar y crear
        y lo demas requeire autentificacion
     */

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests( (authz) -> authz
            .requestMatchers("/api/users").permitAll()
            .anyRequest().authenticated())
            //valor secreto del lado del servidor login formulario token(csrf) evita vulnerabilidad vistas del servidor
            .csrf(config -> config.disable())
            .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            /* la sesion se guarda en la session http sin estado aqui no
               la sesion se envia en el token cada vez que se haga un request con alguno datos del usuario
               para que se pueda auhtenticar
               no queda autenticado en la sesion del http por eso esta sin estado
             */
            .build();


    }
}
