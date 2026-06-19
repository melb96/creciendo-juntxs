package com.unlar.guarderia.Entitites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String turno;
    private int capacidad;
    @OneToMany(mappedBy = "sala")
    private List<Infante> infantes;
    @OneToOne
    @JoinColumn(name = "maestra_titular_id")
    private Maestra maestraTitular;
}