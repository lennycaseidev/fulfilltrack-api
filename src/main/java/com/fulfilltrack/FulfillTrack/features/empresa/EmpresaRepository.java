package com.fulfilltrack.FulfillTrack.features.empresa;


import com.fulfilltrack.FulfillTrack.common.utils.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmpresaRepository extends JpaRepository<EmpresaEntity, Long> {
    Optional<EmpresaEntity> findByUuid(UUID uuid);
    boolean existsByEmail(String email);
    boolean existsByEmailAndUuidNot(String email, UUID uuid);
    boolean existsByUuid(UUID uuid);
    List<EmpresaEntity> findByEstado(Estado estado);
}
