package com.unlar.guarderia.Controllers;

import com.unlar.guarderia.Entitites.Infante;
import com.unlar.guarderia.Entitites.Tutor;
import com.unlar.guarderia.Entitites.Usuario;
import com.unlar.guarderia.Services.InfanteService;
import com.unlar.guarderia.Repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MonitoreoWebController {

    private final InfanteService infanteService;
    private final UsuarioRepository usuarioRepository;

    @Value("${camara.stream-url:}")
    private String camaraStreamUrl;

    @Value("${camara.nombre:Sala principal}")
    private String camaraNombre;

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

        String streamUrl = obtenerStreamUrlSeguro();
        String nombreCamara = camaraNombre != null && !camaraNombre.trim().isEmpty()
                ? camaraNombre.trim()
                : "Sala principal";

        model.addAttribute("camaraStreamUrl", streamUrl);
        model.addAttribute("camaraNombre", nombreCamara);
        model.addAttribute("streamConfigurado", !streamUrl.isEmpty());

        return "monitoreo";
    }

    private String obtenerStreamUrlSeguro() {
        String streamUrl = camaraStreamUrl != null ? camaraStreamUrl.trim() : "";

        if (streamUrl.isEmpty()) {
            return "";
        }

        try {
            URI uri = URI.create(streamUrl);
            String scheme = uri.getScheme();
            boolean esHttp = "http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme);

            if (!esHttp || uri.getUserInfo() != null) {
                return "";
            }

            return streamUrl;
        } catch (IllegalArgumentException ex) {
            return "";
        }
    }
}
