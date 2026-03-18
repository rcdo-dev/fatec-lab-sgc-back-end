package br.com.sgc.api.cemitery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sgc.api.cemitery.entity.BlockEntity;

public interface BlockRepository extends JpaRepository<BlockEntity, Long> {

}
