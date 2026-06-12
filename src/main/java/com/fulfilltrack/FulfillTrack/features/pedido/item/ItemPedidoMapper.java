package com.fulfilltrack.FulfillTrack.features.pedido.item;

import com.fulfilltrack.FulfillTrack.features.pedido.item.dto.ItemPedidoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemPedidoMapper {
    @Mapping(source = "producto.uuid", target = "productoUuid")
    @Mapping(source = "producto.nombreProducto", target = "nombreProducto")
    @Mapping(source = "producto.sku", target = "sku")
    ItemPedidoResponseDTO toResponseDTO(ItemPedidoEntity entity);

    List<ItemPedidoResponseDTO> toResponseList(List<ItemPedidoEntity> entities);
}
