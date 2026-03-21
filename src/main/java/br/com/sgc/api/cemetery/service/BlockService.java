package br.com.sgc.api.cemetery.service;

import java.util.Objects;

import org.springframework.stereotype.Service;

import br.com.sgc.api.cemetery.dto.request.BlockRequestDTO;
import br.com.sgc.api.cemetery.dto.response.BlockResponseDTO;
import br.com.sgc.api.cemetery.mapper.BlockMapper;
import br.com.sgc.api.cemetery.repositories.BlockRepository;
import br.com.sgc.api.cemetery.repositories.CemeteryRepository;
import br.com.sgc.api.common.exception.BusinessException;
import br.com.sgc.api.common.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlockService {
    private final CemeteryRepository cemeteryRepository;
    private final BlockRepository blockRepository;
    private final BlockMapper mapper;

    public BlockResponseDTO save(BlockRequestDTO request) {
        if (blockRepository.existsByNumberAndCemeteryId(request.number(), request.cemeteryId())) {
            throw new BusinessException("Já existe uma quadra com esse número neste cemitério.");
        }

        var cemeteryEntity = cemeteryRepository
                .findById(Objects.requireNonNull(request.cemeteryId(), "O Id do cemitério não pode ser nulo."))
                .orElseThrow(() -> new ResourceNotFoundException("Cemitério não encontrado."));

        var blockEntity = mapper.toEntity(request);
        blockEntity.setCemetery(cemeteryEntity);

        var blockSaved = blockRepository.save(blockEntity);

        return mapper.toReponse(blockSaved);
    }

}
