package com.example.entities;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message = "El campo <nombre> no puede estar vacío")
    @Size(min = 4, max = 25, message = "El nombre debe contener entre 4 y 25 caracteres")
    private String name;

    @NotEmpty(message = "El campo <apellidos> no puede estar vacío")
    @Size(min = 4, max = 25, message = "Los apellidos debe contener entre 4 y 25 caracteres")
    private String surnames;

    @NotEmpty(message = "El campo <correo> no puede estar vacío")
    @Size(min = 4, max = 25, message = "El correo debe contener entre 4 y 25 caracteres")
    private String email;

    @NotEmpty(message = "El campo <contraseña> no puede estar vacío")
    private String password;

    @NotEmpty(message = "El campo <ciudad> no puede estar vacía")
    private String city;

    private List<String> hobbie;

    private String phone;

    private String imageUser;

    // 1. RELACION USER-DEPARTMENT

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
  
    private Department department;

    // 2. RELACION USER-YARDS (MANYTOMANY)

    @JoinTable(
        name = "rel_yards_users",
        joinColumns = @JoinColumn(name = "user_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name="yard_id", nullable = false)
    )
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    
    
    List<Yard> yards;
    

    // 5. RELACION USER - POSTS

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, mappedBy = "user")
    @JsonIgnore
    private List<Post> posts;

    
}
