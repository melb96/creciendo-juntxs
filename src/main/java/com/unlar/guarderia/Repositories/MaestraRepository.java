package com.unlar.guarderia.Repositories;

import com.unlar.guarderia.Entitites.Maestra;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MaestraRepository extends JpaRepository<Maestra, Long> {
    boolean existsByDni(String dni);
    boolean existsByLegajo(String legajo);
    Optional<Maestra> findByDni(String dni);
    Optional<Maestra> findByLegajo(String legajo);
}