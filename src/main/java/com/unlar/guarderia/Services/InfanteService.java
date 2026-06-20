package com.unlar.guarderia.Services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.unlar.guarderia.Entitites.Infante;
import com.unlar.guarderia.Entitites.Sala;
import com.unlar.guarderia.Entitites.Tutor;
import com.unlar.guarderia.Repositories.InfanteRepository;
import com.unlar.guarderia.Repositories.TutorRepository;

import jakarta.transaction.Transactional;

import com.unlar.guarderia.Repositories.SalaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InfanteService {

    private final InfanteRepository infanteRepository;
    private final TutorRepository tutorRepository;
    private final SalaRepository salaRepository;

    public String registrarInfante(Infante infante, Long tutorId, Long salaId) {
        if (infanteRepository.findByDni(infante.getDni()).isPresent()) {
            return "Error: Ya existe un infante registrado con el DNI " + infante.getDni();
        }

        long dias = ChronoUnit.DAYS.between(infante.getFechaNacimiento(), LocalDate.now());
        if (dias < 45 || dias > (5 * 365)) {
            return "Error: La edad del infante debe estar comprendida entre 45 días y 5 años.";
        }

        Optional<Sala> salaOpt = salaRepository.findById(salaId);
        if (salaOpt.isEmpty()) {
            return "Error: La sala seleccionada no existe.";
        }
        Sala sala = salaOpt.get();
        long ocupacion = infanteRepository.findBySalaId(salaId).size();
        if (ocupacion >= sala.getCapacidad()) {
            return "Error: La sala " + sala.getNombre() + " ya está completa (" + ocupacion + "/" + sala.getCapacidad()
                    + ").";
        }

        Optional<Tutor> tutorOpt = tutorRepository.findById(tutorId);
        if (tutorOpt.isEmpty()) {
            return "Error: El Tutor con ID " + tutorId + " no existe en el sistema.";
        }

        infante.setTutor(tutorOpt.get());
        infante.setSala(sala);
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

    @Transactional
    public String actualizarInfante(Infante infante) {
        Infante infanteExistente = infanteRepository.findById(infante.getId())
                .orElseThrow(() -> new RuntimeException("Infante no encontrado"));
        infante.setEstadoActual(infanteExistente.getEstadoActual());
        infanteRepository.save(infante);
        return "Infante actualizado con éxito";
    }

    public List<Infante> buscarPorTutorId(Long tutorId) {
        return infanteRepository.findByTutorId(tutorId);
    }

    public void eliminarInfante(Long id) {
        if (infanteRepository.existsById(id)) {
            infanteRepository.deleteById(id);
        }
    }
}