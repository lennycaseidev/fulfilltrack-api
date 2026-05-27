package com.fulfilltrack.FulfillTrack.features.pedidoMovimiento.dto;

import com.fulfilltrack.FulfillTrack.features.pedido.EstadoPedido;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PedidoMovimientoResponseDTO {
    private UUID uuid;
    private EstadoPedido estadoAnterior;
    private EstadoPedido estadoNuevo;
    private UUID usuarioUuid;
    private LocalDateTime fecha;
}