package br.com.sgc.api.person.service;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import br.com.sgc.api.common.exception.classes.ConflictException;
import br.com.sgc.api.common.exception.classes.ResourceNotFoundException;

import br.com.sgc.api.person.dto.request.DeathRequestDTO;
import br.com.sgc.api.person.dto.response.DeathResponseDTO;
import br.com.sgc.api.person.entity.DeathEntity;
import br.com.sgc.api.person.mapper.DeathMapper;
import br.com.sgc.api.person.repositories.DeathRepository;
import br.com.sgc.api.person.repositories.DeceasedRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeathService {

    // ============================================================================================
    // DEPENDENCIES
    // ============================================================================================

    private final DeathRepository deathRepository;
    private final DeceasedRepository deceasedRepository;
    private final DeathMapper mapper;
    private final MessageSource messageSource;

    // ============================================================================================
    // PUBLIC METHODS
    // ============================================================================================

    public DeathResponseDTO save(DeathRequestDTO request) {
        var death = mapper.toEntity(request);

        validateExistingDeath(request);

        // Polimorfismo aplicado aqui.
        var deceased = deceasedRepository.findById(request.deceasedId())
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("deceased.not.found")));
        death.setDeceased(deceased);

        return mapper.toResponse(deathRepository.save(death));

    }

    public List<DeathResponseDTO> findAll() {
        return deathRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    public DeathResponseDTO findById(Long Id) {
        return mapper.toResponse(findByDeathId(Id));
    }

    public DeathResponseDTO update(Long id, DeathRequestDTO request) {
        var death = findByDeathId(id);

        validateExistingDeath(request);

        updateData(death, request);

        return mapper.toResponse(deathRepository.save(death));
    }

    public void delete(Long id) {
        deathRepository.delete(findByDeathId(id));
    }

    // ============================================================================================
    // VALIDATIONS
    // ============================================================================================

    private void validateExistingDeath(DeathRequestDTO request) {
        if (deathRepository.existsByDeceasedId(request.deceasedId())) {
            throw new ConflictException(getMessage("death.already.exists"));
        }
    }

    // ============================================================================================
    // UPDATE HELPERS
    // ============================================================================================

    private void updateData(DeathEntity death, DeathRequestDTO request) {
        death.setPlace(request.place());
        death.setDate(request.date());
        death.setTime(request.time());
        death.setCauseDeath(request.causeDeath());
        death.setDoctorResponsible(request.doctorResponsible());
        death.setCertificateNumber(request.certificateNumber());
        death.setObservations(request.observations());
    }

    // ============================================================================================
    // FIND METHODS
    // ============================================================================================

    private DeathEntity findByDeathId(Long id) {
        return deathRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("death.not.found")));
    }

    // ============================================================================================
    // MESSAGE METHODS
    // ============================================================================================

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, "Messagem não encontrada " + key, LocaleContextHolder.getLocale());
    }

}
