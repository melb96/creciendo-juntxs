package com.unlar.guarderia.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unlar.guarderia.Entitites.Infante;

public interface InfanteRepository extends JpaRepository<Infante, Long> {
Optional<Infante> findByDni(String dni);
List<Infante> findByTutorId(Long tutorId);
}
