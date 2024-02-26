package com.curso.springboot.jpa.security.filter;

import com.curso.springboot.jpa.security.SimpleGrantedAuthorityJsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.curso.springboot.jpa.security.TokenJwtConfig.*;

public class JwtValidationFilter extends BasicAuthenticationFilter {

    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }


    //crear el metodo doFilterInternal con el overrrider
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws IOException, ServletException {

        //obtener el token que viene el Header
        String header = request.getHeader(HEADER_AUTHORIZATION);

        if (header == null || !header.startsWith(PREFIX_TOKEN)){
            chain.doFilter(request, response);
            return;
        }

        //quitar el prefijo del token del header
        String token = header.replace(PREFIX_TOKEN, "");

        //validar los claims

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            //obtener el username  de diferentes formas
            String username = claims.getSubject();
            //String username2 = (String) claims.get(username);


            //obtener los roles o authorities
            //pero viene en un obeject un json de tipo string
            //hay que pasarlo a un collection
            Object authoritiesClaims = claims.get("authorities");

            //espera una lista con el collection entonces ponemos Arrays.asList
            //pero en authorities hay que crear una clase personalizada un constructor personaliado a
            //SimpleGrantedAuthority en security SimpleGrantedAuthorityJsonCreator.java
            Collection<? extends GrantedAuthority> authorities = Arrays.asList(
                    new ObjectMapper()
                            .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
                            .readValue(authoritiesClaims
                                            .toString()
                                            .getBytes(),
                            SimpleGrantedAuthority[].class));


            //iniciar sesion
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities );

            //nos autentificamos
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            chain.doFilter(request, response);



        }catch (JwtException e){
            //validando errores
            Map<String, String> body = new HashMap<>();
            body.put("error", e.getMessage());
            body.put("message", "El Token JWT es invalido!");

            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            //response.setStatus(401);// no esta autorizado
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(CONTENT_TYPE);




        }



    }



}
