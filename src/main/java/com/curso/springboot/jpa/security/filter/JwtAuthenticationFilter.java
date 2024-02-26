package com.curso.springboot.jpa.security.filter;

import com.curso.springboot.jpa.entities.User;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.curso.springboot.jpa.security.TokenJwtConfig.*;

/*
  JwtAuthenticationFilter
  Autentificar y Genera el token y regresar al cliente una vez que ingresa el username y el password

*/
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    //implementamos el constructor para el atributo authenticationManager
    private AuthenticationManager authenticationManager;


    /*
        We need a signing key, so we'll create one just for this example. Usually
         the key would be read from your application configuration instead.
          SecretKey key = Jwts.SIG.HS256.key().build();

          Genera el token
          String jws = Jwts.builder().subject("Joe").signWith(key).compact();
     */

    //generando la llave secreta final no se puede modificar ponemos Key importamos de java security
    /*
    // lo comentamos porque lo pasamos a una clase de configuracion
    //private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    //private static final String PREFIX_TOKEN = "Bearer ";
    //private static final String HEADER_AUTHORIZATION= "Authorization";
    */


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    //se implementar un  metodo con el overrider attemptAuthentication
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        // obtiene el username y password se envia un Json
        // hay que comvertirlo a un objeto de java con el User que lo importanmos de las clase entity

        //user de la clase entity
        User user = null;
        String username = null;
        String password = null;

        //obtener el user con el object mapper().
        //viene el json en el request y lo obtenemos con getInputStream()
        //User.class el maper lo convierte a un objeto de tipo user


        try {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            //poblamos los datos del obtejo user y los pasamos
            //en el username y el password para que authentifique los datos en el authenticationToken
            username = user.getUsername();
            password = user.getPassword();

        } catch (StreamReadException e) {  //error de lectura del stream
            e.printStackTrace();

        } catch (DatabindException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        //authenticationToken tiene le username y el password
        //el metodo authenticate va al JpaUserDetalisService y lo valida en la bd
        return authenticationManager.authenticate(authenticationToken);
    }

    // ponemos un nuevo metodo con overrrider ya que actualizamos en el pom.xml las librerias de https://github.com/jwtk/jjwt
    // https://jwt.io/libraries?language=Java
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {

          //obtenemos el username para pasarlo al token user de security
          org.springframework.security.core.userdetails.User
                  user = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
          String username = user.getUsername();
          //obteniendo los roles para pasarlos a los claims que es informacion extra para generar el token
          Collection<? extends GrantedAuthority> roles =  authResult.getAuthorities();

          //no poner informacion sensible password
          // authorities es un arreglo de roles hay que convertirlo a un json

          Claims claims = Jwts.claims()
                  //.add("authorities", roles)//aqui lo pasamos como un objeto hay ponerlo en un json
                  .add("authorities", new ObjectMapper().writeValueAsString(roles)) //pasamos los roles como un json
                  .add("username", username)
                  .build();



        //Genera el token
        String token = Jwts.builder()
                .subject(username)
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + 3600000))  //expira desde que empieza + una hora en milisegundos
                .issuedAt(new Date()) //fecha actual
                .signWith(SECRET_KEY)
                .compact();

        //regresando el token al cliente a la vista
        //response.addHeader("Authorization", "Bearer" + token);

        response.addHeader(HEADER_AUTHORIZATION,  PREFIX_TOKEN + token);


        //lo pasamos como una repuesta en json usamos el map

        Map<String, String> body = new HashMap<>();
        body.put("token", token);
        body.put("username", username);
        //%s pone el valor que le pasamos por parametro en username
        body.put("message", String.format("Hola %s has iniciado sesion con exito! ", username));

         //poenemos el json en la respuesta
         //pero el map esta en string  lo convertimos a un json
         response.getWriter().write(new ObjectMapper().writeValueAsString(body));
         response.setContentType(CONTENT_TYPE);
         //response.setContentType("application/json"); //la pasamos a una variable CONTENT_TYPE

         response.setStatus(200);
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException, ServletException {
        Map<String, String> body = new HashMap<>();
        body.put("message", "Error en la autenticacion username o password incorrectos!");
        body.put("error", failed.getMessage());

        //pasamos el mensaje el body a un json
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401); //no esta autorizado
        response.setContentType(CONTENT_TYPE);


    }
}
