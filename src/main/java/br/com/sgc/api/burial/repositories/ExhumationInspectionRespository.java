package br.com.sgc.api.burial.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sgc.api.burial.entity.ExhumationInspectionEntity;

public interface ExhumationInspectionRespository extends JpaRepository<ExhumationInspectionEntity, Long> {

}
