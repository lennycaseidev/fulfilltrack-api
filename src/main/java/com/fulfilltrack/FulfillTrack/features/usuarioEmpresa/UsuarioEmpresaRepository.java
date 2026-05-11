package com.fulfilltrack.FulfillTrack.features.usuarioEmpresa;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioEmpresaRepository extends JpaRepository<UsuarioEmpresaEntity, Long> {
    Optional<UsuarioEmpresaEntity> findByUuid(UUID uuid);
    List<UsuarioEmpresaEntity> findByEmpresa_Uuid(UUID empresaUuid);
}