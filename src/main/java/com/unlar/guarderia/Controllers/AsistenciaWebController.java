package com.unlar.guarderia.Controllers;

import com.unlar.guarderia.Entitites.Asistencia;
import com.unlar.guarderia.Entitites.Infante;
import com.unlar.guarderia.Services.AsistenciaService;
import com.unlar.guarderia.Services.InfanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/asistencias")
@RequiredArgsConstructor
public class AsistenciaWebController {

    private final AsistenciaService asistenciaService;
    private final InfanteService infanteService;

    @GetMapping
    public String mostrarPanelAsistencia(Model model) {
        LocalDate hoy = LocalDate.now();
        model.addAttribute("fechaHoy", hoy);
        List<Infante> infantes = infanteService.obtenerTodos();
        model.addAttribute("infantes", infantes);
        List<Asistencia> asistenciasHoy = asistenciaService.listarPorFecha(hoy);
        model.addAttribute("asistenciasHoy", asistenciasHoy);
        return "asistencias";
    }

    @PostMapping("/ingreso")
    public String procesarIngreso(@RequestParam("infanteId") Long infanteId) {
        asistenciaService.registrarIngreso(infanteId);
        return "redirect:/asistencias";
    }

    @PostMapping("/egreso")
    public String procesarEgreso(@RequestParam("infanteId") Long infanteId,
            @RequestParam(value = "bitacora", required = false) String bitacora) {
        asistenciaService.registrarEgreso(infanteId, bitacora);
        return "redirect:/asistencias";
    }
}