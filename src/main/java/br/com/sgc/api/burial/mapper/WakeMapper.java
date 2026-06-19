package br.com.sgc.api.burial.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.sgc.api.burial.dto.request.WakeRequestDTO;
import br.com.sgc.api.burial.dto.response.WakeResponseDTO;
import br.com.sgc.api.burial.entity.WakeEntity;

@Mapper(componentModel = "spring")
public interface WakeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appliedFee", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "deceased", ignore = true)
    @Mapping(target = "cemetery", ignore = true)
    WakeEntity toEntity(WakeRequestDTO dto);

    @Mapping(target = "deceasedId", source = "deceased.id")
    @Mapping(target = "cemeteryId", source = "cemetery.id")
    @Mapping(target = "message", ignore = true)
    WakeResponseDTO toResponse(WakeEntity entity);

    @Mapping(target = "deceasedId", source = "entity.deceased.id")
    @Mapping(target = "cemeteryId", source = "entity.cemetery.id")
    WakeResponseDTO toResponse(WakeEntity entity, String message);
}
