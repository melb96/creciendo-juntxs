package com.unlar.guarderia.Services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unlar.guarderia.Entitites.ActividadInfante;
import com.unlar.guarderia.Entitites.Asistencia;
import com.unlar.guarderia.Entitites.Infante;
import com.unlar.guarderia.Entitites.PrioridadActividad;
import com.unlar.guarderia.Entitites.TipoEventoActividad;
import com.unlar.guarderia.Entitites.Usuario;
import com.unlar.guarderia.Repositories.ActividadInfanteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActividadInfanteService {

    private final ActividadInfanteRepository actividadInfanteRepository;

    @Transactional
    public ActividadInfante registrarActividad(
            Infante infante,
            Asistencia asistencia,
            TipoEventoActividad tipoEvento,
            String estadoAnterior,
            String estadoNuevo,
            String descripcion,
            Usuario usuario,
            boolean notificadoTutor,
            String canalNotificacion,
            PrioridadActividad prioridad) {

        ActividadInfante actividad = new ActividadInfante();
        actividad.setInfante(infante);
        actividad.setAsistencia(asistencia);
        actividad.setTipoEvento(tipoEvento);
        actividad.setEstadoAnterior(estadoAnterior);
        actividad.setEstadoNuevo(estadoNuevo);
        actividad.setDescripcion(descripcion);
        actividad.setFechaHora(LocalDateTime.now());
        actividad.setUsuario(usuario);
        actividad.setNotificadoTutor(notificadoTutor);
        actividad.setCanalNotificacion(canalNotificacion);
        actividad.setPrioridad(prioridad != null ? prioridad : PrioridadActividad.NORMAL);

        return actividadInfanteRepository.save(actividad);
    }

    @Transactional
    public ActividadInfante registrarIngreso(
            Infante infante,
            Asistencia asistencia,
            String descripcion,
            Usuario usuario) {
        return registrarActividad(
                infante,
                asistencia,
                TipoEventoActividad.INGRESO,
                null,
                "En Sala",
                descripcion,
                usuario,
                false,
                null,
                PrioridadActividad.NORMAL);
    }

    @Transactional
    public ActividadInfante registrarCambioEstado(
            Infante infante,
            Asistencia asistencia,
            String estadoAnterior,
            String estadoNuevo,
            String descripcion,
            Usuario usuario) {
        return registrarActividad(
                infante,
                asistencia,
                TipoEventoActividad.CAMBIO_ESTADO,
                estadoAnterior,
                estadoNuevo,
                descripcion,
                usuario,
                false,
                null,
                PrioridadActividad.NORMAL);
    }

    @Transactional
    public ActividadInfante registrarAlerta(
            Infante infante,
            Asistencia asistencia,
            String descripcion,
            Usuario usuario,
            PrioridadActividad prioridad) {
        return registrarActividad(
                infante,
                asistencia,
                TipoEventoActividad.ALERTA,
                infante != null ? infante.getEstadoActual() : null,
                infante != null ? infante.getEstadoActual() : null,
                descripcion,
                usuario,
                false,
                null,
                prioridad != null ? prioridad : PrioridadActividad.ALTA);
    }

    @Transactional
    public ActividadInfante registrarEgreso(
            Infante infante,
            Asistencia asistencia,
            String estadoAnterior,
            String descripcion,
            Usuario usuario) {
        return registrarActividad(
                infante,
                asistencia,
                TipoEventoActividad.EGRESO,
                estadoAnterior,
                "Retirado",
                descripcion,
                usuario,
                false,
                null,
                PrioridadActividad.NORMAL);
    }

    @Transactional(readOnly = true)
    public List<ActividadInfante> obtenerHistorialInfante(Long infanteId) {
        return actividadInfanteRepository.findByInfanteIdOrderByFechaHoraDesc(infanteId);
    }
}
