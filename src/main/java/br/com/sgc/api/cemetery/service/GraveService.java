package br.com.sgc.api.cemetery.service;

import java.util.Objects;

import org.springframework.stereotype.Service;

import br.com.sgc.api.cemetery.dto.request.GraveRequestDTO;
import br.com.sgc.api.cemetery.dto.response.GraveResponseDTO;
import br.com.sgc.api.cemetery.mapper.GraveMapper;
import br.com.sgc.api.cemetery.repositories.BlockRepository;
import br.com.sgc.api.cemetery.repositories.GraveRepository;
import br.com.sgc.api.common.enums.AreaType;
import br.com.sgc.api.common.enums.GraveType;
import br.com.sgc.api.common.exception.BusinessException;
import br.com.sgc.api.common.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GraveService {
    private final GraveRepository graveRepository;
    private final BlockRepository blockRepository;
    private final GraveMapper mapper;

    public GraveResponseDTO save(GraveRequestDTO request) {

        validateAreaTypeAndGraveType(request.areaType(), request.graveType());
        validateBodyCapacity(request.graveType(), request.bodyCapacity());

        if (graveRepository.existsByNumberAndBlockId(request.number(), request.blockId())) {
            throw new BusinessException("Já existe uma sepultura com essse número nessa quadra.");
        }

        var blockEntity = blockRepository
                .findById(Objects.requireNonNull(request.blockId(), "O Id da quadra não pode ser nulo"))
                .orElseThrow(() -> new ResourceNotFoundException("Quadra não encontrada."));

        var graveEntity = mapper.toEntity(request);
        graveEntity.setBlock(blockEntity);

        return mapper.toResponse(graveRepository.save(graveEntity));
    }

    private void validateAreaTypeAndGraveType(AreaType areaType, GraveType graveType) {
        if (areaType == AreaType.COMMON && graveType != GraveType.EARTH) {
            throw new BusinessException("Sepultura de área comum deve ser do tipo terra.");
        }
    }

    private void validateBodyCapacity(GraveType graveType, int bodyCapacity) {
        if (graveType == GraveType.EARTH && bodyCapacity > 2) {
            throw new BusinessException("Sepultura do tipo terra deve ter capacidade máxima de 2 corpos.");
        }

        if (graveType == GraveType.MAUSOLEUM && bodyCapacity > 4) {
            throw new BusinessException("Jazigo deve ter capacidade máxima de 4 corpos.");
        }
    }

}
