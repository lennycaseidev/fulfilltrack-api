package com.fulfilltrack.FulfillTrack.auth.permisos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<RolEntity, Long> {
    Optional<RolEntity> findByRol(Roles rol);
}
