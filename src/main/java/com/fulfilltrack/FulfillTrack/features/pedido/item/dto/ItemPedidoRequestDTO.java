package com.fulfilltrack.FulfillTrack.features.pedido.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoRequestDTO {
    private Integer cantidad;
    private UUID pedidoUuid;
    private UUID productoUuid;
}
