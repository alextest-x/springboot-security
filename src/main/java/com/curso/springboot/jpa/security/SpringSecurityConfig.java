package com.curso.springboot.jpa.security;

import com.curso.springboot.jpa.security.filter.JwtAuthenticationFilter;
import com.curso.springboot.jpa.security.filter.JwtValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig {

      //configurando el authenticationManager

    @Autowired
      private AuthenticationConfiguration authenticationConfiguration;

    //generamos con el bean el objeto authenticationManager y sincronizar con el filtro
    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


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
        //csrf es el valor secreto del lado del servidor login formulario token(csrf) evita vulnerabilidad vistas del servidor
     */

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests( (authz) -> authz
                        .requestMatchers(HttpMethod.GET,"/api/users").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/users/register").permitAll()

                /*
                        //se comenta porque usamos anotaciones  @PreAuthorize("hasRole('ADMIN')")
                        //en el UserController y en ProductController

                        //.requestMatchers(HttpMethod.POST,"/api/users").hasRole("ADMIN")
                        //.requestMatchers(HttpMethod.POST,"/api/products").hasRole("ADMIN")
                        //.requestMatchers(HttpMethod.GET,"/api/products", "/api/products/{id}")
                        //.hasAnyRole("ADMIN", "USER")
                        //.requestMatchers(HttpMethod.PUT,"/api/products/{id}").hasRole("ADMIN")
                        //.requestMatchers(HttpMethod.DELETE,"/api/products/{id}").hasRole("ADMIN")
                 */


              //las otras rutas son auntenticadas, despues del request se requiere auntentificacion
            .anyRequest().authenticated())
                //.addFilter(new JwtAuthenticationFilter(authenticationConfiguration.getAuthenticationManager()))
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtValidationFilter(authenticationManager()))
                .csrf(config -> config.disable())
                .cors( cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            /* la sesion se guarda en la session http sin estado aqui no
               la sesion se envia en el token cada vez que se haga un request con alguno datos del usuario
               para que se pueda auhtenticar
               no queda autenticado en la sesion del http por eso esta sin estado
             */
            .build();


    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "DELETED", "PUT"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        //se aplica desde la raiz de la aplicacion
        source.registerCorsConfiguration("/**", config);
        return source;

        }

        //filtro de spring que siempre se ejecuta en esta ruta de tipo corsfilter
        //que se asocia con el metodo de corsConfigurationSource()
        // y poner en le metodo filterChain abajo de .csrf(config -> config.disable())
        // .cors( cors -> cors.configurationSource())

    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter(){
        FilterRegistrationBean<CorsFilter> corsBean =
                new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));

        //se configura el corsBeans para dar una lata prioridad
        corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return corsBean;
    }




}
