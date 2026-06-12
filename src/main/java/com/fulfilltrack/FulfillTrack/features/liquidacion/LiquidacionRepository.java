package com.fulfilltrack.FulfillTrack.features.liquidacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LiquidacionRepository extends JpaRepository<LiquidacionEntity, Long> {
    Optional<LiquidacionEntity> findByUuid(UUID uuid);
    List<LiquidacionEntity> findByEmpresa_Uuid(UUID empresaUuid);
    List<LiquidacionEntity> findByEstadoPago(EstadoPago estadoPago);
    boolean existsByEmpresa_UuidAndPeriodo(UUID empresaUuid, String periodo);
}