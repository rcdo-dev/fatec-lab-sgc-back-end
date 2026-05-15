package br.com.sgc.api.person.service;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import br.com.sgc.api.common.exception.classes.BusinessException;
import br.com.sgc.api.common.exception.classes.ConflictException;
import br.com.sgc.api.common.exception.classes.ResourceNotFoundException;
import br.com.sgc.api.person.dto.request.DeathRequestDTO;
import br.com.sgc.api.person.dto.response.DeathResponseDTO;
import br.com.sgc.api.person.entity.DeathEntity;
import br.com.sgc.api.person.mapper.DeathMapper;
import br.com.sgc.api.person.repositories.DeathRepository;
import br.com.sgc.api.person.repositories.DeceasedIdentifiedRepository;
import br.com.sgc.api.person.repositories.DeceasedPetRepository;
import br.com.sgc.api.person.repositories.DeceasedRepository;
import br.com.sgc.api.person.repositories.DeceasedUnidentifiedRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeathService {

    // ============================================================================================
    // DEPENDENCIES
    // ============================================================================================

    private final DeathRepository deathRepository;
    private final DeceasedRepository deceasedRepository;
    private final DeathMapper mapper;
    private final MessageSource messageSource;

    // ============================================================================================
    // PUBLIC METHODS
    // ============================================================================================

    public DeathResponseDTO save(DeathRequestDTO request) {
        var death = mapper.toEntity(request);

        if (deathRepository.existsByDeceasedId(request.deceasedId())) {
            throw new ConflictException(getMessage("death.already.exists"));
        }

         // Polimorfismo aplicado aqui.
        var deceased = deceasedRepository.findById(request.deceasedId())
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("deceased.not.found")));
        death.setDeceased(deceased);

        return mapper.toResponse(deathRepository.save(death));

    }

    // ============================================================================================
    // VALIDATIONS
    // ============================================================================================

    // private void validateDeathAssociations(DeathEntity death) {

    // long associations = Stream.of(
    // death.getDeceasedIdentified(),
    // death.getDeceasedUnidentified(),
    // death.getDeceasedPet()
    // )
    // .filter(Objects::nonNull)
    // .count();

    // if (associations != 1) {
    // throw new BusinessException(getMessage("death.invalid.association"));
    // }
    // }

    // ============================================================================================
    // UPDATE HELPERS
    // ============================================================================================

    // ============================================================================================
    // FIND METHODS
    // ============================================================================================

    // private Object findByDeceasedEntityId(Long id) {
    // return Stream
    // .of(deceasedIdentifiedRepository.findById(id),
    // deceasedPetRepository.findById(id),
    // deceasedUnidentifiedRepository.findById(id))
    // .filter(Optional::isPresent).map(Optional::get).findFirst()
    // .orElseThrow(() -> new
    // BusinessException(getMessage("death.invalid.association")));
    // }

    // ============================================================================================
    // MESSAGE METHODS
    // ============================================================================================

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, "Messagem não encontrada " + key, LocaleContextHolder.getLocale());
    }

}
