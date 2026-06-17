package com.unlar.guarderia.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unlar.guarderia.Entitites.Tutor;
import com.unlar.guarderia.Entitites.Usuario;
import com.unlar.guarderia.Repositories.TutorRepository;
import com.unlar.guarderia.Repositories.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TutorService {

    private final TutorRepository tutorRepository;
    private final UsuarioRepository usuarioRepository;

    public String registrarTutor(Tutor tutor) {
        if (tutorRepository.existsByDni(tutor.getDni())) {
            return "Error: Ya existe un tutor registrado con el DNI " + tutor.getDni();
        }
        tutorRepository.save(tutor);
        return "Tutor registrado con éxito.";
    }

    public List<Tutor> obtenerTodos() {
        return tutorRepository.findAll();
    }

    public Optional<Tutor> buscarPorDni(String dni) {
        return tutorRepository.findByDni(dni);
    }

    public Optional<Tutor> obtenerPorId(Long id) {
        return tutorRepository.findById(id);
    }

    public String actualizarTutor(Tutor tutor) {
        tutorRepository.save(tutor);
        return "Tutor actualizado con éxito";
    }

    @Transactional
    public void eliminarTutor(Long id) {
        Tutor tutor = tutorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Error: No se encontró el legajo de tutor con ID: " + id));
        
        // 1. Eliminar primero el usuario asociado si existe
        if (tutor.getUsuario() != null) {
            Usuario usuario = tutor.getUsuario();
            // Desvinculamos el tutor antes de borrar para evitar inconsistencias
            usuario.setTutor(null);
            usuarioRepository.delete(usuario);
        }
        
        // 2. Ahora que el usuario no existe, borramos el tutor
        tutorRepository.delete(tutor);
        
        System.out.println("Legajo de Tutor ID [" + id + "] y su usuario/infantes asociados fueron eliminados del sistema.");
    }
}