package com.unlar.guarderia.Entitites;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "infantes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Infante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 60)
    private String nombre;
    @Column(nullable = false, length = 60)
    private String apellido;
    @Column(nullable = false, unique = true, length = 15)
    private String dni;
    @Column(nullable = false)
    private LocalDate fechaNacimiento;
    @Column(length = 255)
    private String observacionesMedicas;
    @Column(nullable = false, length = 50)
    private String estadoActual = "En Sala"; // Valores: "En Sala", "Comiendo", "Durmiendo", "Jugando"
    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    private Tutor tutor;
    // Adentro de Infante.java
@OneToMany(mappedBy = "infante", cascade = CascadeType.REMOVE, orphanRemoval = true)
private List<Asistencia> asistencias;
}
