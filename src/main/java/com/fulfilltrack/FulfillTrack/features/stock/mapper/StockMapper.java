package com.fulfilltrack.FulfillTrack.features.stock.mapper;

import com.fulfilltrack.FulfillTrack.features.stock.StockEntity;
import com.fulfilltrack.FulfillTrack.features.stock.dto.StockResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockMapper {
    @Mapping(source = "producto.uuid", target = "productoUuid")
    StockResponseDTO toResponseDTO(StockEntity entity);
}