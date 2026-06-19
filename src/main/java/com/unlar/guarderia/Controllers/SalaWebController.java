package com.unlar.guarderia.Controllers;

import com.unlar.guarderia.Entitites.Sala;
import com.unlar.guarderia.Services.SalaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/salas")
@RequiredArgsConstructor
public class SalaWebController {

    private final SalaService salaService;

    private boolean tienePermiso(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_OPERADOR"));
    }

    @GetMapping
    public String listar(Authentication auth, Model model) {
        if (!tienePermiso(auth)) return "redirect:/home";
        model.addAttribute("lista", salaService.obtenerTodas());
        return "salas-lista";
    }

    @GetMapping("/nuevo")
    public String formulario(Authentication auth, Model model) {
        if (!tienePermiso(auth)) return "redirect:/home";
        model.addAttribute("sala", new Sala());
        return "salas-formulario";
    }

    @PostMapping("/guardar")
    public String guardar(Authentication auth, @ModelAttribute Sala sala) {
        if (!tienePermiso(auth)) return "redirect:/home";
        salaService.registrarSala(sala);
        return "redirect:/salas";
    }

    @PostMapping("/actualizar")
    public String actualizar(Authentication auth, @ModelAttribute Sala sala) {
        if (!tienePermiso(auth)) return "redirect:/home";
        salaService.registrarSala(sala);
        return "redirect:/salas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarSala(Authentication auth, @PathVariable Long id) {
        if (!tienePermiso(auth)) return "redirect:/home";
        salaService.eliminarSala(id);
        return "redirect:/salas";
    }

    @GetMapping("/editar/{id}")
    public String editarSala(Authentication auth, @PathVariable Long id, Model model) {
        if (!tienePermiso(auth)) return "redirect:/home";
        model.addAttribute("sala", salaService.obtenerSalaPorId(id));
        return "salas-formulario";
    }
}