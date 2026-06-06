package com.fulfilltrack.FulfillTrack.auth.permisos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PermisoRepository extends JpaRepository<PermisoEntity, Long> {
    Optional<PermisoEntity> findByPermiso(Permisos permiso);
    Set<PermisoEntity> findByPermisoIn(Collection<Permisos> permisos);
}
