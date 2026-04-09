package br.com.sgc.api.cemetery.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import br.com.sgc.api.cemetery.dto.request.BlockRequestDTO;
import br.com.sgc.api.cemetery.dto.request.BlockUpdateRequestDTO;
import br.com.sgc.api.cemetery.dto.response.BlockResponseDTO;
import br.com.sgc.api.cemetery.entity.BlockEntity;
import br.com.sgc.api.cemetery.mapper.BlockMapper;
import br.com.sgc.api.cemetery.repositories.BlockRepository;
import br.com.sgc.api.cemetery.repositories.CemeteryRepository;
import br.com.sgc.api.common.exception.classes.BusinessException;
import br.com.sgc.api.common.exception.classes.ConflictException;
import br.com.sgc.api.common.exception.classes.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlockService {
    private final CemeteryRepository cemeteryRepository;
    private final BlockRepository blockRepository;
    private final BlockMapper mapper;

    public BlockResponseDTO save(BlockRequestDTO request) {
        if (blockRepository.existsByNumberAndCemeteryId(request.number(), request.cemeteryId())) {
            throw new ConflictException("block.number.already.exists");
        }

        var cemeteryEntity = cemeteryRepository
                .findById(Objects.requireNonNull(request.cemeteryId(), "cemetery.id.required"))
                .orElseThrow(() -> new ResourceNotFoundException("cemetery.not.found"));

        var blockEntity = mapper.toEntity(request);
        blockEntity.setActive(true);
        blockEntity.setCemetery(cemeteryEntity);

        return mapper.toResponse(blockRepository.save(blockEntity));
    }

    public List<BlockResponseDTO> findAll() {
        return blockRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public BlockResponseDTO findById(Long id) {
        return mapper.toResponse(findBlockById(id));
    }

    public BlockResponseDTO update(Long id, BlockUpdateRequestDTO request) {
        var block = findBlockById(id);

        block.setDescription(request.description());

        return mapper.toResponse(blockRepository.save(block));
    }

    public BlockResponseDTO inactivate(Long id) {
        var block = findBlockById(id);
        block.setActive(false);

        return mapper.toResponse(blockRepository.save(block));
    }

    private BlockEntity findBlockById(Long id) {
        if (id == null) {
            throw new BusinessException("block.id.required");
        }

        return blockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("block.not.found"));
    }

}
