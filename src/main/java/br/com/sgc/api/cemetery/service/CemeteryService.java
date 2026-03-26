package br.com.sgc.api.cemetery.service;

import java.util.List;
import java.util.Objects;

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

    public CemeteryResponseDTO save(CemeteryRequestDTO request) {

        if (cemeteryRepository.existsByNameIgnoreCase(request.name())) {
            throw new ConflictException("Já existe um cemitério cadastrado com esse nome.");
        }

        /**
         * Objects.requireNonNull() -> Verifica se o objeto em questão é nulo.
         */
        var cemetery = Objects.requireNonNull(
                mapper.toEntity(request),
                "Erro ao mapear CemeteryRequestDTO para CemeteryEntity.");

        var cemeterySaved = cemeteryRepository.save(cemetery);

        return mapper.toResponse(cemeterySaved);
    }

    public List<CemeteryResponseDTO> findAll() {
        return cemeteryRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public CemeteryResponseDTO findById(Long id) {
        return mapper.toResponse(findCemeteryById(id));
    }

    public CemeteryResponseDTO update(Long id, CemeteryRequestDTO request) {
        var cemetery = findCemeteryById(id);

        if (cemeteryRepository.existsByNameIgnoreCaseAndIdNot(request.name(), id)) {
            throw new ConflictException("Já existe um cemitério cadastrado com esse nome.");
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
            throw new BusinessException("Id não pode ser nulo.");
        }

        return cemeteryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cemitério não encontrado"));
    }
}