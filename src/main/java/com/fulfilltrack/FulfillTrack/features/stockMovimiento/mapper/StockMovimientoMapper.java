package com.fulfilltrack.FulfillTrack.features.stockMovimiento.mapper;

import com.fulfilltrack.FulfillTrack.features.stockMovimiento.StockMovimientoEntity;
import com.fulfilltrack.FulfillTrack.features.stockMovimiento.dto.StockMovimientoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StockMovimientoMapper {

    @Mapping(source = "producto.uuid", target = "productoUuid")
    @Mapping(source = "producto.nombreProducto", target = "productoNombre")
    @Mapping(source = "producto.sku", target = "productoSku")
    StockMovimientoResponseDTO toResponseDTO(StockMovimientoEntity entity);

    List<StockMovimientoResponseDTO> toResponseList(List<StockMovimientoEntity> entities);
}