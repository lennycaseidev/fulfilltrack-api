package com.fulfilltrack.FulfillTrack.features.producto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductoRepository extends JpaRepository<ProductoEntity, Long> {
    Optional<ProductoEntity> findByUuid(UUID uuid);
    Optional<ProductoEntity> findBySku(String sku);
    Optional<ProductoEntity> findBySkuAndEmpresa_Uuid(String sku, UUID empresaUuid);
    List<ProductoEntity> findByEmpresa_Uuid(UUID empresaUuid);
    boolean existsBySkuAndEmpresa_Uuid(String sku, UUID empresaUuid);

}
