package com.unlar.guarderia.Entitites;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "tutores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Tutor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 60)
    private String nombre;
    @Column(nullable = false, length = 60)
    private String apellido;
    @Column(nullable = false, unique = true, length = 15)
    private String dni;
    @Column(nullable = false, length = 20)
    private String telefono;
    @Column(nullable = false, length = 100)
    private String relacionUniversitaria;
    // Adentro de Tutor.java
    @OneToMany(mappedBy = "tutor", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Infante> infantes;
    // Dentro de Tutor.java
    // Reemplaza el bloque de la relación @OneToOne
    @OneToOne(mappedBy = "tutor") 
    private Usuario usuario;
}