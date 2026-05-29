package com.fulfilltrack.FulfillTrack.features.despacho;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DespachoRepository extends JpaRepository<DespachoEntity, Long> {
    Optional<DespachoEntity> findByUuid(UUID uuid);
    Optional<DespachoEntity> findByPedido_Uuid(UUID pedidoUuid);
    boolean existsByPedido_Uuid(UUID pedidoUuid);
}
