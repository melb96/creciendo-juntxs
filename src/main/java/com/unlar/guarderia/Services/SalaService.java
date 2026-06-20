package com.unlar.guarderia.Services;

import com.unlar.guarderia.Entitites.Infante;
import com.unlar.guarderia.Entitites.Sala;
import com.unlar.guarderia.Repositories.InfanteRepository;
import com.unlar.guarderia.Repositories.SalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalaService {

    private final SalaRepository salaRepository;
    private final InfanteRepository infanteRepository;

    @Transactional
    public String registrarSala(Sala sala) {
        // Validación: verificar si el nombre ya existe
        if (salaRepository.existsByNombre(sala.getNombre())) {
            return "Error: Ya existe una sala con el nombre '" + sala.getNombre() + "'.";
        }
        salaRepository.save(sala);
        return "Sala registrada con éxito.";
    }

    // Nuevo método para obtener cupos (ej: 2/5)
    @Transactional(readOnly = true)
    public String obtenerCupoTexto(Long id) {
        Sala sala = salaRepository.findById(id).orElse(null);
        if (sala == null) return "N/A";
        long ocupados = infanteRepository.findBySalaId(id).size();
        return ocupados + "/" + sala.getCapacidad();
    }

    @Transactional(readOnly = true)
    public List<Sala> obtenerTodas() {
        return salaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Sala> obtenerPorId(Long id) {
        return salaRepository.findById(id);
    }

    @Transactional
    public Sala obtenerSalaPorId(Long id) {
        return salaRepository.findById(id).orElse(null);
    }

    @Transactional
    public void eliminarSala(Long id) {
        List<Infante> infantes = infanteRepository.findBySalaId(id);
        for (Infante infante : infantes) {
            infante.setSala(null);
            infanteRepository.save(infante);
        }
        salaRepository.deleteById(id);
    }
}