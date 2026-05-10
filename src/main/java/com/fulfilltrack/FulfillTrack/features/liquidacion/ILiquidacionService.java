package com.fulfilltrack.FulfillTrack.features.liquidacion;

import com.fulfilltrack.FulfillTrack.features.liquidacion.dto.LiquidacionRequestDTO;
import com.fulfilltrack.FulfillTrack.features.liquidacion.dto.LiquidacionResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ILiquidacionService {
    LiquidacionResponseDTO crearLiquidacion(LiquidacionRequestDTO request);
    LiquidacionResponseDTO obtenerLiquidacionPorUuid(UUID uuid);
    List<LiquidacionResponseDTO> listarLiquidaciones();
    List<LiquidacionResponseDTO> listarLiquidacionesPorEmpresa(UUID empresaUuid);
    LiquidacionResponseDTO actualizarLiquidacion(UUID uuid, LiquidacionRequestDTO request);
    void marcarComoPagada(UUID uuid);
    void marcarComoImpaga(UUID uuid);
}