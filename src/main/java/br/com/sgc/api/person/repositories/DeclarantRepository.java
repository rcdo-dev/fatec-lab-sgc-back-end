package br.com.sgc.api.person.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sgc.api.person.entity.DeclarantEntity;

public interface DeclarantRepository extends JpaRepository<DeclarantEntity, Long> {

}
