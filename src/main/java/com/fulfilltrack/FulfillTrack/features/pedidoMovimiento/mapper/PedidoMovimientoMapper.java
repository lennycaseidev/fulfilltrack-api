package com.fulfilltrack.FulfillTrack.features.pedidoMovimiento.mapper;

import com.fulfilltrack.FulfillTrack.features.pedidoMovimiento.PedidoMovimientoEntity;
import com.fulfilltrack.FulfillTrack.features.pedidoMovimiento.dto.PedidoMovimientoResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PedidoMovimientoMapper {
    PedidoMovimientoResponseDTO toResponseDTO(PedidoMovimientoEntity entity);
    List<PedidoMovimientoResponseDTO> toResponseList(List<PedidoMovimientoEntity> entities);
}