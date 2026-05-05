package com.fulfilltrack.FulfillTrack.features.infoEmpleados;

import com.fulfilltrack.FulfillTrack.features.empresa.EmpresaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InfoEmpleadosRepository extends JpaRepository<InfoEmpleadosEntity, Long> {
    Optional<InfoEmpleadosEntity> findByUuid(UUID uuid);
    boolean existsByDocumento(String documento);
    boolean existsByUuid(UUID uuid);
}
