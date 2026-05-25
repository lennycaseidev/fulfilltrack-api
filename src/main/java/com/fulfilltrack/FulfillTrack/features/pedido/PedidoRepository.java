package com.fulfilltrack.FulfillTrack.features.pedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {
    Optional<PedidoEntity> findByUuid(UUID uuid);
    List<PedidoEntity> findByEmpresa_Uuid(UUID empresaUuid);
}

