package br.com.sgc.api.cemetery.service;

import java.util.List;
import java.util.Objects;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import br.com.sgc.api.cemetery.dto.request.GraveRequestDTO;
import br.com.sgc.api.cemetery.dto.request.GraveUpdateRequestDTO;
import br.com.sgc.api.cemetery.dto.response.GraveResponseDTO;
import br.com.sgc.api.cemetery.entity.GraveEntity;
import br.com.sgc.api.cemetery.mapper.GraveMapper;
import br.com.sgc.api.cemetery.repositories.BlockRepository;
import br.com.sgc.api.cemetery.repositories.GraveRepository;
import br.com.sgc.api.common.enums.AreaType;
import br.com.sgc.api.common.enums.GraveStatus;
import br.com.sgc.api.common.enums.GraveType;
import br.com.sgc.api.common.exception.classes.BusinessException;
import br.com.sgc.api.common.exception.classes.ConflictException;
import br.com.sgc.api.common.exception.classes.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GraveService {
    private final GraveRepository graveRepository;
    private final BlockRepository blockRepository;
    private final GraveMapper mapper;
    private final MessageSource messageSource;

    // ------------------ CRUD

    public GraveResponseDTO save(GraveRequestDTO request) {

        validateAreaTypeAndGraveType(request.areaType(), request.graveType());
        validateBodyCapacity(request.graveType(), request.bodyCapacity());

        if (graveRepository.existsByNumberAndBlockId(request.number(), request.blockId())) {
            throw new ConflictException(getMessage("grave.number.already.exists"));
        }

        var blockEntity = blockRepository
                .findById(Objects.requireNonNull(request.blockId(), getMessage("block.id.required")))
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("block.not.found")));

        var graveEntity = mapper.toEntity(request);
        graveEntity.setActive(true);
        graveEntity.setStatus(GraveStatus.AVAILABLE);
        graveEntity.setBlock(blockEntity);

        return mapper.toResponse(graveRepository.save(graveEntity));
    }

    public List<GraveResponseDTO> findAll() {
        return graveRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public GraveResponseDTO findById(Long id) {
        return mapper.toResponse(findGraveById(id));
    }

    public GraveResponseDTO update(Long id, GraveUpdateRequestDTO request) {
        var graveEntity = findGraveById(id);

        validateAreaTypeAndGraveType(request.areaType(), request.graveType());
        validateBodyCapacity(request.graveType(), request.bodyCapacity());

        graveEntity.setGraveType(request.graveType());
        graveEntity.setBodyCapacity(request.bodyCapacity());
        graveEntity.setAreaType(request.areaType());

        return mapper.toResponse(graveRepository.save(graveEntity));
    }

    public GraveResponseDTO inactivate(Long id) {
        var graveEntity = findGraveById(id);
        graveEntity.setActive(false);

        return mapper.toResponse(graveRepository.save(graveEntity));
    }

    // ------------------ Find

    private GraveEntity findGraveById(Long id) {
        if (id == null) {
            throw new BusinessException(getMessage("grave.id.required"));
        }

        return graveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("grave.not.found")));
    }

    private GraveEntity findActiveGraveById(Long id) {
        var graveEntity = findGraveById(id);

        if (!graveEntity.isActive()) {
            throw new BusinessException(getMessage("grave.inactive"));
        }

        return graveEntity;
    }

    // ------------------ Transitions

    // --- Status

    public void occupy(Long graveId) {
        var graveEntity = findActiveGraveById(graveId);

        if (graveEntity.getStatus() != GraveStatus.AVAILABLE) {
            throw new BusinessException(getMessage("grave.unavailable"));
        }

        graveEntity.setStatus(GraveStatus.OCCUPIED);
        graveRepository.save(graveEntity);
    }

    public void release(Long graveId) {
        var graveEntity = findActiveGraveById(graveId);

        if (graveEntity.getStatus() != GraveStatus.OCCUPIED) {
            throw new BusinessException(getMessage("grave.available"));
        }

        graveEntity.setStatus(GraveStatus.AVAILABLE);
        graveRepository.save(graveEntity);
    }

    public void sendToMaintenance(Long graveId) {
        var graveEntity = findActiveGraveById(graveId);

        if (graveEntity.getStatus() != GraveStatus.AVAILABLE) {
            throw new BusinessException(getMessage("grave.unavailable"));
        }

        graveEntity.setStatus(GraveStatus.MAINTENANCE);
        graveRepository.save(graveEntity);
    }

    public void finishMaintenance(Long graveId) {
        var graveEntity = findActiveGraveById(graveId);

        if (graveEntity.getStatus() != GraveStatus.MAINTENANCE) {
            throw new BusinessException(getMessage("grave.available"));
        }

        graveEntity.setStatus(GraveStatus.AVAILABLE);
        graveRepository.save(graveEntity);
    }

    // --- blockage

    public void markAsBlocked(Long graveId, String reason) {
        var graveEntity = findActiveGraveById(graveId);

        graveEntity.setBlocked(true);
        graveEntity.setReason(reason);

        graveRepository.save(graveEntity);
    }

    public void markAsUnblocked(Long graveId, String reason) {
        var graveEntity = findActiveGraveById(graveId);

        graveEntity.setBlocked(false);
        graveEntity.setReason(reason);

        graveRepository.save(graveEntity);
    }

    // --- Activation

    public void activate(Long graveId) {
        var graveEntity = findGraveById(graveId);

        if (graveEntity.isActive()) {
            throw new BusinessException(getMessage("grave.active"));
        }

        graveEntity.setActive(true);
        graveRepository.save(graveEntity);
    }

    // ------------------ Validations

    private void validateAreaTypeAndGraveType(AreaType areaType, GraveType graveType) {
        if (areaType == AreaType.COMMON && graveType != GraveType.EARTH) {
            throw new BusinessException(getMessage("grave.common.area.earth.type"));
        }

        if (areaType == AreaType.PERPETUAL && graveType != GraveType.EARTH && graveType != GraveType.MAUSOLEUM) {
            throw new BusinessException(getMessage("grave.perpetual.earth.mausoleum.type"));
        }
    }

    private void validateBodyCapacity(GraveType graveType, int bodyCapacity) {
        if (graveType == GraveType.EARTH && bodyCapacity > 2) {
            throw new BusinessException(getMessage("grave.earth.type.capacity"));
        }

        if (graveType == GraveType.MAUSOLEUM && bodyCapacity > 4) {
            throw new BusinessException(getMessage("grave.mausoleum.type.capacity"));
        }
    }

    private String getMessage(String key) {
        return messageSource.getMessage(Objects.requireNonNull(key), null, "Messagem nao encontrada: " + key,
                LocaleContextHolder.getLocale());
    }

}
