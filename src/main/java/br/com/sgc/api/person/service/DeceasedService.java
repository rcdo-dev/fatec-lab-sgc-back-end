package br.com.sgc.api.person.service;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sgc.api.common.exception.classes.BusinessException;
import br.com.sgc.api.common.exception.classes.ResourceNotFoundException;
import br.com.sgc.api.person.dto.request.DeceasedRequestDTO;
import br.com.sgc.api.person.dto.response.DeceasedResponseDTO;
import br.com.sgc.api.person.entity.DeceasedIdentifiedEntity;
import br.com.sgc.api.person.entity.embeddable.DocumentInfo;
import br.com.sgc.api.person.mapper.DeceasedIdentifiedMapper;
import br.com.sgc.api.person.repositories.DeceasedRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeceasedService {
    private final DeceasedRepository deceasedRepository;
    private final DeceasedIdentifiedMapper mapper;
    private final MessageSource messageSource;

    public DeceasedResponseDTO save(DeceasedRequestDTO request) {
        var deceased = mapper.toEntity(request);
        var deceasedSaved = deceasedRepository.save(deceased);
        return mapper.toResponse(deceasedSaved);
    }

    public List<DeceasedResponseDTO> findAll() {
        return deceasedRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    public DeceasedResponseDTO findById(Long id) {
        return mapper.toResponse(findDeceasedById(id));
    }

    @Transactional
    public DeceasedResponseDTO update(Long id, DeceasedRequestDTO request) {
        var deceased = findDeceasedById(id);
        var documentInfo = new DocumentInfo(request.document().rg(), request.document().cpf());

        deceased.setName(request.name());
        deceased.setBirthDate(request.birthDate());
        deceased.setGender(request.gender());
        deceased.setDocument(documentInfo);
        deceased.setOccupation(request.occupation());
        deceased.setFathersName(request.fathersName());
        deceased.setMothersName(request.mothersName());
        deceased.setNaturalness(request.naturalness());
        deceased.setCityResident(request.cityResident());
        deceased.setObservations(request.observations());

        return mapper.toResponse(deceased);
    }

    public void archive(Long id) {
        // Não há delete para falecido, pode ser arquivado.
    }

    public void history(Long id) {
        // Não há delete para falecido, pode ter histórico.
    }

    private DeceasedIdentifiedEntity findDeceasedById(Long id) {
        if (id == null) {
            throw new BusinessException("deceased.id.required");
        }
        return deceasedRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("deceased.not.found"));
    }

}
