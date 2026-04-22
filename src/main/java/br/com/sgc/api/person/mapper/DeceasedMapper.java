package br.com.sgc.api.person.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.sgc.api.person.dto.request.DeceasedRequestDTO;
import br.com.sgc.api.person.dto.response.DeceasedResponseDTO;
import br.com.sgc.api.person.entity.DeceasedEntity;

@Mapper(componentModel = "spring")
public interface DeceasedMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "declarant", ignore = true)
    DeceasedEntity toEntity(DeceasedRequestDTO dto);

    @Mapping(target = "declarantId", source = "declarant.id")
    DeceasedResponseDTO toResponse(DeceasedEntity entity);
}
