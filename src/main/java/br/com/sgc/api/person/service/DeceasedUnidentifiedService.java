package br.com.sgc.api.person.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sgc.api.common.enums.DeceasedStatus;
import br.com.sgc.api.common.exception.classes.ResourceNotFoundException;

import br.com.sgc.api.person.dto.request.DeceasedUnidentifiedRequestDTO;
import br.com.sgc.api.person.dto.response.DeceasedUnidentifiedResponseDTO;
import br.com.sgc.api.person.entity.DeceasedUnidentifiedEntity;
import br.com.sgc.api.person.mapper.DeceasedUnidentifiedMapper;
import br.com.sgc.api.person.repositories.DeceasedUnidentifiedRepository;
import br.com.sgc.api.person.repositories.DeclarantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeceasedUnidentifiedService {

    // ============================================================================================
    // DEPENDENCIES
    // ============================================================================================

    private final DeceasedUnidentifiedRepository deceasedUnidentifiedRepository;
    private final DeclarantRepository declarantRepository;
    private final DeceasedUnidentifiedMapper mapper;
    private final MessageSource messageSource;

    // ============================================================================================
    // PUBLIC METHODS
    // ============================================================================================

    public DeceasedUnidentifiedResponseDTO save(DeceasedUnidentifiedRequestDTO request) {
        var deceasedUnidentified = mapper.toEntity(request);

        var declarant = declarantRepository.findById(request.declarantId())
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("declarant.not.found")));

        deceasedUnidentified.setDeclarant(declarant);
        deceasedUnidentified.setDeceasedStatus(DeceasedStatus.ACTIVE);

        return mapper.toResponse(deceasedUnidentifiedRepository.save(deceasedUnidentified));
    }

    public List<DeceasedUnidentifiedResponseDTO> findAll() {
        return deceasedUnidentifiedRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    public DeceasedUnidentifiedResponseDTO findById(Long id) {
        return mapper.toResponse(findByDeceasedUnidentifiedId(id));
    }

    @Transactional
    public DeceasedUnidentifiedResponseDTO update(Long id, DeceasedUnidentifiedRequestDTO request) {
        var deceased = findByDeceasedUnidentifiedId(id);

        updateData(deceased, request);

        return mapper.toResponse(deceasedUnidentifiedRepository.save(deceased));
    }

    public void archived(Long id) {
        var deceased = findByDeceasedUnidentifiedId(id);

        deceased.setArchived(true);
        deceased.setDeceasedStatus(DeceasedStatus.ARCHIVED);
        deceased.setArchivedAt(LocalDateTime.now());

        deceasedUnidentifiedRepository.save(deceased);
    }

    // ============================================================================================
    // UPDATE HELPERS
    // ============================================================================================

    private void updateData(DeceasedUnidentifiedEntity deceasedUnidentified, DeceasedUnidentifiedRequestDTO request) {
        deceasedUnidentified.setEstimatedAge(request.estimatedAge());
        deceasedUnidentified.setGender(request.gender());
        deceasedUnidentified.setSkinColor(request.skinColor());
        deceasedUnidentified.setStature(request.stature());
        deceasedUnidentified.setHairColor(request.hairColor());
        deceasedUnidentified.setHairType(request.hairType());
        deceasedUnidentified.setEyeColor(request.eyeColor());
        deceasedUnidentified.setEyeType(request.eyeType());
    }

    // ============================================================================================
    // FIND METHODS
    // ============================================================================================

    private DeceasedUnidentifiedEntity findByDeceasedUnidentifiedId(Long id) {
        return deceasedUnidentifiedRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("deceased.not.found")));
    }

    // ============================================================================================
    // MESSAGE METHODS
    // ============================================================================================

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, "Messagem não encontrada " + key, LocaleContextHolder.getLocale());
    }
}
