package br.com.sgc.api.person.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.sgc.api.person.dto.request.DeceasedIdentifiedRequestDTO;
import br.com.sgc.api.person.dto.response.DeceasedIdentifiedResponseDTO;
import br.com.sgc.api.person.entity.DeceasedIdentifiedEntity;

@Mapper(componentModel = "spring")
public interface DeceasedIdentifiedMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "declarant", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "burials", ignore = true)
    DeceasedIdentifiedEntity toEntity(DeceasedIdentifiedRequestDTO dto);

    @Mapping(target = "declarantId", source = "declarant.id")
    @Mapping(target = "deceasedStatus", source = "status")
    DeceasedIdentifiedResponseDTO toResponse(DeceasedIdentifiedEntity entity);
}
