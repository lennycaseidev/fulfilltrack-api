package com.fulfilltrack.FulfillTrack.features.pedidoMovimiento;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PedidoMovimientoRepository extends JpaRepository<PedidoMovimientoEntity, Long> {
    List<PedidoMovimientoEntity> findByPedido_UuidOrderByFechaDesc(UUID pedidoUuid);
}