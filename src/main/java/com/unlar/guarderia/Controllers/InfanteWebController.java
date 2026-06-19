package com.unlar.guarderia.Controllers;

import com.unlar.guarderia.Entitites.*;
import com.unlar.guarderia.Services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/infantes")
@RequiredArgsConstructor
public class InfanteWebController {

    private final InfanteService infanteService;
    private final TutorService tutorService;
    private final AsistenciaService asistenciaService;
    private final MaestraService maestraService;
    private final SalaService salaService;

    @GetMapping
    public String listarInfantes(Model model) {
        model.addAttribute("lista", infanteService.obtenerTodos());
        return "infantes-lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("infante", new Infante());
        cargarListas(model);
        return "infantes-formulario";
    }

    @PostMapping("/guardar")
    public String guardarInfante(@ModelAttribute("infante") Infante infante,
            @RequestParam("tutorId") Long tutorId,
            @RequestParam("maestraId") Long maestraId,
            @RequestParam("salaId") Long salaId) {

        infante.setMaestra(maestraService.obtenerPorId(maestraId).orElse(null));
        infante.setSala(salaService.obtenerPorId(salaId).orElse(null));

        infanteService.registrarInfante(infante, tutorId);
        return "redirect:/infantes";
    }

    @GetMapping("/historial/{id}")
    public String verHistorialInfante(@PathVariable("id") Long id, Model model) {
        Infante infante = infanteService.obtenerPorId(id).orElseThrow();
        model.addAttribute("infante", infante);
        model.addAttribute("historial", asistenciaService.obtenerHistorialPorInfante(id));
        return "infantes-historial";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model model) {
        model.addAttribute("infante", infanteService.obtenerPorId(id).orElseThrow());
        cargarListas(model);
        return "infantes-formulario";
    }

    @PostMapping("/actualizar")
    public String actualizarInfante(@ModelAttribute("infante") Infante infante,
            @RequestParam("tutorId") Long tutorId,
            @RequestParam("maestraId") Long maestraId,
            @RequestParam("salaId") Long salaId) {

        infante.setTutor(tutorService.obtenerPorId(tutorId).orElse(null));
        infante.setMaestra(maestraService.obtenerPorId(maestraId).orElse(null));
        infante.setSala(salaService.obtenerPorId(salaId).orElse(null));

        infanteService.actualizarInfante(infante);
        return "redirect:/infantes";
    }

    @PostMapping("/cambiar-estado")
    public String cambiarEstadoRapido(@RequestParam("infanteId") Long infanteId,
            @RequestParam("nuevoEstado") String nuevoEstado) {
        Infante infante = infanteService.obtenerPorId(infanteId).orElseThrow();
        infante.setEstadoActual(nuevoEstado);
        infanteService.actualizarInfante(infante);

        LocalDate hoy = LocalDate.now();
        asistenciaService.listarPorFecha(hoy).stream()
                .filter(a -> a.getInfante().getId().equals(infanteId) && a.getHoraSalida() == null)
                .findFirst()
                .ifPresent(a -> {
                    a.setBitacoraActividades(nuevoEstado);
                    asistenciaService.actualizarAsistencia(a);
                });

        return "redirect:/infantes";
    }

    private void cargarListas(Model model) {
        model.addAttribute("tutores", tutorService.obtenerTodos());
        model.addAttribute("maestras", maestraService.obtenerTodas());
        model.addAttribute("salas", salaService.obtenerTodas());
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        infanteService.eliminarInfante(id);
        return "redirect:/infantes";
    }
}