package com.fulfilltrack.FulfillTrack.auth.credenciales;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CredencialRepository extends JpaRepository<CredencialEntity, Long> {
    Optional<CredencialEntity> findByUsername(String username);
    List<CredencialEntity> findByActivo(Boolean activo);
    Optional<CredencialEntity> findByUsuario(com.fulfilltrack.FulfillTrack.features.usuario.UsuarioEntity usuario);
}
