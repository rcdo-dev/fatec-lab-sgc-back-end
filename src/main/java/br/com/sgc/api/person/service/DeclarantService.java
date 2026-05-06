package br.com.sgc.api.person.service;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import br.com.sgc.api.common.exception.classes.BusinessException;
import br.com.sgc.api.common.exception.classes.ResourceNotFoundException;
import br.com.sgc.api.person.dto.request.DeclarantRequestDTO;
import br.com.sgc.api.person.dto.response.DeclarantResponseDTO;
import br.com.sgc.api.person.entity.DeclarantEntity;
import br.com.sgc.api.person.entity.embeddable.AddressInfo;
import br.com.sgc.api.person.entity.embeddable.ContatctInfo;
import br.com.sgc.api.person.entity.embeddable.DocumentInfo;
import br.com.sgc.api.person.mapper.DeclarantMapper;
import br.com.sgc.api.person.repositories.DeclarantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeclarantService {
    private final DeclarantRepository declarantRepository;
    private final DeclarantMapper mapper;
    private final MessageSource messageSource;

    public DeclarantResponseDTO save(DeclarantRequestDTO request) {
        var declarant = mapper.toEntity(request);
        var declarantSaved = declarantRepository.save(declarant);
        return mapper.toResponse(declarantSaved);
    }

    public List<DeclarantResponseDTO> findAll() {
        return declarantRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    public DeclarantResponseDTO findById(Long id) {
        return mapper.toResponse(findByDeclarantId(id));
    }

    public DeclarantResponseDTO update(Long id, DeclarantRequestDTO request) {
        var declarant = findByDeclarantId(id);

        var documentInfo = new DocumentInfo(
                                request.document().rg(),
                                request.document().cpf()
                            );
        var contactInfo = new ContatctInfo(
                                request.contact().phone(),
                                request.contact().email()
                            );
        var addressInfo = new AddressInfo(
                                request.address().street(),
                                request.address().number(),
                                request.address().district(),
                                request.address().city(),
                                request.address().state(),
                                request.address().cep()
                            );
        declarant.setName(request.name());
        declarant.setDocument(documentInfo);
        declarant.setContact(contactInfo);
        declarant.setAddress(addressInfo);
        declarant.setOccupation(request.occupation());

        return mapper.toResponse(declarantRepository.save(declarant));
    }

    public void delete (Long id){
        if(!declarantRepository.existsById(id)){
            throw new ResourceNotFoundException("declarant.not.found");
        }
        declarantRepository.deleteById(id);
    }

    private DeclarantEntity findByDeclarantId(Long id) {
        if (id == null)
            throw new BusinessException("declarant.id.required");
        return declarantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("declarant.not.found"));
    }
}
