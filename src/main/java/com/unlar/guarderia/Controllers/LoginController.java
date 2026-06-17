package com.unlar.guarderia.Controllers;

import com.unlar.guarderia.Services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UsuarioService usuarioService;

    // 1. Muestra la pantalla de Login (Ya la debes tener apuntando a tu login.html)
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login"; 
    }

    // 2. Procesa el formulario de Login tradicional de la Web
    @PostMapping("/login/entrar")
    public String procesarLogin(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        String resultado = usuarioService.iniciarSesion(email, password);

        if (resultado.startsWith("Login exitoso")) {
            // "resultado" devuelve por ejemplo: "Login exitoso. Rol: ADMIN"
            // Extraemos el rol del texto de tu servicio de forma simple:
            String rol = resultado.substring(resultado.lastIndexOf(" ") + 1);
            
            // GUARDAMOS EL ROL EN LA SESIÓN HTTP
            session.setAttribute("usuarioRol", rol);
            session.setAttribute("usuarioEmail", email);
            
            return "redirect:/home"; // Redirige al Home de forma segura
        } else {
            // Si falla, vuelve al login mostrando el error
            model.addAttribute("error", resultado);
            return "login";
        }
    }

    // 3. Tu pantalla de Home modificada para leer la sesión
    @GetMapping("/home")
    public String mostrarHome(HttpSession session, Model model) {
        String rol = (String) session.getAttribute("usuarioRol");
        
        // Si por alguna razón quieren entrar al home sin loguearse, los mandamos al login
        if (rol == null) {
            return "redirect:/login";
        }
        
        // Le pasamos el rol al HTML de Thymeleaf
        model.addAttribute("usuarioRol", rol);
        model.addAttribute("usuarioEmail", session.getAttribute("usuarioEmail"));
        return "home";
    }

    // 4. Endpoint para cerrar sesión (Opcional pero super útil)
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate(); // Destruye la sesión actual
        return "redirect:/login";
    }
}