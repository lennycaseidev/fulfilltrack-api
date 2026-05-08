package com.fulfilltrack.FulfillTrack.features.puesto;

import com.fulfilltrack.FulfillTrack.features.empresa.EmpresaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PuestoRepository extends JpaRepository<PuestoEntity, Long> {
    Optional<PuestoEntity> findByUuid(UUID uuid);
}
