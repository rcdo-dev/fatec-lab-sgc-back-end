package br.com.sgc.api.cemetery.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import br.com.sgc.api.cemetery.dto.request.CemeteryRequestDTO;
import br.com.sgc.api.cemetery.dto.response.CemeteryResponseDTO;
import br.com.sgc.api.cemetery.entity.CemeteryEntity;
import br.com.sgc.api.cemetery.repositories.CemeteryRepository;
import br.com.sgc.api.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CemeteryService {
    private final CemeteryRepository cemeteryRepository;
    private final ModelMapper mapper;

    public CemeteryResponseDTO save(CemeteryRequestDTO request) {

        var entity = mapper.map(request, CemeteryEntity.class);

        if (cemeteryRepository.existsByNameIgnoreCase(request.name())) {
            throw new BusinessException("Já existe um cemitério cadastrado com esse nome.");
        }

        var entitySaved = cemeteryRepository.save(entity);
        return new CemeteryResponseDTO(
                entitySaved.getId(),
                entitySaved.getName(),
                entitySaved.getFoundation(),
                entitySaved.isActive());
    }
}