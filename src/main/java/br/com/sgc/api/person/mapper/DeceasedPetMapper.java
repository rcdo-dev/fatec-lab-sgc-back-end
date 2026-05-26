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
    @Mapping(target = "status", ignore = true)
    DeceasedPetEntity toEntity(DeceasedPetRequestDTO dto);

    @Mapping(target = "declarantId", source = "declarant.id")
    @Mapping(target = "deceasedStatus", ignore = true)
    DeceasedPetResponseDTO toResponse(DeceasedPetEntity entity);
}
