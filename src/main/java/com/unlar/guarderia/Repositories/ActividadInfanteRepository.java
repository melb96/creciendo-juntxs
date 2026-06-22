package com.unlar.guarderia.Repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unlar.guarderia.Entitites.ActividadInfante;

public interface ActividadInfanteRepository extends JpaRepository<ActividadInfante, Long> {

    List<ActividadInfante> findByInfanteIdOrderByFechaHoraDesc(Long infanteId);

    List<ActividadInfante> findByAsistenciaIdOrderByFechaHoraAsc(Long asistenciaId);

    List<ActividadInfante> findByInfanteIdAndFechaHoraBetweenOrderByFechaHoraAsc(
            Long infanteId,
            LocalDateTime desde,
            LocalDateTime hasta);
}
