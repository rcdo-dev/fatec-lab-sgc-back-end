package br.com.sgc.api.cemetery.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import br.com.sgc.api.cemetery.dto.request.CemeteryRequestDTO;
import br.com.sgc.api.cemetery.dto.response.CemeteryResponseDTO;
import br.com.sgc.api.cemetery.entity.CemeteryEntity;
import br.com.sgc.api.cemetery.mapper.CemeteryMapper;
import br.com.sgc.api.cemetery.repositories.CemeteryRepository;

import br.com.sgc.api.common.exception.BusinessException;
import br.com.sgc.api.common.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CemeteryService {
    private final CemeteryRepository cemeteryRepository;
    private final CemeteryMapper mapper;

    public CemeteryResponseDTO save(CemeteryRequestDTO request) {

        if (cemeteryRepository.existsByNameIgnoreCase(request.name())) {
            throw new BusinessException("Já existe um cemitério cadastrado com esse nome.");
        }

        /**
         * Objects.requireNonNull() -> Verifica se o objeto em questão é nulo.
         */
        var entity = Objects.requireNonNull(
                mapper.toEntity(request),
                "Erro ao mapear CemeteryRequestDTO para CemeteryEntity.");

        var entitySaved = cemeteryRepository.save(entity);

        return mapper.toResponse(entitySaved);
    }

    public List<CemeteryResponseDTO> findAll() {
        return cemeteryRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public CemeteryResponseDTO findById(Long id) {
        return mapper.toResponse(findEntityById(id));
    }

    public CemeteryResponseDTO update(Long id, CemeteryRequestDTO request) {
        var entity = findEntityById(id);

        if (cemeteryRepository.existsByNameIgnoreCaseAndIdNot(request.name(), id)) {
            throw new BusinessException("Já existe um cemitério cadastrado com esse nome.");
        }

        entity.setName(request.name());
        entity.setFoundation(request.foundation());
        entity.setActive(request.active());

        cemeteryRepository.save(entity);

        return mapper.toResponse(entity);
    }

    public CemeteryResponseDTO inactivate(Long id) {
        var entity = findEntityById(id);
        entity.setActive(false);

        return mapper.toResponse(cemeteryRepository.save(entity));
    }

    private CemeteryEntity findEntityById(Long id) {
        if (id == null) {
            throw new BusinessException("Id não pode ser nulo.");
        }

        return cemeteryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cemitério não encontrado"));
    }
}