package br.com.sgc.api.person.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import br.com.sgc.api.person.dto.request.DeclarantRequestDTO;
import br.com.sgc.api.person.dto.response.DeclarantResponseDTO;
import br.com.sgc.api.person.mapper.DeclarantMapper;
import br.com.sgc.api.person.repositories.DeclarantRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeclarantService {
    private final DeclarantRepository declarantRepository;
    private final DeclarantMapper mappper;
    private final MessageSource messageSource;

    public DeclarantResponseDTO save(DeclarantRequestDTO request) {
        var declarant = mappper.toEntity(request);
        var declarantSaved = declarantRepository.save(declarant);
        return mappper.toResponse(declarantSaved);
    }
}
