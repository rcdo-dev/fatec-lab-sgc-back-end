package br.com.sgc.api.cemetery.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sgc.api.cemetery.entity.WakeConfigurationEntity;

public interface WakeConfigurationRepository extends JpaRepository<WakeConfigurationEntity, Long> {

    Optional<WakeConfigurationEntity> findByCemeteryId(Long cemeteryId);
}
