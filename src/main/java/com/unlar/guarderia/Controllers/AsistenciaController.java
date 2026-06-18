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
        return asistenciaService.registrarIngreso(request.getInfanteId());
    }

    @PostMapping("/egreso")
    public String egreso(@RequestBody AsistenciaRequest request) {
        return asistenciaService.registrarEgreso(request.getInfanteId(), request.getBitacoraActividades());
    }

    @GetMapping("/fecha")
    public List<Asistencia> listarPorFecha(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate f) {
        return asistenciaService.listarPorFecha(f);
    }

}
