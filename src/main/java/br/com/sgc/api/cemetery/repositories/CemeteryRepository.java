package br.com.sgc.api.cemetery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sgc.api.cemetery.entity.CemeteryEntity;

public interface CemeteryRepository extends JpaRepository<CemeteryEntity, Long> {
    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
