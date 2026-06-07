package br.com.sgc.api.burial.service;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sgc.api.burial.dto.request.BurialRequestDTO;
import br.com.sgc.api.burial.dto.response.BurialResponseDTO;
import br.com.sgc.api.burial.entity.BurialEntity;
import br.com.sgc.api.burial.mapper.BurialMapper;
import br.com.sgc.api.burial.repositories.BurialRepository;
import br.com.sgc.api.cemetery.entity.GraveEntity;
import br.com.sgc.api.cemetery.repositories.GraveRepository;
import br.com.sgc.api.common.enums.BurialStatus;
import br.com.sgc.api.common.enums.DeceasedStatus;
import br.com.sgc.api.common.enums.GraveStatus;
import br.com.sgc.api.common.exception.classes.BusinessException;
import br.com.sgc.api.common.exception.classes.ConflictException;
import br.com.sgc.api.common.exception.classes.ResourceNotFoundException;
import br.com.sgc.api.person.entity.DeceasedEntity;
import br.com.sgc.api.person.repositories.DeceasedRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BurialService {

    // ============================================================================================
    // DEPENDENCIES
    // ============================================================================================

    private final BurialRepository burialRepository;
    private final DeceasedRepository deceasedRepository;
    private final GraveRepository graveRepository;
    private final BurialMapper mapper;
    private final MessageSource messageSource;

    // ============================================================================================
    // PUBLIC METHODS
    // ============================================================================================

    @Transactional
    public BurialResponseDTO save(BurialRequestDTO request) {
        var deceased = findDeceasedById(request.deceasedId());
        var grave = findGraveById(request.graveId());

        validateDeceasedCanBeBuried(deceased);
        validateDeceasedWithoutActiveBurial(deceased.getId());
        validateGraveCanReceiveBurial(grave);
        validateGraveWithoutActiveBurial(grave.getId());
        validateGraveCapacity(grave);

        var burial = mapper.toEntity(request);
        burial.setDeceased(deceased);
        burial.setGrave(grave);
        burial.setStatus(BurialStatus.IN_PROGRESS);

        updateDeceasedStatusToBuried(deceased);
        updateGraveStatusToOccupied(grave);

        return mapper.toResponse(burialRepository.save(burial));
    }

    public List<BurialResponseDTO> findAll() {
        return burialRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    public BurialResponseDTO findById(Long id) {
        return mapper.toResponse(findBurialById(id));
    }

    @Transactional
    public BurialResponseDTO update(Long id, BurialRequestDTO request) {
        var burial = findBurialById(id);
        var currentDeceased = burial.getDeceased();
        var currentGrave = burial.getGrave();

        if (burial.getStatus() != BurialStatus.IN_PROGRESS) {
            throw new BusinessException(getMessage("burial.invalid.status"));
        }

        var deceased = findDeceasedById(request.deceasedId());
        var grave = findGraveById(request.graveId());

        validateDeceasedCanBeUpdatedForBurial(burial, deceased);
        validateGraveCanBeUpdatedForBurial(burial, grave);

        updateData(burial, request);
        burial.setDeceased(deceased);
        burial.setGrave(grave);

        updateDeceasedStatusToBuried(deceased);
        updateGraveStatusToOccupied(grave);
        releaseOldGraveIfNecessary(currentGrave.getId(), grave.getId(), burial.getId());
        releaseOldDeceasedIfNecessary(currentDeceased.getId(), deceased.getId());

        return mapper.toResponse(burialRepository.save(burial));
    }

    @Transactional
    public BurialResponseDTO cancel(Long id) {
        var burial = findBurialById(id);

        if (burial.getStatus() != BurialStatus.IN_PROGRESS) {
            throw new BusinessException(getMessage("burial.invalid.status"));
        }

        burial.setStatus(BurialStatus.CANCELLED);
        burialRepository.save(burial);

        updateDeceasedStatusToActive(burial.getDeceased());
        releaseGraveIfThereIsNoActiveBurial(burial.getGrave().getId(), burial.getId());

        return mapper.toResponse(burial);
    }

    // ============================================================================================
    // VALIDATIONS
    // ============================================================================================

    private void validateDeceasedCanBeBuried(DeceasedEntity deceased) {
        if (deceased.isArchived() || deceased.getStatus() == DeceasedStatus.ARCHIVED) {
            throw new BusinessException(getMessage("burial.invalid.status"));
        }

        if (deceased.getStatus() == DeceasedStatus.BURIED) {
            throw new ConflictException(getMessage("burial.deceased.already.buried"));
        }
    }

    private void validateDeceasedWithoutActiveBurial(Long deceasedId) {
        if (burialRepository.existsByDeceasedIdAndStatus(deceasedId, BurialStatus.IN_PROGRESS)) {
            throw new ConflictException(getMessage("burial.already.exists.for.deceased"));
        }
    }

    private void validateGraveCanReceiveBurial(GraveEntity grave) {
        if (!grave.isActive()) {
            throw new BusinessException(getMessage("burial.grave.inactive"));
        }

        if (grave.getStatus() == GraveStatus.MAINTENANCE) {
            throw new BusinessException(getMessage("burial.grave.in.maintenance"));
        }

        if (grave.getStatus() != GraveStatus.AVAILABLE) {
            throw new BusinessException(getMessage("burial.grave.not.available"));
        }
    }

    private void validateGraveWithoutActiveBurial(Long graveId) {
        if (burialRepository.existsByGraveIdAndStatus(graveId, BurialStatus.IN_PROGRESS)) {
            throw new ConflictException(getMessage("burial.already.exists.for.grave"));
        }
    }

    private void validateGraveCapacity(GraveEntity grave) {
        if (grave.getBodyCapacity() <= 0) {
            throw new BusinessException(getMessage("burial.grave.not.available"));
        }
    }

    private void validateDeceasedCanBeUpdatedForBurial(BurialEntity burial, DeceasedEntity deceased) {
        if (!burial.getDeceased().getId().equals(deceased.getId())) {
            validateDeceasedCanBeBuried(deceased);
            validateDeceasedWithoutActiveBurial(deceased.getId());
            return;
        }

        if (deceased.isArchived() || deceased.getStatus() == DeceasedStatus.ARCHIVED) {
            throw new BusinessException(getMessage("burial.invalid.status"));
        }
    }

    private void validateGraveCanBeUpdatedForBurial(BurialEntity burial, GraveEntity grave) {
        if (!burial.getGrave().getId().equals(grave.getId())) {
            validateGraveCanReceiveBurial(grave);
            validateGraveCapacity(grave);
            validateGraveWithoutActiveBurial(grave.getId());
            return;
        }

        if (!grave.isActive()) {
            throw new BusinessException(getMessage("burial.grave.inactive"));
        }

        if (grave.getStatus() == GraveStatus.MAINTENANCE) {
            throw new BusinessException(getMessage("burial.grave.in.maintenance"));
        }
    }

    // ============================================================================================
    // UPDATE HELPERS
    // ============================================================================================

    private void updateData(BurialEntity burial, BurialRequestDTO request) {
        burial.setDate(request.date());
        burial.setTime(request.time());
        burial.setObservations(request.observations());
    }

    private void updateGraveStatusToOccupied(GraveEntity grave) {
        grave.setStatus(GraveStatus.OCCUPIED);
        graveRepository.save(grave);
    }

    private void updateDeceasedStatusToBuried(DeceasedEntity deceased) {
        deceased.setStatus(DeceasedStatus.BURIED);
        deceasedRepository.save(deceased);
    }

    private void updateDeceasedStatusToActive(DeceasedEntity deceased) {
        deceased.setStatus(DeceasedStatus.ACTIVE);
        deceasedRepository.save(deceased);
    }

    private void releaseGraveIfThereIsNoActiveBurial(Long graveId, Long currentBurialId) {
        if (burialRepository.countByGraveIdAndStatusAndIdNot(graveId, BurialStatus.IN_PROGRESS, currentBurialId) == 0L) {
            var grave = findGraveById(graveId);
            grave.setStatus(GraveStatus.AVAILABLE);
            graveRepository.save(grave);
        }
    }

    private void releaseOldGraveIfNecessary(Long currentGraveId, Long newGraveId, Long burialId) {
        if (!currentGraveId.equals(newGraveId)) {
            releaseGraveIfThereIsNoActiveBurial(currentGraveId, burialId);
        }
    }

    private void releaseOldDeceasedIfNecessary(Long currentDeceasedId, Long newDeceasedId) {
        if (!currentDeceasedId.equals(newDeceasedId)
                && !burialRepository.existsByDeceasedIdAndStatus(currentDeceasedId, BurialStatus.IN_PROGRESS)) {
            var oldDeceased = findDeceasedById(currentDeceasedId);
            oldDeceased.setStatus(DeceasedStatus.ACTIVE);
            deceasedRepository.save(oldDeceased);
        }
    }

    // ============================================================================================
    // FIND METHODS
    // ============================================================================================

    private BurialEntity findBurialById(Long id) {
        return burialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("burial.not.found")));
    }

    private DeceasedEntity findDeceasedById(Long deceasedId) {
        return deceasedRepository.findById(deceasedId)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("burial.deceased.not.found")));
    }

    private GraveEntity findGraveById(Long graveId) {
        return graveRepository.findById(graveId)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("burial.grave.not.found")));
    }

    // ============================================================================================
    // MESSAGE METHODS
    // ============================================================================================

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, "Messagem não encontrada " + key, LocaleContextHolder.getLocale());
    }
}
