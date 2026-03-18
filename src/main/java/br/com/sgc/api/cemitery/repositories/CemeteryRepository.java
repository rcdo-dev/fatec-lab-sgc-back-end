package br.com.sgc.api.cemitery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.sgc.api.cemitery.entity.CemeteryEntity;

@Repository
public interface CemeteryRepository extends JpaRepository<CemeteryEntity, Long> {

}
