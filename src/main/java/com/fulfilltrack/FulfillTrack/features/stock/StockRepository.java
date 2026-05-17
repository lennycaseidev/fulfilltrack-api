package com.fulfilltrack.FulfillTrack.features.stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Long> {
    Optional<StockEntity> findByProducto_Uuid(UUID productoUuid);
    List<StockEntity> findByProducto_Empresa_Uuid(UUID empresaUuid);
}