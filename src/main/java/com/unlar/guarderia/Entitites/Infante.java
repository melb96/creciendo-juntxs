package com.unlar.guarderia.Entitites;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;
    @Column(nullable = false, length = 50)
    private String estadoActual = "En Sala";
    @Column(length = 200)
    private String alergias;
    @Column(length = 100)
    private String obraSocial;
    @Column(length = 250)
    private String medicacion;
    @Column(length = 255)
    private String contactosEmergencia;
    @Column(length = 255)
    private String autorizadosRetiro;
    @Column(columnDefinition = "TEXT")
    private String observaciones;
    @Column(name = "tiene_consentimiento_camara", nullable = false)
    private boolean consienteCamara;

    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    private Tutor tutor;
    @OneToMany(mappedBy = "infante", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Asistencia> asistencias;
    @ManyToOne
    @JoinColumn(name = "maestra_id")
    private Maestra maestra;
    @ManyToOne
    @JoinColumn(name = "sala_id")
    private Sala sala;
}