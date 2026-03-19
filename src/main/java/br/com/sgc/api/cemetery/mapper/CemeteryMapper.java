package br.com.sgc.api.cemetery.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.sgc.api.cemetery.dto.request.CemeteryRequestDTO;
import br.com.sgc.api.cemetery.dto.response.CemeteryResponseDTO;
import br.com.sgc.api.cemetery.entity.CemeteryEntity;

/**
 * componentModel = "spring" -> Transforma o mapper em um Bean do Spring,
 * funciona automaticamente com @Autowired / @RequiredArgsConstructor
 */
@Mapper(componentModel = "spring")
public interface CemeteryMapper {

    @Mapping(target = "id", ignore = true) // Ignora o mapeamento para id.
    @Mapping(target = "blocks", ignore = true) // Ignora o mapeamento para blocks.
    CemeteryEntity toEntity(CemeteryRequestDTO dto);

    CemeteryResponseDTO toResponse(CemeteryEntity entity);

}