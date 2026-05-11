package com.fulfilltrack.FulfillTrack.features.permiso;

import com.fulfilltrack.FulfillTrack.features.puesto.PuestoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PermisoRepository extends JpaRepository<PermisoEntity, Long> {
    Optional<PermisoEntity> findByUuid(UUID uuid);
    boolean existsByNombrePermiso(String nombrePermiso);
}
