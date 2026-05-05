package br.com.sgc.api.person.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sgc.api.person.entity.DeceasedPetEntity;

public interface DeceasedPetRepository extends JpaRepository<DeceasedPetEntity, Long> {

}
