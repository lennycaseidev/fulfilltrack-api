package com.fulfilltrack.FulfillTrack.features.pedidoMovimiento;

import com.fulfilltrack.FulfillTrack.features.pedidoMovimiento.dto.PedidoMovimientoResponseDTO;
import com.fulfilltrack.FulfillTrack.features.pedidoMovimiento.mapper.PedidoMovimientoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PedidoMovimientoService implements IPedidoMovimientoService {

    private final PedidoMovimientoRepository movimientoRepository;
    private final PedidoMovimientoMapper movimientoMapper;

    @Override
    public List<PedidoMovimientoResponseDTO> listarMovimientosPorPedido(UUID pedidoUuid) {
        return null;
    }
}