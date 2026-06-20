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

    @Transactional
    public String registrarTutor(Tutor tutor) {
        // Validación: verificar si el DNI ya existe
        if (tutorRepository.existsByDni(tutor.getDni())) {
            return "Error: Ya existe un tutor registrado con el DNI " + tutor.getDni();
        }
        tutorRepository.save(tutor);
        return "Tutor registrado con éxito.";
    }

    @Transactional(readOnly = true)
    public List<Tutor> obtenerTodos() {
        return tutorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Tutor> buscarPorDni(String dni) {
        return tutorRepository.findByDni(dni);
    }

    @Transactional(readOnly = true)
    public Optional<Tutor> obtenerPorId(Long id) {
        return tutorRepository.findById(id);
    }

    @Transactional
    public String actualizarTutor(Tutor tutor) {
        // Opcional: Podrías añadir aquí validación para que no cambien el DNI a uno ya existente
        tutorRepository.save(tutor);
        return "Tutor actualizado con éxito";
    }

    @Transactional
    public void eliminarTutor(Long id) {
        Tutor tutor = tutorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Error: No se encontró el legajo de tutor con ID: " + id));
        
        if (tutor.getUsuario() != null) {
            Usuario usuario = tutor.getUsuario();
            usuario.setTutor(null);
            usuarioRepository.delete(usuario);
        }
        tutorRepository.delete(tutor);

        System.out.println(
                "Legajo de Tutor ID [" + id + "] y su usuario/infantes asociados fueron eliminados del sistema.");
    }
}