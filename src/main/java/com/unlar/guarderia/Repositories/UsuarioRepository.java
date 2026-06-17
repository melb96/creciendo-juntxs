package com.unlar.guarderia.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unlar.guarderia.Entitites.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<Usuario> findByEmailAndTutor_Dni(String email, String dni);
}
