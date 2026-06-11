package com.fulfilltrack.FulfillTrack.features.despacho;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DespachoRepository extends JpaRepository<DespachoEntity, Long> {
    Optional<DespachoEntity> findByUuid(UUID uuid);
    Optional<DespachoEntity> findByPedido_Uuid(UUID pedidoUuid);
    boolean existsByPedido_Uuid(UUID pedidoUuid);
    int countByPedido_Empresa_UuidAndFechaDespachoBetween(UUID empresaUuid, LocalDateTime inicio, LocalDateTime fin);
    List<DespachoEntity> findByPedido_Empresa_Uuid(UUID empresaUuid);
}
