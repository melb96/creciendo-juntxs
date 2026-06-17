package com.unlar.guarderia.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unlar.guarderia.Entitites.Infante;
import com.unlar.guarderia.Services.InfanteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/infantes")
@RequiredArgsConstructor
public class InfanteController {

    private final InfanteService infanteService;

    // Endpoint para registrar: POST http://localhost:8080/api/infantes/registrar?tutorId=1
    @PostMapping("/registrar")
    public String registrar(@RequestBody Infante infante, @RequestParam Long tutorId) {
        return infanteService.registrarInfante(infante, tutorId);
    }

    // Endpoint para listar todos: GET http://localhost:8080/api/infantes
    @GetMapping
    public List<Infante> listarTodos() {
        return infanteService.obtenerTodos();
    }

    // Endpoint para listar por Tutor: GET http://localhost:8080/api/infantes/tutor/1
    @GetMapping("/tutor/{tutorId}")
    public List<Infante> listarPorTutor(@PathVariable Long tutorId) {
        return infanteService.obtenerPorTutor(tutorId);
    }
}
