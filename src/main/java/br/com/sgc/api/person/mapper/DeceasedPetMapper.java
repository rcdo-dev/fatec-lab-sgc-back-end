package br.com.sgc.api.person.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.sgc.api.person.dto.request.DeceasedPetRequestDTO;
import br.com.sgc.api.person.dto.response.DeceasedPetResponseDTO;
import br.com.sgc.api.person.entity.DeceasedPetEntity;

@Mapper(componentModel = "spring")
public interface DeceasedPetMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "declarant", ignore = true)
    DeceasedPetEntity toEntity(DeceasedPetRequestDTO dto);

    @Mapping(target = "declarantId", source = "declarant.id")
    DeceasedPetResponseDTO toResponse(DeceasedPetEntity entity);
}
