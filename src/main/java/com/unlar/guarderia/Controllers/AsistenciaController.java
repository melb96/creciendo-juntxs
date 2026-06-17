package com.unlar.guarderia.Controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unlar.guarderia.DTO.AsistenciaRequest;
import com.unlar.guarderia.Entitites.Asistencia;
import com.unlar.guarderia.Services.AsistenciaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/asistencias")
@RequiredArgsConstructor
public class AsistenciaController {
    
    private final AsistenciaService asistenciaService;

    @PostMapping("/ingreso")
    public String ingreso(@RequestBody AsistenciaRequest request) {
        // Sacamos el ID del JSON y se lo pasamos como número al servicio
        return asistenciaService.registrarIngreso(request.getInfanteId());
    }

    @PostMapping("/egreso")
    public String egreso(@RequestBody AsistenciaRequest request) {
        // Sacamos el ID y la bitácora del JSON y se los pasamos al servicio
        return asistenciaService.registrarEgreso(request.getInfanteId(), request.getBitacoraActividades());
    }

    // Listar: GET http://localhost:8080/api/asistencias/fecha?f=2026-06-13
    @GetMapping("/fecha")
    public List<Asistencia> listarPorFecha(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate f) {
        return asistenciaService.listarPorFecha(f);
    }

}
