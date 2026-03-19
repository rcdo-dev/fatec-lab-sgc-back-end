package br.com.sgc.api.cemetery.service;

import java.util.Objects;

import org.springframework.stereotype.Service;

import br.com.sgc.api.cemetery.dto.request.CemeteryRequestDTO;
import br.com.sgc.api.cemetery.dto.response.CemeteryResponseDTO;
import br.com.sgc.api.cemetery.mapper.CemeteryMapper;
import br.com.sgc.api.cemetery.repositories.CemeteryRepository;

import br.com.sgc.api.common.exception.BusinessException;

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
}