package br.com.sgc.api.burial.service;

import java.time.LocalDate;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import br.com.sgc.api.burial.dto.request.ExhumationInspectionRequestDTO;
import br.com.sgc.api.burial.dto.response.ExhumationInspectionResponseDTO;
import br.com.sgc.api.burial.mapper.ExhumationInspectionMapper;
import br.com.sgc.api.burial.repositories.BurialRepository;
import br.com.sgc.api.burial.repositories.ExhumationInspectionRespository;
import br.com.sgc.api.common.exception.classes.BusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExhumationInspectionService {

    // ============================================================================================
    // DEPENDENCIES
    // ============================================================================================

    private final BurialRepository burialRepository;
    private final ExhumationInspectionRespository exhumationInspectionRespository;
    private final ExhumationInspectionMapper mapper;

    private final MessageSource messageSource;

    // ============================================================================================
    // PUBLIC METHODS
    // ============================================================================================

    public ExhumationInspectionResponseDTO save(ExhumationInspectionRequestDTO request) {
        
        var exhumationInpection = mapper.toEntity(request);
        
        validateMinimumTimeframe(exhumationInpection.getBurial().getDate(), exhumationInpection.getInspectedAt());

        return mapper.toResponse(exhumationInspectionRespository.save(exhumationInpection));
    }

    // ============================================================================================
    // VALIDATIONS
    // ============================================================================================

    private void validateMinimumTimeframe(LocalDate burialDate, LocalDate inspectionDate) {
        if (burialDate.plusYears(3).isAfter(inspectionDate)) {
            throw new BusinessException(getMessage("exhumation.inspection.date.prior.to.burial.date"));
        }
    }

    // ============================================================================================
    // UPDATE HELPERS
    // ============================================================================================

    // ============================================================================================
    // FIND METHODS
    // ============================================================================================

    // ============================================================================================
    // MESSAGE METHODS
    // ============================================================================================

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, "Messagem não encontrada " + key, LocaleContextHolder.getLocale());
    }

}
