package com.unlar.guarderia.Controllers;

import com.unlar.guarderia.Entitites.Infante;
import com.unlar.guarderia.Entitites.Tutor;
import com.unlar.guarderia.Entitites.Usuario;
import com.unlar.guarderia.Services.InfanteService;
import com.unlar.guarderia.Repositories.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MonitoreoWebController {

    private final InfanteService infanteService; 
    private final UsuarioRepository usuarioRepository; 

    @GetMapping("/monitoreo")
    public String mostrarMonitoreo(HttpSession session, Model model) {
        String rol = (String) session.getAttribute("usuarioRol");
        String email = (String) session.getAttribute("usuarioEmail"); 
        
        if (rol == null) {
            return "redirect:/login";
        }

        // LÓGICA CORREGIDA: Buscamos y enviamos la lista completa de hijos del Tutor
        if ("TUTOR".equals(rol)) {
            Optional<Usuario> userOpt = usuarioRepository.findByEmail(email);
            
            if (userOpt.isPresent() && userOpt.get().getTutor() != null) {
                Tutor legajoTutor = userOpt.get().getTutor();
                
                // Traemos todos los hermanos vinculados a este legajo
                List<Infante> susHijos = infanteService.buscarPorTutorId(legajoTutor.getId());
                
                // Agregamos la lista entera al modelo para que la itere Thymeleaf
                model.addAttribute("infantes", susHijos);
                
                System.out.println("Tutor autenticado: [" + email + "] accediendo al monitoreo general de sus " + susHijos.size() + " hijo/s.");
            }
        }
        
        model.addAttribute("usuarioRol", rol);
        return "monitoreo"; 
    }
}