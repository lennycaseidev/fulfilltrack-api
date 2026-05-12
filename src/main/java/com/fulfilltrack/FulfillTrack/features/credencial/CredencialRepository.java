package com.fulfilltrack.FulfillTrack.features.credencial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CredencialRepository extends JpaRepository<CredencialEntity, Long> {
    Optional<CredencialEntity> findByUuid(UUID uuid);
    Optional<CredencialEntity> findByEmail(String email);
    boolean existsByNombreUsuario(String nombreUsuario);
    boolean existsByEmail(String email);
}
