package com.fulfilltrack.FulfillTrack.features.liquidacion.mapper;

import com.fulfilltrack.FulfillTrack.features.liquidacion.LiquidacionEntity;
import com.fulfilltrack.FulfillTrack.features.liquidacion.dto.LiquidacionRequestDTO;
import com.fulfilltrack.FulfillTrack.features.liquidacion.dto.LiquidacionResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LiquidacionMapper {

    @Mapping(target = "idLiquidacion", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "total", ignore = true)
    @Mapping(target = "estadoPago", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    LiquidacionEntity toEntity(LiquidacionRequestDTO request);

    @Mapping(source = "empresa.uuid", target = "empresaUuid")
    LiquidacionResponseDTO toResponseDTO(LiquidacionEntity entity);

    List<LiquidacionResponseDTO> toResponseList(List<LiquidacionEntity> liquidaciones);
}