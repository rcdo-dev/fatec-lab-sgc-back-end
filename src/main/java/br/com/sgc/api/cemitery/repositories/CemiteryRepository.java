package br.com.sgc.api.cemitery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CemiteryRepository extends JpaRepository<CemiteryRepository, Long> {

}
