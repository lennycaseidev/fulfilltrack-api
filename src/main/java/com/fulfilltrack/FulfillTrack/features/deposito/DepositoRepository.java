package com.fulfilltrack.FulfillTrack.features.deposito;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DepositoRepository extends JpaRepository<DepositoEntity, Long> {
Optional<DepositoEntity> findByUuid(UUID uuid);
boolean existsByEmail(String email);
boolean existsByEmailAndUuidNot(String email, UUID uuid);
}