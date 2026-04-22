package br.com.sgc.api.person.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sgc.api.person.entity.DeceasedEntity;

public interface DeceasedRepository extends JpaRepository<DeceasedEntity, Long> {

}
