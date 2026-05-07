package br.com.sgc.api.person.service;

import java.util.List;
import java.util.Objects;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import br.com.sgc.api.common.exception.classes.BusinessException;
import br.com.sgc.api.common.exception.classes.ConflictException;
import br.com.sgc.api.common.exception.classes.ResourceNotFoundException;
import br.com.sgc.api.person.dto.request.DeclarantRequestDTO;
import br.com.sgc.api.person.dto.request.support.AddressInfoRequestDTO;
import br.com.sgc.api.person.dto.request.support.ContactInfoRequestDTO;
import br.com.sgc.api.person.dto.request.support.DocumentInfoRequestDTO;
import br.com.sgc.api.person.dto.response.DeclarantResponseDTO;
import br.com.sgc.api.person.entity.DeclarantEntity;
import br.com.sgc.api.person.entity.embeddable.AddressInfo;
import br.com.sgc.api.person.entity.embeddable.ContatctInfo;
import br.com.sgc.api.person.entity.embeddable.DocumentInfo;
import br.com.sgc.api.person.mapper.DeclarantMapper;
import br.com.sgc.api.person.repositories.DeclarantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeclarantService {

    // ============================================================================================
    // DEPENDENCIES
    // ============================================================================================

    private final DeclarantRepository declarantRepository;
    private final DeclarantMapper mapper;
    private final MessageSource messageSource;

    // ============================================================================================
    // PUBLIC METHODS
    // ============================================================================================

    public DeclarantResponseDTO save(DeclarantRequestDTO request) {
        var declarant = mapper.toEntity(request);

        validateDuplicatedDocuments(request);

        var declarantSaved = declarantRepository.save(declarant);

        return mapper.toResponse(declarantSaved);
    }

    public List<DeclarantResponseDTO> findAll() {
        return declarantRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    public DeclarantResponseDTO findById(Long id) {
        return mapper.toResponse(findByDeclarantId(id));
    }

    public DeclarantResponseDTO update(Long id, DeclarantRequestDTO request) {
        var declarant = findByDeclarantId(id);

        validateUpdateDocuments(id, request);

        updateData(declarant, request);

        return mapper.toResponse(declarantRepository.save(declarant));
    }

    public void delete(Long id) {
        var declarant = findByDeclarantId(id);
        declarantRepository.delete(declarant);
    }

    // ============================================================================================
    // VALIDATIONS
    // ============================================================================================

    private void validateDuplicatedDocuments(DeclarantRequestDTO request) {
        if (declarantRepository.existsByDocument_RgIgnoreCase(request.document().rg())
                || declarantRepository.existsByDocument_CpfIgnoreCase(request.document().cpf())) {
            throw new ConflictException(getMessage("declarant.already.exists"));
        }
    }

    private void validateUpdateDocuments(Long id, DeclarantRequestDTO request) {
        if (declarantRepository.existsByDocument_RgIgnoreCaseAndIdNot(request.document().rg(), id)) {
            throw new BusinessException(getMessage("declarant.rg.already.exists"));
        }

        if (declarantRepository.existsByDocument_CpfIgnoreCaseAndIdNot(request.document().cpf(), id)) {
            throw new BusinessException(getMessage("declarant.cpf.already.exists"));
        }
    }

    // ============================================================================================
    // UPDATE HELPERS
    // ============================================================================================

    private void updateData(DeclarantEntity declarant, DeclarantRequestDTO request) {
        declarant.setName(request.name());
        declarant.setDocument(buildDocumentInfo(request.document()));
        declarant.setContact(buildContatctInfo(request.contact()));
        declarant.setAddress(buildAddressInfo(request.address()));
        declarant.setOccupation(request.occupation());
    }

    private DocumentInfo buildDocumentInfo(DocumentInfoRequestDTO document) {
        return new DocumentInfo(document.rg(), document.cpf());
    }

    private ContatctInfo buildContatctInfo(ContactInfoRequestDTO contact) {
        return new ContatctInfo(contact.phone(), contact.email());
    }

    private AddressInfo buildAddressInfo(AddressInfoRequestDTO address) {
        return new AddressInfo(address.street(), address.number(), address.district(), address.city(), address.state(),
                address.cep());
    }

    // ============================================================================================
    // FIND METHODS
    // ============================================================================================

    private DeclarantEntity findByDeclarantId(Long id) {
        if (id == null)
            throw new BusinessException(getMessage("declarant.id.required"));
        return declarantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("declarant.not.found")));
    }

    // ============================================================================================
    // MESSAGE METHODS
    // ============================================================================================

    private String getMessage(String key) {
        return messageSource.getMessage(Objects.requireNonNull(key), null, "Messagem nao encontrada: " + key,
                LocaleContextHolder.getLocale());
    }
}
