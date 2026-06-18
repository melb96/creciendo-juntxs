package com.unlar.guarderia.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.unlar.guarderia.Entitites.Infante;
import com.unlar.guarderia.Entitites.Tutor;
import com.unlar.guarderia.Repositories.InfanteRepository;
import com.unlar.guarderia.Repositories.TutorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InfanteService {

    private final InfanteRepository infanteRepository;
    private final TutorRepository tutorRepository;

    public String registrarInfante(Infante infante, Long tutorId) {
        if (infanteRepository.findByDni(infante.getDni()).isPresent()) {
            return "Error: Ya existe un infante registrado con el DNI " + infante.getDni();
        }

        Optional<Tutor> tutorOpt = tutorRepository.findById(tutorId);
        if (tutorOpt.isEmpty()) {
            return "Error: El Tutor con ID " + tutorId + " no existe en el sistema.";
        }

        infante.setTutor(tutorOpt.get());
        infante.setEstadoActual("Sin Ingresar");

        infanteRepository.save(infante);
        return "Infante registrado con éxito y vinculado a su tutor.";
    }

    public List<Infante> obtenerTodos() {
        return infanteRepository.findAll();
    }

    public List<Infante> obtenerPorTutor(Long tutorId) {
        return infanteRepository.findByTutorId(tutorId);
    }

    public Optional<Infante> obtenerPorId(Long id) {
        return infanteRepository.findById(id);
    }

    public String actualizarInfante(Infante infante) {
        infanteRepository.save(infante);
        return "Infante actualizado con éxito";
    }

    public List<Infante> buscarPorTutorId(Long tutorId) {
        return infanteRepository.findByTutorId(tutorId);
    }
}