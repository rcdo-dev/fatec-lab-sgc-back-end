package br.com.sgc.api.person.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.sgc.api.person.dto.request.DeathRequestDTO;
import br.com.sgc.api.person.dto.response.DeathResponseDTO;
import br.com.sgc.api.person.entity.DeathEntity;

@Mapper(componentModel = "spring")
public interface DeathMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deceased", ignore = true)
    DeathEntity toEntity(DeathRequestDTO dto);

    @Mapping(target = "deceasedId", source = "deceased.id")
    DeathResponseDTO toResponse(DeathEntity entity);

}
