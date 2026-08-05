package br.com.sgc.api.burial.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.sgc.api.burial.dto.request.BurialRequestDTO;
import br.com.sgc.api.burial.dto.response.BurialResponseDTO;
import br.com.sgc.api.burial.entity.BurialEntity;

@Mapper(componentModel = "spring")
public interface BurialMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deceased", ignore = true)
    @Mapping(target = "grave", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "inspections", ignore = true)
    BurialEntity toEntity(BurialRequestDTO dto);

    @Mapping(target = "deceasedId", source = "deceased.id")
    @Mapping(target = "graveId", source = "grave.id")
    BurialResponseDTO toResponse(BurialEntity entity);
}
