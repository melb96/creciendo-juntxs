package com.unlar.guarderia.Services;

import java.util.Optional;
import java.util.List;

import org.springframework.stereotype.Service;

import com.unlar.guarderia.Entitites.Usuario;
import com.unlar.guarderia.Entitites.Tutor;
import com.unlar.guarderia.Repositories.UsuarioRepository;

import jakarta.transaction.Transactional;

import com.unlar.guarderia.Repositories.TutorRepository; // IMPORTANTE: Inyectamos el repo de tutores

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TutorRepository tutorRepository; // Agregado para poder buscar el legajo físico

    // 1. Registro genérico (ADMIN y OPERADOR desde el panel de control)
    public String registrarUsuario(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            return "Error: El correo electrónico ya está registrado.";
        }
        usuarioRepository.save(usuario);
        return "Usuario registrado con éxito.";
    }

    // =========================================================================
    // NUEVO MÉTODO INTEGRADO: Registra y vincula la cuenta directamente al Tutor
    // =========================================================================
    public String registrarUsuarioTutor(Usuario usuario, Long tutorId) {
        // Validamos si el mail de login elegido ya existe
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            return "Error: El correo electrónico ya está registrado.";
        }

        // Buscamos el legajo del tutor en la base de datos
        Tutor tutorLegajo = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new IllegalArgumentException("Error: El legajo de tutor con ID " + tutorId + " no existe."));

        // Forzamos el rol seguro y unimos las dos puntas de la relación
        usuario.setRole("TUTOR");
        usuario.setTutor(tutorLegajo);

        // Al guardar, JPA extrae automáticamente el ID del tutor y lo inserta en la columna tutor_id
        usuarioRepository.save(usuario);
        return "Usuario Tutor registrado y vinculado con éxito.";
    }

    // 2. Control de Login tradicional
    public String iniciarSesion(String email, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (usuario.getPassword().equals(password)) {
                return "Login exitoso. Rol: " + usuario.getRole();
            } else {
                return "Contraseña incorrecta.";
            }
        }
        return "El usuario no existe.";
    }

    // Muestra a todos los usuarios en la tabla general
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    // Busca un usuario específico por su ID para editarlo
    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    // Guarda las modificaciones del usuario existente
    public String actualizarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
        return "Usuario actualizado con éxito.";
    }

    // Reemplaza tu método eliminarUsuario completo:
    @Transactional // IMPORTANTE: Agrega esta anotación arriba del método
    public void eliminarUsuario(Long id) {
    Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    
    // Desvinculamos el tutor para que la eliminación no intente sincronizarlo
    if (usuario.getTutor() != null) {
        usuario.setTutor(null);
        usuarioRepository.save(usuario); // Guardamos la desvinculación
    }
    
    usuarioRepository.deleteById(id);
}

    public String restablecerContraseniaPorDni(String email, String dni, String nuevaPassword) {
        // Buscamos el usuario cruzando el email con el DNI del tutor asociado
        java.util.Optional<Usuario> usuarioOpt = usuarioRepository.findByEmailAndTutor_Dni(email, dni);
        
        if (usuarioOpt.isEmpty()) {
            return "Error: Los datos ingresados no coinciden con ningún Tutor registrado.";
        }
        
        Usuario usuario = usuarioOpt.get();
        usuario.setPassword(nuevaPassword); // Seteamos la nueva clave
        usuarioRepository.save(usuario);    // Guardamos en la base de datos
        
        return "Éxito: Tu contraseña ha sido restablecida correctamente.";
    }
}