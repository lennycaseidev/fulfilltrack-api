package com.fulfilltrack.FulfillTrack.features.pedido;

import com.fulfilltrack.FulfillTrack.features.pedido.dto.PedidoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.pedido.dto.PedidoResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IPedidoService {
    PedidoResponseDTO crearPedido(PedidoRequestDTO request);
    PedidoResponseDTO obtenerPedidoPorUuid(UUID uuid);
    List<PedidoResponseDTO> listarPedidos();
    List<PedidoResponseDTO> listarPedidosPorEmpresa(UUID empresaUuid);
    PedidoEntity obtenerPedidoUuid(UUID uuid);
    PedidoResponseDTO confirmarPedido(UUID uuid);
    PedidoResponseDTO iniciarPreparacion(UUID uuid);
    PedidoResponseDTO marcarListoParaDespacho(UUID uuid);
    PedidoResponseDTO marcarDespachado(UUID uuid);
    PedidoResponseDTO devolverPedido(UUID uuid);
}
