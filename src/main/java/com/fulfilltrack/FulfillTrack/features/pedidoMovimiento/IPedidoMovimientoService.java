package com.fulfilltrack.FulfillTrack.features.pedidoMovimiento;

import com.fulfilltrack.FulfillTrack.features.pedidoMovimiento.dto.PedidoMovimientoResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IPedidoMovimientoService {
    List<PedidoMovimientoResponseDTO> listarMovimientosPorPedido(UUID pedidoUuid);
}