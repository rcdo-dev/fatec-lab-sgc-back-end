package br.com.sgc.api.person.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sgc.api.person.entity.DeceasedIdentifiedEntity;

public interface DeceasedIdentifiedRepository extends JpaRepository<DeceasedIdentifiedEntity, Long> {

}
