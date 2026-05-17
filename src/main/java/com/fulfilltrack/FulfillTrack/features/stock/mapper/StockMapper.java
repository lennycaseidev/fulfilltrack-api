package com.fulfilltrack.FulfillTrack.features.stock.mapper;

import com.fulfilltrack.FulfillTrack.features.stock.StockEntity;
import com.fulfilltrack.FulfillTrack.features.stock.dto.StockResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StockMapper {
    @Mapping(source = "producto.uuid", target = "productoUuid")
    @Mapping(source = "producto.nombreProducto", target = "nombreProducto")
    @Mapping(source = "producto.sku", target = "sku")
    StockResponseDTO toResponseDTO(StockEntity entity);

    List<StockResponseDTO> toResponseList(List<StockEntity> stocks);
}