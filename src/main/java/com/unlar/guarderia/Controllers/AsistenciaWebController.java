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

    // Pantalla principal de presentismo del día: http://localhost:8080/asistencias
    @GetMapping
    public String mostrarPanelAsistencia(Model model) {
        LocalDate hoy = LocalDate.now();
        
        // Pasamos la fecha de hoy formateada para mostrar en el encabezado
        model.addAttribute("fechaHoy", hoy);
        
        // Traemos todos los infantes para armar la lista del personal
        List<Infante> infantes = infanteService.obtenerTodos();
        model.addAttribute("infantes", infantes);
        
        // Traemos las asistencias que ya se tomaron hoy para saber quién está adentro y quién no
        List<Asistencia> asistenciasHoy = asistenciaService.listarPorFecha(hoy);
        model.addAttribute("asistenciasHoy", asistenciasHoy);
        
        return "asistencias"; // Busca templates/asistencias.html
    }

    // Procesar el Ingreso desde la web
    @PostMapping("/ingreso")
    public String procesarIngreso(@RequestParam("infanteId") Long infanteId) {
        asistenciaService.registrarIngreso(infanteId); // Reutiliza el método que ya teníamos
        return "redirect:/asistencias"; // Recarga el panel actualizado
    }

    // Procesar el Egreso desde la web
    @PostMapping("/egreso")
    public String procesarEgreso(@RequestParam("infanteId") Long infanteId, 
                                 @RequestParam(value = "bitacora", required = false) String bitacora) {
        asistenciaService.registrarEgreso(infanteId, bitacora); // Reutiliza tu método con bitácora
        return "redirect:/asistencias"; // Recarga el panel actualizado
    }
}