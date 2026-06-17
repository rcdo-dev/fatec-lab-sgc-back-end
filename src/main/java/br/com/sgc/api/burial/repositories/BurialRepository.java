package br.com.sgc.api.burial.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sgc.api.burial.entity.BurialEntity;
import br.com.sgc.api.common.enums.BurialStatus;

public interface BurialRepository extends JpaRepository<BurialEntity, Long> {

    boolean existsByDeceasedIdAndStatus(Long deceasedId, BurialStatus status);

    boolean existsByGraveIdAndStatus(Long graveId, BurialStatus status);

    List<BurialEntity> findByDeceasedId(Long deceasedId);

    List<BurialEntity> findByGraveId(Long graveId);

    List<BurialEntity> findByStatus(BurialStatus status);

    long countByGraveIdAndStatus(Long graveId, BurialStatus status);

    long countByGraveIdAndStatusAndIdNot(Long graveId, BurialStatus status, Long id);
}
