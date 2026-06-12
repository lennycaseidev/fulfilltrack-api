package com.fulfilltrack.FulfillTrack.features.stockMovimiento;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StockMovimientoRepository extends JpaRepository<StockMovimientoEntity, Long> {
    List<StockMovimientoEntity> findByProducto_UuidOrderByFechaDesc(UUID productoUuid);
    List<StockMovimientoEntity> findByProducto_Empresa_UuidOrderByFechaDesc(UUID empresaUuid);
}