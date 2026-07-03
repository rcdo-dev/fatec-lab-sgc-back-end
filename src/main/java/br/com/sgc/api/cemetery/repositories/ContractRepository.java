package br.com.sgc.api.cemetery.repositories;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sgc.api.cemetery.entity.ContractEntity;
import br.com.sgc.api.common.enums.ContractStatus;

public interface ContractRepository extends JpaRepository<ContractEntity, Long> {

    boolean existsByNumber(String number);

    boolean existsByNumberAndIdNot(String number, Long id);

    boolean existsByGraveIdAndStatusIn(Long graveId, Collection<ContractStatus> statuses);

    List<ContractEntity> findByStatusAndEndDateBefore(ContractStatus status, LocalDate date);
}
