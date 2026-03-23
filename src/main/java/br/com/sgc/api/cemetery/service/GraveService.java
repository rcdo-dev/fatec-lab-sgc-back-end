package br.com.sgc.api.cemetery.service;

import java.util.Objects;

import org.springframework.stereotype.Service;

import br.com.sgc.api.cemetery.dto.request.GraveRequestDTO;
import br.com.sgc.api.cemetery.dto.response.GraveResponseDTO;
import br.com.sgc.api.cemetery.mapper.GraveMapper;
import br.com.sgc.api.cemetery.repositories.BlockRepository;
import br.com.sgc.api.cemetery.repositories.GraveRepository;
import br.com.sgc.api.common.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GraveService {
    private final GraveRepository graveRepository;
    private final BlockRepository blockRepository;
    private final GraveMapper mapper;

    public GraveResponseDTO save(GraveRequestDTO request) {
        var blockEntity = blockRepository
                .findById(Objects.requireNonNull(request.blockId(), "O Id da quadra não pode ser nulo"))
                .orElseThrow(() -> new ResourceNotFoundException("Quadra não encontrada."));

        var graveEntity = mapper.toEntity(request);
        graveEntity.setBlock(blockEntity);

        return mapper.toResponse(graveRepository.save(graveEntity));
    }

}
