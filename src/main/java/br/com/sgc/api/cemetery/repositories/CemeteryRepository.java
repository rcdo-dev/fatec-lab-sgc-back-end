package br.com.sgc.api.cemetery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.sgc.api.cemetery.entity.CemeteryEntity;

@Repository
public interface CemeteryRepository extends JpaRepository<CemeteryEntity, Long> {

}
