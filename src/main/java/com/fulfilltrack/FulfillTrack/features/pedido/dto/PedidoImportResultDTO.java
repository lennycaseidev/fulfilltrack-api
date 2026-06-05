package com.fulfilltrack.FulfillTrack.features.pedido.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoImportResultDTO {
    private int pedidosCreados;
    private int pedidosConError;
    private List<PedidoResponseDTO> creados;
    private List<PedidoImportErrorDTO> errores;
}