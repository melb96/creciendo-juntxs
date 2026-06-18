package com.unlar.guarderia.Controllers;

import com.unlar.guarderia.Repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UsuarioRepository usuarioRepository;

    @GetMapping("/home")
    public String home(Authentication auth, Model model) {
        if (auth != null && auth.isAuthenticated()) {
            String email = auth.getName();
            model.addAttribute("usuarioEmail", email);
            usuarioRepository.findByEmail(email).ifPresent(u -> {
                // Pasamos el rol limpio (ADMIN, TUTOR, etc.)
                model.addAttribute("rolLimpio", u.getRole());
            });
        }
        return "home";
    }
}