package br.com.sgc.api.cemetery.service;

import java.util.List;
import java.util.Objects;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import br.com.sgc.api.cemetery.dto.request.CemeteryRequestDTO;
import br.com.sgc.api.cemetery.dto.response.CemeteryResponseDTO;
import br.com.sgc.api.cemetery.entity.CemeteryEntity;
import br.com.sgc.api.cemetery.mapper.CemeteryMapper;
import br.com.sgc.api.cemetery.repositories.CemeteryRepository;

import br.com.sgc.api.common.exception.classes.BusinessException;
import br.com.sgc.api.common.exception.classes.ConflictException;
import br.com.sgc.api.common.exception.classes.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CemeteryService {
    private final CemeteryRepository cemeteryRepository;
    private final CemeteryMapper mapper;
    private final MessageSource messageSource;

    public CemeteryResponseDTO save(CemeteryRequestDTO request) {

        if (cemeteryRepository.existsByNameIgnoreCase(request.name())) {
            throw new ConflictException(getMessage("cemetery.name.already.exists"));
        }

        /**
         * Objects.requireNonNull() -> Verifica se o objeto em questão é nulo.
         */
        var cemetery = Objects.requireNonNull(mapper.toEntity(request),
                getMessage("validation.assigned.value.cannot.be.null"));

        var cemeterySaved = cemeteryRepository.save(cemetery);

        return mapper.toResponse(cemeterySaved);
    }

    public List<CemeteryResponseDTO> findAll() {
        return cemeteryRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    public CemeteryResponseDTO findById(Long id) {
        return mapper.toResponse(findCemeteryById(id));
    }

    public CemeteryResponseDTO update(Long id, CemeteryRequestDTO request) {
        var cemetery = findCemeteryById(id);

        if (cemeteryRepository.existsByNameIgnoreCaseAndIdNot(request.name(), id)) {
            throw new ConflictException(getMessage("cemetery.name.already.exists"));
        }

        cemetery.setName(request.name());
        cemetery.setFoundation(request.foundation());
        cemetery.setActive(request.active());

        cemeteryRepository.save(cemetery);

        return mapper.toResponse(cemetery);
    }

    public CemeteryResponseDTO inactivate(Long id) {
        var cemetery = findCemeteryById(id);
        cemetery.setActive(false);

        return mapper.toResponse(cemeteryRepository.save(cemetery));
    }

    private CemeteryEntity findCemeteryById(Long id) {
        if (id == null) {
            throw new BusinessException(getMessage("cemetery.id.required"));

        }

        return cemeteryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("cemetery.not.found")));

    }

    private String getMessage(String key) {
        return messageSource.getMessage(
                Objects.requireNonNull(key, getMessage("validation.assigned.value.cannot.be.null")), null,
                "Messagem nao encontrada: " + key, LocaleContextHolder.getLocale());
    }
}