package com.fulfilltrack.FulfillTrack.features.pedido.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoImportErrorDTO {
    private String numeroOrden;
    private String motivo;
}