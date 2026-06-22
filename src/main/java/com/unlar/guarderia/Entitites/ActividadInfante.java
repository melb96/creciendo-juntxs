package com.unlar.guarderia.Entitites;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "actividades_infante")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ActividadInfante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "infante_id", nullable = false)
    private Infante infante;

    @ManyToOne
    @JoinColumn(name = "asistencia_id")
    private Asistencia asistencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoEventoActividad tipoEvento;

    @Column(length = 50)
    private String estadoAnterior;

    @Column(length = 50)
    private String estadoNuevo;

    @Column(length = 1000)
    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false)
    private boolean notificadoTutor;

    @Column(length = 50)
    private String canalNotificacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PrioridadActividad prioridad;
}
