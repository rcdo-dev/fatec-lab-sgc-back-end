package br.com.sgc.api.burial.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.sgc.api.burial.dto.request.ExhumationInspectionRequestDTO;
import br.com.sgc.api.burial.dto.response.ExhumationInspectionResponseDTO;
import br.com.sgc.api.burial.entity.ExhumationInspectionEntity;

@Mapper(componentModel = "spring")
public interface ExhumationInspectionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "burial", ignore = true)
    ExhumationInspectionEntity toEntity(ExhumationInspectionRequestDTO dto);

    @Mapping(target = "burialId", source = "burial.id")
    ExhumationInspectionResponseDTO toResponse(ExhumationInspectionEntity entity);

}
