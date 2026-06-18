package com.unlar.guarderia.Controllers;

import com.unlar.guarderia.Entitites.Usuario;
import com.unlar.guarderia.Services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioWebController {

    private final UsuarioService usuarioService;

    // Método auxiliar para verificar si el usuario es ADMIN
    private boolean esAdmin(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @GetMapping
    public String listarUsuarios(Authentication auth, Model model) {
        if (!esAdmin(auth))
            return "redirect:/home";
        model.addAttribute("lista", usuarioService.obtenerTodos());
        return "usuarios-lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Authentication auth, Model model) {
        if (!esAdmin(auth))
            return "redirect:/home";
        model.addAttribute("usuario", new Usuario());
        return "usuarios-formulario";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(Authentication auth, @ModelAttribute("usuario") Usuario usuario, Model model) {
        if (!esAdmin(auth))
            return "redirect:/home";
        String resultado = usuarioService.registrarUsuario(usuario);
        if (resultado.startsWith("Error")) {
            model.addAttribute("error", resultado);
            return "usuarios-formulario";
        }
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(Authentication auth, @PathVariable("id") Long id, Model model) {
        if (!esAdmin(auth))
            return "redirect:/home";
        Usuario usuario = usuarioService.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + id));
        model.addAttribute("usuario", usuario);
        return "usuarios-formulario";
    }

    @PostMapping("/actualizar")
    public String actualizarUsuario(Authentication auth, @ModelAttribute("usuario") Usuario usuario) {
        if (!esAdmin(auth))
            return "redirect:/home";
        usuarioService.actualizarUsuario(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(Authentication auth, @PathVariable("id") Long id) {
        if (!esAdmin(auth))
            return "redirect:/home";
        usuarioService.eliminarUsuario(id);
        return "redirect:/usuarios";
    }

    // A. Muestra el formulario de usuario interceptando el ID del tutor
    @GetMapping("/nuevo-tutor")
    public String mostrarFormularioTutor(Authentication auth, @RequestParam("tutorId") Long tutorId, Model model) {
        if (!esAdmin(auth))
            return "redirect:/home";
        Usuario usuario = new Usuario();
        usuario.setRole("TUTOR");
        model.addAttribute("usuario", usuario);
        model.addAttribute("tutorIdId", tutorId);
        return "usuarios-formulario";
    }

    // B. Procesa el guardado mapeando el ID del legajo físico
    @PostMapping("/guardar-tutor")
    public String guardarUsuarioTutor(Authentication auth, @ModelAttribute("usuario") Usuario usuario,
            @RequestParam("tutorId") Long tutorId, Model model) {
        if (!esAdmin(auth))
            return "redirect:/home";
        String resultado = usuarioService.registrarUsuarioTutor(usuario, tutorId);
        if (resultado.startsWith("Error")) {
            model.addAttribute("error", resultado);
            model.addAttribute("tutorIdId", tutorId);
            return "usuarios-formulario";
        }
        return "redirect:/tutores";
    }

    // A. Muestra la pantalla de recuperación (Pública)
    @GetMapping("/recuperar")
    public String mostrarPantallaRecuperar() {
        return "usuarios-recuperar";
    }

    // B. Procesa el formulario de restablecimiento (Pública - Sin Authentication)
    @PostMapping("/recuperar")
    public String procesarRestablecimiento(@RequestParam("email") String email,
            @RequestParam("dni") String dni,
            @RequestParam("nuevaPassword") String nuevaPassword,
            Model model) {

        String resultado = usuarioService.restablecerContraseniaPorDni(email, dni, nuevaPassword);

        if (resultado.startsWith("Error")) {
            model.addAttribute("error", resultado);
            return "usuarios-recuperar";
        }

        model.addAttribute("exito", "Contraseña restablecida correctamente.");
        return "redirect:/login";
    }
}