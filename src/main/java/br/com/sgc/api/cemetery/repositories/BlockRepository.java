package br.com.sgc.api.cemetery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sgc.api.cemetery.entity.BlockEntity;

public interface BlockRepository extends JpaRepository<BlockEntity, Long> {

    boolean existsByNumberAndCemeteryId(int number, Long cemeteryId);

}
