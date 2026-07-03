package br.com.sgc.api.burial.service;

import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sgc.api.burial.dto.request.WakeRequestDTO;
import br.com.sgc.api.burial.dto.response.WakeResponseDTO;
import br.com.sgc.api.burial.entity.WakeEntity;
import br.com.sgc.api.burial.mapper.WakeMapper;
import br.com.sgc.api.burial.repositories.WakeRepository;
import br.com.sgc.api.cemetery.entity.CemeteryEntity;
import br.com.sgc.api.cemetery.entity.WakeConfigurationEntity;
import br.com.sgc.api.cemetery.repositories.CemeteryRepository;
import br.com.sgc.api.cemetery.service.WakeConfigurationService;
import br.com.sgc.api.common.enums.WakeStatus;
import br.com.sgc.api.common.exception.classes.BusinessException;
import br.com.sgc.api.common.exception.classes.ConflictException;
import br.com.sgc.api.common.exception.classes.ResourceNotFoundException;
import br.com.sgc.api.person.entity.DeceasedEntity;
import br.com.sgc.api.person.repositories.DeceasedRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WakeService {

    // ============================================================================================
    // DEPENDENCIES
    // ============================================================================================

    private final WakeRepository wakeRepository;
    private final DeceasedRepository deceasedRepository;
    private final CemeteryRepository cemeteryRepository;
    private final WakeConfigurationService wakeConfigurationService;

    private final MessageSource messageSource;
    private final WakeMapper mapper;

    // ============================================================================================
    // PUBLIC METHODS
    // ============================================================================================

    @Transactional
    public WakeResponseDTO save(WakeRequestDTO request) {
        var deceased = findDeceasedById(request.deceasedId());
        var cemetery = resolveCemetery(request.cemeteryId());
        var configuration = wakeConfigurationService.findOrCreateForCemetery(cemetery);

        validatePeriod(request);
        validateDuration(request, configuration);
        validateScheduleAvailability(request, cemetery.getId());

        var wake = mapper.toEntity(request);
        wake.setDeceased(deceased);
        wake.setCemetery(cemetery);
        wake.setStatus(WakeStatus.SCHEDULED);
        wake.setAppliedFee(wakeConfigurationService.resolveAppliedFee(configuration));

        return mapper.toResponse(
                wakeRepository.save(wake),
                getMessage("wake.registered.success")
            );
    }

    public List<WakeResponseDTO> findAll() {
        return wakeRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    public WakeResponseDTO findById(Long id) {
        return mapper.toResponse(findWakeById(id));
    }

    @Transactional
    public WakeResponseDTO cancel(Long id) {
        var wake = findWakeById(id);

        if (wake.getStatus() != WakeStatus.SCHEDULED) {
            throw new BusinessException(getMessage("wake.invalid.status"));
        }

        wake.setStatus(WakeStatus.CANCELLED);

        return mapper.toResponse(wakeRepository.save(wake));
    }

    // ============================================================================================
    // VALIDATIONS
    // ============================================================================================

    private void validatePeriod(WakeRequestDTO request) {
        if (!request.endTime().isAfter(request.startTime())) {
            throw new BusinessException(getMessage("wake.invalid.period"));
        }
    }

    private void validateDuration(WakeRequestDTO request, WakeConfigurationEntity configuration) {
        var durationMinutes = ChronoUnit.MINUTES.between(request.startTime(), request.endTime());

        if (durationMinutes > configuration.getDurationMinutes()) {
            throw new BusinessException(getMessage("wake.duration.exceeded"));
        }
    }

    private void validateScheduleAvailability(WakeRequestDTO request, Long cemeteryId) {
        var hasConflict = wakeRepository.existsScheduleConflict(
                cemeteryId,
                request.date(),
                request.startTime(),
                request.endTime(),
                WakeStatus.SCHEDULED);

        if (hasConflict) {
            throw new ConflictException(getMessage("wake.schedule.conflict"));
        }
    }

    // ============================================================================================
    // RESOLUTION METHODS
    // ============================================================================================

    private CemeteryEntity resolveCemetery(Long cemeteryId) {
        if (cemeteryId != null) {
            return cemeteryRepository.findById(cemeteryId)
            .orElseThrow(() -> new ResourceNotFoundException(getMessage("wake.cemetery.not.found")));
        }
        
        if (cemeteryRepository.countByActiveTrue() != 1L) {
            throw new BusinessException(getMessage("wake.cemetery.required"));
        }
        
        return cemeteryRepository.findFirstByActiveTrue()
        .orElseThrow(() -> new BusinessException(getMessage("wake.cemetery.required")));
    }
    
    // ============================================================================================
    // FIND METHODS
    // ============================================================================================
    
    private WakeEntity findWakeById(Long id) {
        return wakeRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(getMessage("wake.not.found")));
    }
    
    private DeceasedEntity findDeceasedById(Long deceasedId) {
        return deceasedRepository.findById(deceasedId)
        .orElseThrow(() -> new ResourceNotFoundException(getMessage("wake.deceased.not.found")));
    }
    
    // ============================================================================================
    // MESSAGE METHODS
    // ============================================================================================

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, "Messagem não encontrada " + key, LocaleContextHolder.getLocale());
    }
}
