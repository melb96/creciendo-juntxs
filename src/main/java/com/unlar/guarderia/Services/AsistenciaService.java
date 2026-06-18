package com.unlar.guarderia.Services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.unlar.guarderia.Entitites.Asistencia;
import com.unlar.guarderia.Entitites.Infante;
import com.unlar.guarderia.Repositories.AsistenciaRepository;
import com.unlar.guarderia.Repositories.InfanteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    private final InfanteRepository infanteRepository;

    public String registrarIngreso(Long infanteId) {
        LocalDate hoy = LocalDate.now();

        Optional<Infante> infanteOpt = infanteRepository.findById(infanteId);
        if (infanteOpt.isEmpty()) {
            return "Error: El infante con ID " + infanteId + " no existe.";
        }

        Optional<Asistencia> asistenciaActiva = asistenciaRepository
                .findByInfanteIdAndFechaAndHoraSalidaIsNull(infanteId, hoy);

        if (asistenciaActiva.isPresent()) {
            return "Error: El infante ya registra un ingreso activo para el día de hoy.";
        }

        Infante infante = infanteOpt.get();

        infante.setEstadoActual("En Sala");
        infanteRepository.save(infante);

        Asistencia asistencia = new Asistencia();
        asistencia.setFecha(hoy);
        asistencia.setHoraEntrada(LocalTime.now());
        asistencia.setInfante(infante);
        asistencia.setBitacoraActividades("En Sala");

        asistenciaRepository.save(asistencia);
        return "Ingreso registrado con éxito para el infante: " + infante.getNombre();
    }

    public String registrarEgreso(Long infanteId, String bitacora) {
        LocalDate hoy = LocalDate.now();

        Optional<Asistencia> asistenciaOpt = asistenciaRepository
                .findByInfanteIdAndFechaAndHoraSalidaIsNull(infanteId, hoy);

        if (asistenciaOpt.isEmpty()) {
            return "Error: No se encontró un ingreso activo para este infante en el día de hoy.";
        }

        Optional<Infante> infanteOpt = infanteRepository.findById(infanteId);
        if (infanteOpt.isPresent()) {
            Infante infante = infanteOpt.get();
            infante.setEstadoActual("Sin Ingresar");
            infanteRepository.saveAndFlush(infante);
        }

        Asistencia asistencia = asistenciaOpt.get();
        asistencia.setHoraSalida(LocalTime.now());

        if (bitacora != null && !bitacora.trim().isEmpty()) {
            asistencia.setBitacoraActividades(bitacora);
        } else {
            asistencia.setBitacoraActividades("Retirado");
        }

        asistenciaRepository.saveAndFlush(asistencia);
        return "Egreso registrado con éxito. ¡El infante ya fue retirado!";
    }

    public void actualizarAsistencia(Asistencia asistencia) {
        asistenciaRepository.save(asistencia);
    }

    public List<Asistencia> listarPorFecha(LocalDate fecha) {
        return asistenciaRepository.findByFecha(fecha);
    }

    public List<Asistencia> obtenerHistorialPorInfante(Long infanteId) {
        return asistenciaRepository.findByInfanteIdOrderByFechaDescHoraEntradaDesc(infanteId);
    }
}