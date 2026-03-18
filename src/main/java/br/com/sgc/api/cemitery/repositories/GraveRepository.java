package br.com.sgc.api.cemitery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.sgc.api.cemitery.entity.GraveEntity;

@Repository
public interface GraveRepository extends JpaRepository<GraveEntity, Long> {

}
