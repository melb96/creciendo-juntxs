package com.unlar.guarderia.Repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unlar.guarderia.Entitites.Asistencia;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    List<Asistencia> findByFecha(LocalDate fecha);
    List<Asistencia> findByInfanteId(Long infanteId);
    Optional<Asistencia> findByInfanteIdAndFechaAndHoraSalidaIsNull (Long infanteId, LocalDate fecha);
    List<Asistencia> findByInfanteIdOrderByFechaDescHoraEntradaDesc(Long infanteId);
}
