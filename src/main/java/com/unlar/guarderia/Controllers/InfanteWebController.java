package com.unlar.guarderia.Controllers;

import com.unlar.guarderia.Entitites.Infante;
import com.unlar.guarderia.Entitites.Tutor;
import com.unlar.guarderia.Entitites.Asistencia;
import com.unlar.guarderia.Services.AsistenciaService;
import com.unlar.guarderia.Services.InfanteService;
import com.unlar.guarderia.Services.TutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/infantes")
@RequiredArgsConstructor
public class InfanteWebController {

    private final InfanteService infanteService;
    private final TutorService tutorService;
    private final AsistenciaService asistenciaService;

    @GetMapping
    public String listarInfantes(Model model) {
        model.addAttribute("lista", infanteService.obtenerTodos());
        return "infantes-lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("infante", new Infante());
        model.addAttribute("tutores", tutorService.obtenerTodos());
        return "infantes-formulario";
    }

    @PostMapping("/guardar")
    public String guardarInfante(@ModelAttribute("infante") Infante infante,
            @RequestParam("tutorId") Long tutorId,
            Model model) {

        String resultado = infanteService.registrarInfante(infante, tutorId);

        if (resultado.startsWith("Error")) {
            model.addAttribute("error", resultado);
            model.addAttribute("tutores", tutorService.obtenerTodos());
            return "infantes-formulario";
        }

        return "redirect:/infantes";
    }

    @GetMapping("/historial/{id}")
    public String verHistorialInfante(@PathVariable("id") Long id, Model model) {
        Infante infante = infanteService.obtenerTodos().stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Infante no encontrado"));

        model.addAttribute("infante", infante);
        model.addAttribute("historial", asistenciaService.obtenerHistorialPorInfante(id));
        return "infantes-historial";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model model) {
        Infante infante = infanteService.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Infante no encontrado: " + id));

        model.addAttribute("infante", infante);
        model.addAttribute("tutores", tutorService.obtenerTodos());
        return "infantes-formulario";
    }

    @PostMapping("/actualizar")
    public String actualizarInfante(@ModelAttribute("infante") Infante infante, @RequestParam("tutorId") Long tutorId) {
        Tutor tutor = tutorService.obtenerPorId(tutorId).orElse(null);
        infante.setTutor(tutor);

        infanteService.actualizarInfante(infante);
        return "redirect:/infantes";
    }

    @PostMapping("/cambiar-estado")
    public String cambiarEstadoRapido(@RequestParam("infanteId") Long infanteId,
            @RequestParam("nuevoEstado") String nuevoEstado) {
        Infante infante = infanteService.obtenerPorId(infanteId)
                .orElseThrow(() -> new IllegalArgumentException("Infante no encontrado: " + infanteId));

        infante.setEstadoActual(nuevoEstado);
        infanteService.actualizarInfante(infante);

        LocalDate hoy = LocalDate.now();
        Optional<Asistencia> asistenciaActiva = asistenciaService.listarPorFecha(hoy).stream()
                .filter(a -> a.getInfante().getId().equals(infanteId) && a.getHoraSalida() == null)
                .findFirst();

        if (asistenciaActiva.isPresent()) {
            Asistencia asistencia = asistenciaActiva.get();
            asistencia.setBitacoraActividades(nuevoEstado);
            asistenciaService.actualizarAsistencia(asistencia);
        }

        return "redirect:/infantes";
    }
}