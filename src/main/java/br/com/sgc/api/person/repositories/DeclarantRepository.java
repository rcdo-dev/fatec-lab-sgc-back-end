package br.com.sgc.api.person.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sgc.api.person.entity.DeclarantEntity;

public interface DeclarantRepository extends JpaRepository<DeclarantEntity, Long> {
    boolean existsByDocument_RgIgnoreCase(String rg);

    boolean existsByDocument_CpfIgnoreCase(String cpf);

    boolean existsByDocument_RgIgnoreCaseAndIdNot(String rg, Long id);

    boolean existsByDocument_CpfIgnoreCaseAndIdNot(String cpf, Long id);

}
