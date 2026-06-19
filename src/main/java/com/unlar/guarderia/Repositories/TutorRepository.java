package com.unlar.guarderia.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unlar.guarderia.Entitites.Tutor;

public interface TutorRepository extends JpaRepository<Tutor, Long> {
Optional<Tutor> findByDni(String dni);
boolean existsByDni(String dni);
Tutor findByUsuario(String usuario);
Tutor findByUsuario_Email(String email); 
}
