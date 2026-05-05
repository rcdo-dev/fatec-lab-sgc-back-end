package br.com.sgc.api.person.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sgc.api.person.entity.DeceasedUnidentifiedEntity;

public interface DeceasedUnidentifiedRepository extends JpaRepository<DeceasedUnidentifiedEntity, Long> {

}
