package com.fulfilltrack.FulfillTrack.features.pedido.mapper;

import com.fulfilltrack.FulfillTrack.features.pedido.PedidoEntity;
import com.fulfilltrack.FulfillTrack.features.pedido.dto.PedidoResponseDTO;
import com.fulfilltrack.FulfillTrack.features.pedido.item.ItemPedidoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ItemPedidoMapper.class})
public interface PedidoMapper {
    @Mapping(source = "empresa.nombreEmpresa", target = "nombreEmpresa")
    PedidoResponseDTO toResponseDTO(PedidoEntity entity);

    List<PedidoResponseDTO> toResponseList(List<PedidoEntity> entities);
}
