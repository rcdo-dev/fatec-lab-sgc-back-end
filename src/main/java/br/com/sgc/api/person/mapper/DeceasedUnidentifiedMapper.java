package br.com.sgc.api.person.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.sgc.api.person.dto.request.DeceasedUnidentifiedRequestDTO;
import br.com.sgc.api.person.dto.response.DeceasedUnidentifiedResponseDTO;
import br.com.sgc.api.person.entity.DeceasedUnidentifiedEntity;

@Mapper(componentModel = "spring")
public interface DeceasedUnidentifiedMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "declarant", ignore = true)
    @Mapping(target = "status", ignore = true)
    DeceasedUnidentifiedEntity toEntity(DeceasedUnidentifiedRequestDTO dto);

    @Mapping(target = "declarantId", source = "declarant.id")
    @Mapping(target = "deceasedStatus", source = "status")
    DeceasedUnidentifiedResponseDTO toResponse(DeceasedUnidentifiedEntity entity);
}
