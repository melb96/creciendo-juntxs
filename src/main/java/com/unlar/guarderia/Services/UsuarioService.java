package com.unlar.guarderia.Services;

import java.util.Optional;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.unlar.guarderia.Entitites.Usuario;
import com.unlar.guarderia.Entitites.Tutor;
import com.unlar.guarderia.Repositories.UsuarioRepository;
import com.unlar.guarderia.Repositories.TutorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TutorRepository tutorRepository;
    private final PasswordEncoder passwordEncoder;

    public String registrarUsuario(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            return "Error: El correo electrónico ya está registrado.";
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);
        return "Usuario registrado con éxito.";
    }

    public String registrarUsuarioTutor(Usuario usuario, Long tutorId) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            return "Error: El correo electrónico ya está registrado.";
        }
        Tutor tutorLegajo = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new IllegalArgumentException("Error: ID no existe."));
        usuario.setRole("TUTOR");
        usuario.setTutor(tutorLegajo);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);
        return "Usuario Tutor registrado y vinculado con éxito.";
    }

    public List<Usuario> obtenerTodos() { return usuarioRepository.findAll(); }

    public Optional<Usuario> obtenerPorId(Long id) { return usuarioRepository.findById(id); }

    public String actualizarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
        return "Usuario actualizado con éxito.";
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        if (usuario.getTutor() != null) {
            usuario.setTutor(null);
            usuarioRepository.save(usuario);
        }
        usuarioRepository.deleteById(id);
    }

    public String restablecerContraseniaPorDni(String email, String dni, String nuevaPassword) {
        java.util.Optional<Usuario> usuarioOpt = usuarioRepository.findByEmailAndTutor_Dni(email, dni);
        if (usuarioOpt.isEmpty()) return "Error.";
        Usuario usuario = usuarioOpt.get();
        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);
        return "Éxito.";
    }
}