package br.com.sgc.api.person.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sgc.api.person.entity.DeceasedIdentifiedEntity;

public interface DeceasedIdentifiedRepository extends JpaRepository<DeceasedIdentifiedEntity, Long> {
    boolean existsByDocument_RgIgnoreCase(String rg);

    boolean existsByDocument_CpfIgnoreCase(String cpf);

    boolean existsByDocument_RgIgnoreCaseAndIdNot(String rg, Long id);

    boolean existsByDocument_CpfIgnoreCaseAndIdNot(String cpf, Long id);

    boolean existsByDeclarantId(Long declarantId);
}
