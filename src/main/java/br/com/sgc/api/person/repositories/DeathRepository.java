package br.com.sgc.api.person.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sgc.api.person.entity.DeathEntity;

public interface DeathRepository extends JpaRepository<DeathEntity, Long> {

}
