package com.unlar.guarderia.Controllers;

import com.unlar.guarderia.Entitites.Usuario;
import com.unlar.guarderia.Services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios") // URL para las pantallas web
@RequiredArgsConstructor
public class UsuarioWebController {

    private final UsuarioService usuarioService;

    // 1. Muestra la tabla de usuarios registrados: http://localhost:8080/usuarios
    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("lista", usuarioService.obtenerTodos());
        return "usuarios-lista"; // Llama a templates/usuarios-lista.html
    }

    // 2. Muestra el formulario para crear un nuevo usuario: http://localhost:8080/usuarios/nuevo
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios-formulario"; // Llama a templates/usuarios-formulario.html
    }

    // 3. Procesa la carga del formulario web
    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute("usuario") Usuario usuario, Model model) {
        String resultado = usuarioService.registrarUsuario(usuario);
        
        if (resultado.startsWith("Error")) {
            model.addAttribute("error", resultado);
            return "usuarios-formulario"; // Si hay error (ej: email duplicado), vuelve al formulario
        }
        
        return "redirect:/usuarios"; // Si todo sale bien, vuelve a la lista
    }

    // 1. Muestra el formulario con los datos del usuario cargados
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model model) {
    Usuario usuario = usuarioService.obtenerPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + id));
            
    model.addAttribute("usuario", usuario);
    return "usuarios-formulario"; // Reutilizamos el mismo HTML de alta
}

    // 2. Procesa la actualización del usuario editado
    @PostMapping("/actualizar")
    public String actualizarUsuario(@ModelAttribute("usuario") Usuario usuario) {
    usuarioService.actualizarUsuario(usuario);
    return "redirect:/usuarios"; // Al terminar, redirige a la tabla
}

    // Endpoint para eliminar: http://localhost:8080/usuarios/eliminar/1
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable("id") Long id) {
    usuarioService.eliminarUsuario(id);
    return "redirect:/usuarios"; // Vuelve a la lista de usuarios
}

// A. Muestra el formulario de usuario interceptando el ID del tutor desde la URL
    @GetMapping("/nuevo-tutor")
    public String mostrarFormularioTutor(@RequestParam("tutorId") Long tutorId, Model model) {
        Usuario usuario = new Usuario();
        usuario.setRole("TUTOR"); // Forzamos por defecto que sea rol TUTOR
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("tutorIdId", tutorId); // Pasamos el ID para inyectarlo de forma oculta
        return "usuarios-formulario"; 
    }

    // B. Procesa el guardado mapeando el ID del legajo físico
    @PostMapping("/guardar-tutor")
    public String guardarUsuarioTutor(@ModelAttribute("usuario") Usuario usuario, 
                                      @RequestParam("tutorId") Long tutorId, 
                                      Model model) {
        
        // Llamamos al nuevo método de tu servicio que explicamos en el paso anterior
        String resultado = usuarioService.registrarUsuarioTutor(usuario, tutorId);
        
        if (resultado.startsWith("Error")) {
            model.addAttribute("error", resultado);
            model.addAttribute("tutorIdId", tutorId); // Mantenemos el ID si hay que rellenar por error
            return "usuarios-formulario";
        }
        
        // Al terminar, mandamos al Operador de vuelta a la lista de legajos de tutores
        return "redirect:/tutores"; 
    }

    // A. Muestra la pantalla de recuperación: http://localhost:8080/usuarios/recuperar
    @GetMapping("/recuperar")
    public String mostrarPantallaRecuperar() {
        return "usuarios-recuperar"; // Llama a templates/usuarios-recuperar.html
    }

    // B. Procesa el formulario de restablecimiento
    @PostMapping("/recuperar")
    public String procesarRestablecimiento(@RequestParam("email") String email,
                                           @RequestParam("dni") String dni,
                                           @RequestParam("nuevaPassword") String nuevaPassword,
                                           Model model) {
        
        String resultado = usuarioService.restablecerContraseniaPorDni(email, dni, nuevaPassword);
        
        if (resultado.startsWith("Error")) {
            model.addAttribute("error", resultado);
            return "usuarios-recuperar"; // Si falla (no coincide DNI/Email), vuelve con el error
        }
        
        // Si sale todo bien, lo mandamos al login con un cartel de éxito
        model.addAttribute("exito", resultado);
        return "login"; // O a la ruta que uses para el login (ej: "redirect:/login")
    }

}