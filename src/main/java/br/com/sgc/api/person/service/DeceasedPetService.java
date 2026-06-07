package br.com.sgc.api.person.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import br.com.sgc.api.burial.repositories.BurialRepository;
import br.com.sgc.api.common.enums.BurialStatus;
import br.com.sgc.api.common.enums.DeceasedStatus;
import br.com.sgc.api.common.exception.classes.BusinessException;
import br.com.sgc.api.common.exception.classes.ResourceNotFoundException;
import br.com.sgc.api.person.dto.request.DeceasedPetRequestDTO;
import br.com.sgc.api.person.dto.response.DeceasedPetResponseDTO;
import br.com.sgc.api.person.entity.DeceasedPetEntity;
import br.com.sgc.api.person.mapper.DeceasedPetMapper;
import br.com.sgc.api.person.repositories.DeceasedPetRepository;
import br.com.sgc.api.person.repositories.DeclarantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeceasedPetService {

    // ============================================================================================
    // DEPENDENCIES
    // ============================================================================================

    private final DeceasedPetRepository deceasedPetRepository;
    private final DeclarantRepository declarantRepository;
    private final BurialRepository burialRepository;
    private final DeceasedPetMapper mapper;
    private final MessageSource messageSource;

    // ============================================================================================
    // PUBLIC METHODS
    // ============================================================================================

    public DeceasedPetResponseDTO save(DeceasedPetRequestDTO request) {
        var deceased = mapper.toEntity(request);

        var declarant = declarantRepository.findById(request.declarantId())
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("declarant.not.found")));
        deceased.setDeclarant(declarant);
        deceased.setStatus(DeceasedStatus.ACTIVE);
        deceased.setArchived(false);
        deceased.setArchivedAt(null);

        return mapper.toResponse(deceasedPetRepository.save(deceased));
    }

    public List<DeceasedPetResponseDTO> listAll() {
        return deceasedPetRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    public DeceasedPetResponseDTO findById(Long id) {
        return mapper.toResponse(findByDeceasedPetId(id));
    }

    public DeceasedPetResponseDTO update(Long id, DeceasedPetRequestDTO request) {
        var deceased = findByDeceasedPetId(id);

        validateNotArchived(deceased);
        updateData(deceased, request);

        return mapper.toResponse(deceasedPetRepository.save(deceased));
    }

    public void archived(Long id) {
        var deceased = findByDeceasedPetId(id);

        validateNotArchived(deceased);
        validateWithoutActiveBurial(deceased.getId());

        deceased.setArchived(true);
        deceased.setStatus(DeceasedStatus.ARCHIVED);
        deceased.setArchivedAt(LocalDateTime.now());

        deceasedPetRepository.save(deceased);
    }

    // ============================================================================================
    // VALIDATIONS
    // ============================================================================================

    private void validateNotArchived(DeceasedPetEntity deceased) {
        if (deceased.isArchived() || deceased.getStatus() == DeceasedStatus.ARCHIVED) {
            throw new BusinessException(getMessage("deceased.archived"));
        }
    }

    private void validateWithoutActiveBurial(Long deceasedId) {
        if (burialRepository.existsByDeceasedIdAndStatus(deceasedId, BurialStatus.IN_PROGRESS)) {
            throw new BusinessException(getMessage("deceased.active.burial"));
        }
    }

    // ============================================================================================
    // UPDATE HELPERS
    // ============================================================================================

    public void updateData(DeceasedPetEntity deceasedPetEntity, DeceasedPetRequestDTO request) {
        deceasedPetEntity.setName(request.name());
        deceasedPetEntity.setSpecies(request.species());
        deceasedPetEntity.setBreed(request.breed());
        deceasedPetEntity.setSex(request.sex());
        deceasedPetEntity.setColor(request.color());
        deceasedPetEntity.setEstimatedAge(request.estimatedAge());
    }

    // ============================================================================================
    // FIND METHODS
    // ============================================================================================

    private DeceasedPetEntity findByDeceasedPetId(Long id) {
        if (id == null) {
            throw new BusinessException(getMessage("deceased.id.required"));
        }
        return deceasedPetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("deceased.not.found")));
    }

    // ============================================================================================
    // MESSAGE METHODS
    // ============================================================================================

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, "Messagem não encontrada " + key, LocaleContextHolder.getLocale());
    }

}
