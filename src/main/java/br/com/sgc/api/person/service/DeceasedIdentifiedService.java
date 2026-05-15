package br.com.sgc.api.person.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sgc.api.common.enums.DeceasedStatus;
import br.com.sgc.api.common.exception.classes.BusinessException;
import br.com.sgc.api.common.exception.classes.ConflictException;
import br.com.sgc.api.common.exception.classes.ResourceNotFoundException;

import br.com.sgc.api.person.dto.request.DeceasedIdentifiedRequestDTO;
import br.com.sgc.api.person.dto.request.support.DocumentInfoRequestDTO;
import br.com.sgc.api.person.dto.response.DeceasedIdentifiedResponseDTO;
import br.com.sgc.api.person.entity.DeceasedIdentifiedEntity;
import br.com.sgc.api.person.entity.embeddable.DocumentInfo;
import br.com.sgc.api.person.mapper.DeceasedIdentifiedMapper;
import br.com.sgc.api.person.repositories.DeceasedIdentifiedRepository;
import br.com.sgc.api.person.repositories.DeclarantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeceasedIdentifiedService {

    // ============================================================================================
    // DEPENDENCIES
    // ============================================================================================

    private final DeceasedIdentifiedRepository deceasedRepository;
    private final DeclarantRepository declarantRepository;
    private final DeceasedIdentifiedMapper mapper;
    private final MessageSource messageSource;

    // ============================================================================================
    // PUBLIC METHODS
    // ============================================================================================

    public DeceasedIdentifiedResponseDTO save(DeceasedIdentifiedRequestDTO request) {
        var deceased = mapper.toEntity(request);

        var declarant = declarantRepository.findById(request.declarantId())
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("declarant.not.found")));

        deceased.setDeclarant(declarant);
        deceased.setStatus(DeceasedStatus.ACTIVE);
        
        validateDuplicatedDocuments(request);

        return mapper.toResponse(deceasedRepository.save(deceased));
    }

    public List<DeceasedIdentifiedResponseDTO> findAll() {
        return deceasedRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    public DeceasedIdentifiedResponseDTO findById(Long id) {
        return mapper.toResponse(findDeceasedById(id));
    }

    @Transactional
    public DeceasedIdentifiedResponseDTO update(Long id, DeceasedIdentifiedRequestDTO request) {
        var deceased = findDeceasedById(id);

        validateUpdateDocuments(id, request);

        updateData(deceased, request);

        return mapper.toResponse(deceasedRepository.save(deceased));
    }

    public void archive(Long id) {
        var deceased = findDeceasedById(id);

        deceased.setStatus(DeceasedStatus.ARCHIVED);
        deceased.setArchived(true);
        deceased.setArchivedAt(LocalDateTime.now());

        deceasedRepository.save(deceased);
    }

    // ============================================================================================
    // VALIDATIONS
    // ============================================================================================

    private void validateDuplicatedDocuments(DeceasedIdentifiedRequestDTO request) {
        if (deceasedRepository.existsByDocument_RgIgnoreCase(request.document().rg())
                || deceasedRepository.existsByDocument_CpfIgnoreCase(request.document().cpf())) {
            throw new ConflictException(getMessage("deceased.already.exists"));
        }
    }

    private void validateUpdateDocuments(Long id, DeceasedIdentifiedRequestDTO request) {
        if (deceasedRepository.existsByDocument_RgIgnoreCaseAndIdNot(request.document().rg(), id)) {
            throw new BusinessException(getMessage("deceased.rg.already.exists"));
        }

        if (deceasedRepository.existsByDocument_CpfIgnoreCaseAndIdNot(request.document().cpf(), id)) {
            throw new BusinessException(getMessage("deceased.cpf.already.exists"));
        }
    }

    // ============================================================================================
    // UPDATE HELPERS
    // ============================================================================================

    private void updateData(DeceasedIdentifiedEntity deceased, DeceasedIdentifiedRequestDTO request) {
        deceased.setName(request.name());
        deceased.setBirthDate(request.birthDate());
        deceased.setGender(request.gender());
        deceased.setDocument(buildDocumentInfo(request.document()));
        deceased.setOccupation(request.occupation());
        deceased.setFathersName(request.fathersName());
        deceased.setMothersName(request.mothersName());
        deceased.setNaturalness(request.naturalness());
        deceased.setCityResident(request.cityResident());
        deceased.setObservations(request.observations());
    }

    private DocumentInfo buildDocumentInfo(DocumentInfoRequestDTO document) {
        return new DocumentInfo(document.rg(), document.cpf());
    }

    // ============================================================================================
    // FIND METHODS
    // ============================================================================================

    private DeceasedIdentifiedEntity findDeceasedById(Long id) {
        if (id == null) {
            throw new BusinessException("deceased.id.required");
        }
        return deceasedRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("deceased.not.found"));
    }

    // ============================================================================================
    // MESSAGE METHODS
    // ============================================================================================

    private String getMessage(String key) {
        return messageSource.getMessage(Objects.requireNonNull(key), null, "Messagem nao encontrada: " + key,
                LocaleContextHolder.getLocale());
    }

}
