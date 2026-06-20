package com.unlar.guarderia.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unlar.guarderia.Entitites.Infante;
import com.unlar.guarderia.Entitites.Maestra;
import com.unlar.guarderia.Repositories.InfanteRepository;
import com.unlar.guarderia.Repositories.MaestraRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaestraService {

    private final MaestraRepository maestraRepository;
    private final InfanteRepository infanteRepository;

    @Transactional
    public String registrarMaestra(Maestra maestra) {

        if (maestraRepository.existsByDni(maestra.getDni())) {
            return "Error: Ya existe una maestra registrada con el DNI " + maestra.getDni();
        }

        if (maestraRepository.existsByLegajo(maestra.getLegajo())) {
            return "Error: Ya existe una maestra registrada con el legajo " + maestra.getLegajo();
        }

        maestraRepository.save(maestra);
        return "Maestra registrada con éxito.";
    }

    @Transactional(readOnly = true)
    public List<Maestra> obtenerTodas() {
        return maestraRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Maestra> obtenerPorId(Long id) {
        return maestraRepository.findById(id);
    }

    @Transactional
    public Maestra obtenerMaestraPorId(Long id) {
        return maestraRepository.findById(id).orElse(null);
    }

    public void eliminarMaestra(Long id) {
        List<Infante> infantes = infanteRepository.findByMaestraId(id);
        for (Infante infante : infantes) {
            infante.setMaestra(null);
            infanteRepository.save(infante);
        }
        maestraRepository.deleteById(id);
    }

    public void guardarMaestra(Maestra maestra) {
        maestraRepository.save(maestra);
    }
}