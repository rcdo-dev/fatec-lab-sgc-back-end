package br.com.sgc.api.cemetery.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.sgc.api.cemetery.dto.request.BlockRequestDTO;
import br.com.sgc.api.cemetery.dto.response.BlockResponseDTO;
import br.com.sgc.api.cemetery.entity.BlockEntity;

@Mapper(componentModel = "spring")
public interface BlockMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cemetery", ignore = true)
    @Mapping(target = "graves", ignore = true)
    BlockEntity toEntity(BlockRequestDTO dto);

    @Mapping(target = "cemeteryId", source = "cemetery.id")
    BlockResponseDTO toReponse(BlockEntity entity);
}
