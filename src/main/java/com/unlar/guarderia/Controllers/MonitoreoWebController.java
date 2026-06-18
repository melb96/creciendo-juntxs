package com.unlar.guarderia.Controllers;

import com.unlar.guarderia.Entitites.Infante;
import com.unlar.guarderia.Entitites.Tutor;
import com.unlar.guarderia.Entitites.Usuario;
import com.unlar.guarderia.Services.InfanteService;
import com.unlar.guarderia.Repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MonitoreoWebController {

    private final InfanteService infanteService;
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/monitoreo")
    public String mostrarMonitoreo(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();

        Optional<Usuario> userOpt = usuarioRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            Usuario usuario = userOpt.get();
            String rol = usuario.getRole();

            if ("TUTOR".equals(rol) && usuario.getTutor() != null) {
                Tutor legajoTutor = usuario.getTutor();
                List<Infante> susHijos = infanteService.buscarPorTutorId(legajoTutor.getId());
                model.addAttribute("infantes", susHijos);

                System.out.println("Tutor autenticado: [" + email + "] accediendo al monitoreo. Cantidad de hijos: "
                        + susHijos.size());
            }

            model.addAttribute("usuarioRol", rol);
        }

        return "monitoreo";
    }
}