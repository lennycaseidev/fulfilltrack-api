package com.fulfilltrack.FulfillTrack.features.pedidoMovimiento;

import com.fulfilltrack.FulfillTrack.auth.TenantContext;
import com.fulfilltrack.FulfillTrack.features.pedido.EstadoPedido;
import com.fulfilltrack.FulfillTrack.features.pedido.IPedidoService;
import com.fulfilltrack.FulfillTrack.features.pedido.PedidoEntity;
import com.fulfilltrack.FulfillTrack.features.pedidoMovimiento.dto.PedidoMovimientoResponseDTO;
import com.fulfilltrack.FulfillTrack.features.pedidoMovimiento.mapper.PedidoMovimientoMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PedidoMovimientoService implements IPedidoMovimientoService {

    private final PedidoMovimientoRepository movimientoRepository;
    private final PedidoMovimientoMapper movimientoMapper;
    private final IPedidoService pedidoService;
    private final TenantContext tenantContext;

    public PedidoMovimientoService(PedidoMovimientoRepository movimientoRepository,
                                   PedidoMovimientoMapper movimientoMapper,
                                   @Lazy IPedidoService pedidoService,
                                   TenantContext tenantContext) {
        this.movimientoRepository = movimientoRepository;
        this.movimientoMapper = movimientoMapper;
        this.pedidoService = pedidoService;
        this.tenantContext = tenantContext;
    }

    @Override
    public void registrarMovimiento(PedidoEntity pedido, EstadoPedido estadoAnterior, EstadoPedido estadoNuevo) {
        UUID usuarioUuid = tenantContext.getUsuarioActual().map(u -> u.getUuid()).orElse(null);
        movimientoRepository.save(PedidoMovimientoEntity.builder()
                .pedido(pedido)
                .estadoAnterior(estadoAnterior)
                .estadoNuevo(estadoNuevo)
                .usuarioUuid(usuarioUuid)
                .build());
    }

    @Override
    public List<PedidoMovimientoResponseDTO> listarMovimientosPorPedido(UUID pedidoUuid) {
        pedidoService.obtenerPedidoUuid(pedidoUuid);
        return movimientoMapper.toResponseList(
                movimientoRepository.findByPedido_UuidOrderByFechaDesc(pedidoUuid));
    }
}