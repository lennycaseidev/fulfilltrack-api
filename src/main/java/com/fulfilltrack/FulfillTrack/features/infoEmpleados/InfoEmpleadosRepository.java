package com.fulfilltrack.FulfillTrack.features.infoEmpleados;

import com.fulfilltrack.FulfillTrack.features.empresa.EmpresaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InfoEmpleadosRepository extends JpaRepository<InfoEmpleadosEntity, Long> {
    Optional<InfoEmpleadosEntity> findByUuid(UUID uuid);
    Optional<InfoEmpleadosEntity> findByUsuario_Uuid(UUID usuarioUuid);
    boolean existsByDocumento(String documento);
    boolean existsByUsuario_Uuid(UUID usuarioUuid);

}
