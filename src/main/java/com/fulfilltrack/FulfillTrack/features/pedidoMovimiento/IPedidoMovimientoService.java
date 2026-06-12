package com.fulfilltrack.FulfillTrack.features.pedidoMovimiento;

import com.fulfilltrack.FulfillTrack.features.pedido.EstadoPedido;
import com.fulfilltrack.FulfillTrack.features.pedido.PedidoEntity;
import com.fulfilltrack.FulfillTrack.features.pedidoMovimiento.dto.PedidoMovimientoResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IPedidoMovimientoService {
    void registrarMovimiento(PedidoEntity pedido, EstadoPedido estadoAnterior, EstadoPedido estadoNuevo);
    List<PedidoMovimientoResponseDTO> listarMovimientosPorPedido(UUID pedidoUuid);
}