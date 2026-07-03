package br.com.sgc.api.cemetery.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.sgc.api.cemetery.dto.request.GraveRequestDTO;
import br.com.sgc.api.cemetery.dto.response.GraveResponseDTO;
import br.com.sgc.api.cemetery.entity.GraveEntity;

@Mapper(componentModel = "spring")
public interface GraveMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "block", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "blocked", ignore = true)
    @Mapping(target = "reason", ignore = true)
    @Mapping(target = "burials", ignore = true)
    @Mapping(target = "contracts", ignore = true)
    GraveEntity toEntity(GraveRequestDTO dto);

    @Mapping(target = "blockId", source = "block.id")
    GraveResponseDTO toResponse(GraveEntity entity);
}
