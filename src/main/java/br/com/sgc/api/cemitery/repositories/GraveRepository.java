package br.com.sgc.api.cemitery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sgc.api.cemitery.entity.GraveEntity;

public interface GraveRepository extends JpaRepository<GraveEntity, Long> {

}
