package com.fulfilltrack.FulfillTrack.features.puesto;

import com.fulfilltrack.FulfillTrack.features.empresa.EmpresaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PuestoRepository extends JpaRepository<PuestoEntity, Long> {
    Optional<PuestoEntity> findByUuid(UUID uuid);
    boolean existsByUuid(UUID uuid);
}
