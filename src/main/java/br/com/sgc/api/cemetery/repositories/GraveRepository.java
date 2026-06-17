package br.com.sgc.api.cemetery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sgc.api.cemetery.entity.GraveEntity;

public interface GraveRepository extends JpaRepository<GraveEntity, Long> {

    boolean existsByNumberAndBlockId(int number, Long blockId);

}
