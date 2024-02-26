package com.curso.springboot.jpa.entities;


import com.curso.springboot.jpa.validation.ExistsByUsername;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ExistsByUsername
    @NotBlank
    @Column(unique = true)
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

    /*
    es una bandera no esta mapeado a la tabla no es un atributo de la clase le ponemos @Transient
    */
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean admin;

    /*
        para deshabilitar al usuario cuando es false lo ponemos en un @PrePersist
        1 = true habilitado Administrador
        0 = false deshabilitado

     */
    private boolean enabled;

     /*
     se pone el
     @PrePersist para mandar a la variable enable un true para que ponga un 1 en la bd
      */
     @PrePersist
     public void prePersist(){
         enabled = true;
     }

     /*
        @JsonIgnoreProperties
        para que no haya error ciclico ponemos en cada lado del  de la relacion
        cuando hay relaciones bidereccionales o cuando un error en el handler del LazyInitializer
        excluir la lista de usuarios del objeto Role
        en User y Role
        en @OneToMany, ManyToMany , ManyToOne son Lazy
     */

     @JsonIgnoreProperties({"users", "handler", "hibernateLazyInitializer"})
     @ManyToMany
     @JoinTable //mapea la tabla intermedia
            ( name = "users_roles",
              joinColumns = @JoinColumn(name = "user_id"),
              inverseJoinColumns = @JoinColumn (name="role_id"),
              uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "role_id"})}
            )
     private List<Role> roles;



     public User() {
        roles = new ArrayList<>();
     }


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



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }


}
