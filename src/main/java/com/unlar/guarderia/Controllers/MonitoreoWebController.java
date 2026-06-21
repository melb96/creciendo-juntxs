package com.unlar.guarderia.Controllers;

import com.unlar.guarderia.Entitites.Infante;
import com.unlar.guarderia.Entitites.Tutor;
import com.unlar.guarderia.Entitites.Usuario;
import com.unlar.guarderia.Services.InfanteService;
import com.unlar.guarderia.Repositories.InfanteRepository;
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
    private final InfanteRepository infanteRepository;

    @Value("${camara.stream-url:}")
    private String camaraStreamUrl;

    @Value("${camara.nombre:Sala principal}")
    private String camaraNombre;

    @Value("${camara.sala-id:}")
    private String camaraSalaId;

    @GetMapping("/monitoreo")
    public String mostrarMonitoreo(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();
        Optional<Usuario> userOpt = usuarioRepository.findByEmail(email);
        String streamUrlSeguro = obtenerStreamUrlSeguro();
        boolean streamConfigurado = !streamUrlSeguro.isEmpty();
        boolean puedeVerCamara = false;
        boolean esTutor = false;
        Optional<Long> salaMonitoreadaId = obtenerSalaMonitoreadaId();

        if (userOpt.isPresent()) {
            Usuario usuario = userOpt.get();
            String rol = usuario.getRole();
            esTutor = "TUTOR".equals(rol);

            if ("ADMIN".equals(rol) || "OPERADOR".equals(rol)) {
                puedeVerCamara = streamConfigurado;
            } else if (esTutor && usuario.getTutor() != null) {
                Tutor legajoTutor = usuario.getTutor();
                List<Infante> susHijos = infanteService.buscarPorTutorId(legajoTutor.getId());
                model.addAttribute("infantes", susHijos);
                puedeVerCamara = streamConfigurado
                        && salaMonitoreadaId.isPresent()
                        && tutorPuedeVerSalaMonitoreada(legajoTutor, salaMonitoreadaId.get());

                System.out.println("Tutor autenticado: [" + email + "] accediendo al monitoreo. Cantidad de hijos: "
                        + susHijos.size());
            }

            model.addAttribute("usuarioRol", rol);
        }

        String nombreCamara = camaraNombre != null && !camaraNombre.trim().isEmpty()
                ? camaraNombre.trim()
                : "Sala principal";

        model.addAttribute("camaraStreamUrl", puedeVerCamara ? streamUrlSeguro : "");
        model.addAttribute("camaraNombre", nombreCamara);
        model.addAttribute("streamConfigurado", streamConfigurado);
        model.addAttribute("puedeVerCamara", puedeVerCamara);
        model.addAttribute("esTutor", esTutor);

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

    private Optional<Long> obtenerSalaMonitoreadaId() {
        String salaId = camaraSalaId != null ? camaraSalaId.trim() : "";

        if (salaId.isEmpty()) {
            return Optional.empty();
        }

        try {
            return Optional.of(Long.parseLong(salaId));
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }

    private boolean tutorPuedeVerSalaMonitoreada(Tutor tutor, Long salaId) {
        List<Infante> presentesEnSala = infanteRepository.findBySalaId(salaId).stream()
                .filter(this::estaPresenteAdentro)
                .toList();

        boolean tutorTieneHijoPresenteConConsentimiento = presentesEnSala.stream()
                .anyMatch(infante -> infante.isConsienteCamara()
                        && infante.getTutor() != null
                        && tutor.getId().equals(infante.getTutor().getId()));

        boolean todosLosPresentesConsienten = presentesEnSala.stream()
                .allMatch(Infante::isConsienteCamara);

        return tutorTieneHijoPresenteConConsentimiento && todosLosPresentesConsienten;
    }

    private boolean estaPresenteAdentro(Infante infante) {
        String estado = infante.getEstadoActual();
        if (estado == null || estado.trim().isEmpty()) {
            return false;
        }

        estado = estado.trim();
        return !"Sin Ingresar".equals(estado)
                && !"Retirado".equals(estado)
                && !"Ausente".equals(estado);
    }
}
