package br.com.sgc.api.burial.repositories;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.sgc.api.burial.entity.WakeEntity;
import br.com.sgc.api.common.enums.WakeStatus;

public interface WakeRepository extends JpaRepository<WakeEntity, Long> {

        /**
        * Verifica se existe algum velório agendado
        * cujo intervalo de horários intercepte o
        * intervalo informado.
        */
    @Query("""
            SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END
            FROM WakeEntity w
            WHERE w.cemetery.id = :cemeteryId
              AND w.date = :date
              AND w.status = :status
              AND w.startTime < :endTime
              AND w.endTime > :startTime
            """)
    boolean existsScheduleConflict(
            @Param("cemeteryId") Long cemeteryId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("status") WakeStatus status);
}
