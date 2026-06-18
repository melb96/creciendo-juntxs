package com.unlar.guarderia.Controllers;

import com.unlar.guarderia.Entitites.Tutor;
import com.unlar.guarderia.Services.TutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tutores")
@RequiredArgsConstructor
public class TutorWebController {

    private final TutorService tutorService;

    @GetMapping
    public String listarTutores(Model model) {
        model.addAttribute("lista", tutorService.obtenerTodos());
        return "tutores-lista"; // Busca templates/tutores-lista.html
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("tutor", new Tutor());
        return "tutores-formulario";
    }

    @PostMapping("/guardar")
    public String guardarTutor(@ModelAttribute("tutor") Tutor tutor, Model model) {
        String resultado = tutorService.registrarTutor(tutor);

        if (resultado.startsWith("Error")) {
            model.addAttribute("error", resultado);
            return "tutores-formulario";
        }
        return "redirect:/tutores";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model model) {
        Tutor tutor = tutorService.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Tutor no encontrado: " + id));
        model.addAttribute("tutor", tutor);
        return "tutores-formulario";
    }

    @PostMapping("/actualizar")
    public String actualizarTutor(@ModelAttribute("tutor") Tutor tutor) {
        tutorService.actualizarTutor(tutor);
        return "redirect:/tutores";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarTutor(@PathVariable("id") Long id) {
        tutorService.eliminarTutor(id);
        return "redirect:/tutores";
    }
}
