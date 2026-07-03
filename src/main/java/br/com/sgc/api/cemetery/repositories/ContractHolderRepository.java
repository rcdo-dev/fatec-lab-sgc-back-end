package br.com.sgc.api.cemetery.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sgc.api.cemetery.entity.ContractHolderEntity;

public interface ContractHolderRepository extends JpaRepository<ContractHolderEntity, Long> {

    Optional<ContractHolderEntity> findByCpf(String cpf);
}
