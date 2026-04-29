package br.com.sgc.api.person.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.sgc.api.person.dto.request.DeceasedRequestDTO;
import br.com.sgc.api.person.dto.response.DeceasedResponseDTO;
import br.com.sgc.api.person.entity.DeceasedIdentifiedEntity;

@Mapper(componentModel = "spring")
public interface DeceasedIdentifiedMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "declarant", ignore = true)
    DeceasedIdentifiedEntity toEntity(DeceasedRequestDTO dto);

    @Mapping(target = "declarantId", source = "declarant.id")
    DeceasedResponseDTO toResponse(DeceasedIdentifiedEntity entity);
}
