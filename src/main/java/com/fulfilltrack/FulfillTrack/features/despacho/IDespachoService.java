package com.fulfilltrack.FulfillTrack.features.despacho;

import com.fulfilltrack.FulfillTrack.features.despacho.dto.DespachoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.despacho.dto.DespachoResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IDespachoService {
    DespachoResponseDTO crearDespacho(DespachoRequestDTO request);
    DespachoResponseDTO obtenerDespachoPorUuid(UUID uuid);
    DespachoResponseDTO obtenerDespachoPorPedido(UUID pedidoUuid);
    List<DespachoResponseDTO> listarDespachos();
    DespachoResponseDTO marcarEntregado(UUID uuid);
}