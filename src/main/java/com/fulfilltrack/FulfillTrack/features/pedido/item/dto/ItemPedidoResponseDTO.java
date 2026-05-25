package com.fulfilltrack.FulfillTrack.features.pedido.item.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPedidoResponseDTO {
    private UUID uuid;
    private Integer cantidad;
    private UUID productoUuid;
    private String nombreProducto;
    private String sku;
}
