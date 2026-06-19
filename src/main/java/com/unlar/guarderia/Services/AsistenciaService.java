package com.unlar.guarderia.Services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unlar.guarderia.Entitites.Asistencia;
import com.unlar.guarderia.Entitites.Infante;
import com.unlar.guarderia.Entitites.PushSubscription;
import com.unlar.guarderia.Repositories.AsistenciaRepository;
import com.unlar.guarderia.Repositories.InfanteRepository;
import com.unlar.guarderia.Repositories.PushSubscriptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    private final InfanteRepository infanteRepository;
    private final PushServiceConfig pushService;
    private final PushSubscriptionRepository pushRepo;

    @Transactional
    public String registrarIngreso(Long infanteId) {
        LocalDate hoy = LocalDate.now();

        Optional<Infante> infanteOpt = infanteRepository.findById(infanteId);
        if (infanteOpt.isEmpty())
            return "Error: El infante con ID " + infanteId + " no existe.";

        if (asistenciaRepository.findByInfanteIdAndFechaAndHoraSalidaIsNull(infanteId, hoy).isPresent()) {
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

        enviarNotificacionATutor(infante, "Ingreso registrado: " + infante.getNombre() + " ya está en la sala.");

        return "Ingreso registrado con éxito para el infante: " + infante.getNombre();
    }

    @Transactional
    public String registrarEgreso(Long infanteId, String bitacora) {
        LocalDate hoy = LocalDate.now();

        Optional<Asistencia> asistenciaOpt = asistenciaRepository
                .findByInfanteIdAndFechaAndHoraSalidaIsNull(infanteId, hoy);

        if (asistenciaOpt.isEmpty())
            return "Error: No se encontró un ingreso activo para este infante en el día de hoy.";

        Optional<Infante> infanteOpt = infanteRepository.findById(infanteId);
        Infante infante = null;
        if (infanteOpt.isPresent()) {
            infante = infanteOpt.get();
            infante.setEstadoActual("Sin Ingresar");
            infanteRepository.saveAndFlush(infante);
        }

        Asistencia asistencia = asistenciaOpt.get();
        asistencia.setHoraSalida(LocalTime.now());
        asistencia.setBitacoraActividades((bitacora != null && !bitacora.isBlank()) ? bitacora : "Retirado");
        asistenciaRepository.saveAndFlush(asistencia);

        if (infante != null) {
            enviarNotificacionATutor(infante, "Egreso registrado: " + infante.getNombre() + " ha sido retirado.");
        }

        return "Egreso registrado con éxito. ¡El infante ya fue retirado!";
    }
    @Async
    public void enviarNotificacionATutor(Infante infante, String mensaje) {
        Infante infanteCompleto = infanteRepository.findById(infante.getId()).orElse(infante);

        if (infanteCompleto.getTutor() != null) {
            Long tutorId = infanteCompleto.getTutor().getId();
            System.out.println("DEBUG: Enviando notificación. Infante: " + infanteCompleto.getNombre() + " | Tutor ID: "
                    + tutorId);

            List<PushSubscription> subs = pushRepo.findByTutorId(tutorId);
            System.out.println("DEBUG: Suscripciones encontradas en BD para ID " + tutorId + ": " + subs.size());

            for (PushSubscription s : subs) {
                pushService.sendNotification(s, mensaje);
            }
        } else {
            System.out.println("ERROR: El infante " + infante.getId() + " tiene el tutor en NULL en memoria.");
        }
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