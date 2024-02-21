package com.curso.springboot.jpa.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank
    @Size(min= 4, max= 12)
    private String username;

    /*
       @JsonProperty para no mostrar el password en la consulta del json de deshabilitamos
       se da acceso cuando se escribe pero no cuando se lee el json
       cuando se deserializa cuando lo escribimos o lo transformamos a una clase
       pero no cuando tomamos una clase y lo pasamos al json

       @JsonIgnore otra alternativa es poner esta anotacion pero crear al usuario muestra el mesaje que no tiene passw

     */
    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    //es una bandera no esta mapeado a la tabla no es un atributo de la clase
    //ponemos @Transient
    @Transient
    private boolean admin;

    //para deshabilitar al usuario cuando es false lo ponemos en un @PrePersist
    //1 = true habilitado Administrador
    //0 = false deshabilitado
    private boolean enabled;

     /*
     se pone el
     @PrePersist para mandar a la variable enable un true para que ponga un 1 en la bd
      */
     @PrePersist
     public void prePersist(){
         enabled = true;
     }

    @ManyToMany
    @JoinTable //mapea la tabla intermedia
            ( name = "users_roles",
              joinColumns = @JoinColumn(name = "user_id"),
              inverseJoinColumns = @JoinColumn (name="role_id"),
              uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "role_id"})}
            )
    private List<Role> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }


    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
