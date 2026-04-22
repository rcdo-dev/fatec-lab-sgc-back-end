package br.com.sgc.api.person.service;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import br.com.sgc.api.common.exception.classes.BusinessException;
import br.com.sgc.api.common.exception.classes.ResourceNotFoundException;
import br.com.sgc.api.person.dto.request.DeceasedRequestDTO;
import br.com.sgc.api.person.dto.response.DeceasedResponseDTO;
import br.com.sgc.api.person.entity.DeceasedEntity;
import br.com.sgc.api.person.mapper.DeceasedMapper;
import br.com.sgc.api.person.repositories.DeceasedRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeceasedService {
    private final DeceasedRepository deceasedRepository;
    private final DeceasedMapper mapper;
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
        return mapper.toResponse(findDeceasedByid(id));
    }

    public DeceasedResponseDTO update(Long id, DeceasedRequestDTO request){
        var deceased = findDeceasedByid(id);

        deceased.setName(request.name());

        deceasedRepository.save(deceased);
        
        return mapper.toResponse(deceased);
    }

    private DeceasedEntity findDeceasedByid(Long id) {
        if (id == null) {
            throw new BusinessException("deceased.id.required");
        }
        return deceasedRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("deceased.not.found"));
    }

}
