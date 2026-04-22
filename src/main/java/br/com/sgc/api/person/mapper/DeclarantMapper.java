package br.com.sgc.api.person.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.sgc.api.person.dto.request.DeclarantRequestDTO;
import br.com.sgc.api.person.dto.response.DeclarantResponseDTO;
import br.com.sgc.api.person.entity.DeclarantEntity;

@Mapper(componentModel = "spring")
public interface DeclarantMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deceased", ignore = true)
    DeclarantEntity toEntity(DeclarantRequestDTO dto);

    DeclarantResponseDTO toResponse(DeclarantEntity entity);

}
