package br.com.sgc.api.cemetery.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.sgc.api.cemetery.dto.request.ContractRequestDTO;
import br.com.sgc.api.cemetery.dto.response.ContractResponseDTO;
import br.com.sgc.api.cemetery.entity.ContractEntity;

@Mapper(componentModel = "spring")
public interface ContractMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "holder", ignore = true)
    @Mapping(target = "grave", ignore = true)
    ContractEntity toEntity(ContractRequestDTO dto);

    @Mapping(target = "graveId", source = "grave.id")
    @Mapping(target = "blockId", source = "grave.block.id")
    ContractResponseDTO toResponse(ContractEntity entity);
}
