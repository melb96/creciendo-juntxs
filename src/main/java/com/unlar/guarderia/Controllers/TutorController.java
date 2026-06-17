package com.unlar.guarderia.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unlar.guarderia.Entitites.Tutor;
import com.unlar.guarderia.Services.TutorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tutores")
@RequiredArgsConstructor
public class TutorController {

    private final TutorService tutorService;

    // Endpoint para registrar: POST http://localhost:8080/api/tutores/registrar
    @PostMapping("/registrar")
    public String registrar(@RequestBody Tutor tutor) {
        return tutorService.registrarTutor(tutor);
    }

    // Endpoint para listar todos: GET http://localhost:8080/api/tutores
    @GetMapping
    public List<Tutor> listarTodos() {
        return tutorService.obtenerTodos();
    }

    // Endpoint para buscar por DNI: GET http://localhost:8080/api/tutores/buscar/{dni}
    @GetMapping("/buscar/{dni}")
    public Optional<Tutor> buscarPorDni(@PathVariable String dni) {
        return tutorService.buscarPorDni(dni);
    }

}
