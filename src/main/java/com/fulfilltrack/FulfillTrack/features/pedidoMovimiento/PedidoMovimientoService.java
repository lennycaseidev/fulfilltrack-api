package com.fulfilltrack.FulfillTrack.features.pedidoMovimiento;

import com.fulfilltrack.FulfillTrack.auth.credenciales.CredencialEntity;
import com.fulfilltrack.FulfillTrack.features.pedido.EstadoPedido;
import com.fulfilltrack.FulfillTrack.features.pedido.IPedidoService;
import com.fulfilltrack.FulfillTrack.features.pedido.PedidoEntity;
import com.fulfilltrack.FulfillTrack.features.pedidoMovimiento.dto.PedidoMovimientoResponseDTO;
import com.fulfilltrack.FulfillTrack.features.pedidoMovimiento.mapper.PedidoMovimientoMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PedidoMovimientoService implements IPedidoMovimientoService {

    private final PedidoMovimientoRepository movimientoRepository;
    private final PedidoMovimientoMapper movimientoMapper;
    private final IPedidoService pedidoService;

    public PedidoMovimientoService(PedidoMovimientoRepository movimientoRepository,
                                   PedidoMovimientoMapper movimientoMapper,
                                   @Lazy IPedidoService pedidoService) {
        this.movimientoRepository = movimientoRepository;
        this.movimientoMapper = movimientoMapper;
        this.pedidoService = pedidoService;
    }

    @Override
    public void registrarMovimiento(PedidoEntity pedido, EstadoPedido estadoAnterior, EstadoPedido estadoNuevo) {
        movimientoRepository.save(PedidoMovimientoEntity.builder()
                .pedido(pedido)
                .estadoAnterior(estadoAnterior)
                .estadoNuevo(estadoNuevo)
                .usuarioUuid(resolverUsuarioUuid())
                .build());
    }

    private UUID resolverUsuarioUuid() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CredencialEntity credencial
                && credencial.getUsuario() != null) {
            return credencial.getUsuario().getUuid();
        }
        return null;
    }

    @Override
    public List<PedidoMovimientoResponseDTO> listarMovimientosPorPedido(UUID pedidoUuid) {
        pedidoService.obtenerPedidoUuid(pedidoUuid);
        return movimientoMapper.toResponseList(
                movimientoRepository.findByPedido_UuidOrderByFechaDesc(pedidoUuid));
    }
}