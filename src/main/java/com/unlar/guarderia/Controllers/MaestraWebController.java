package com.unlar.guarderia.Controllers;

import com.unlar.guarderia.Entitites.Maestra;
import com.unlar.guarderia.Services.MaestraService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/maestras")
@RequiredArgsConstructor
public class MaestraWebController {

    private final MaestraService maestraService;

    private boolean tienePermiso(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_OPERADOR"));
    }

    @GetMapping
    public String listar(Authentication auth, Model model) {
        if (!tienePermiso(auth))
            return "redirect:/home";
        model.addAttribute("lista", maestraService.obtenerTodas());
        return "maestras-lista";
    }

    @GetMapping("/nuevo")
    public String formulario(Authentication auth, Model model) {
        if (!tienePermiso(auth))
            return "redirect:/home";
        model.addAttribute("maestra", new Maestra());
        return "maestras-formulario";
    }

    @PostMapping("/guardar")
    public String guardar(Authentication auth, @ModelAttribute Maestra maestra, RedirectAttributes redirectAttributes) {
        if (!tienePermiso(auth))
            return "redirect:/home";

        String resultado = maestraService.registrarMaestra(maestra);

        if (resultado.startsWith("Error")) {
            redirectAttributes.addFlashAttribute("error", resultado);
            return "redirect:/maestras/nuevo";
        }

        return "redirect:/maestras";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(Authentication auth, @PathVariable Long id) {
        if (!tienePermiso(auth))
            return "redirect:/home";
        maestraService.eliminarMaestra(id);
        return "redirect:/maestras";
    }

    @GetMapping("/maestras/eliminar/{id}")
    public String eliminarMaestra(@PathVariable Long id) {
        maestraService.eliminarMaestra(id);
        return "redirect:/maestras";
    }

    @GetMapping("/editar/{id}")
    public String editarMaestra(@PathVariable Long id, Model model) {
        model.addAttribute("maestra", maestraService.obtenerMaestraPorId(id));
        return "maestras-formulario";
    }

    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute Maestra maestra) {
        maestraService.guardarMaestra(maestra);
        return "redirect:/maestras";
    }
}