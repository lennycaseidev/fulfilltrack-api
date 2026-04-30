package com.fulfilltrack.FulfillTrack.features.deposito;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "deposito")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepositoEntity {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id_deposito")
private Long idDeposito;

@Column(name = "uuid" ,nullable = false, updatable = false)
private UUID uuid;


@Column(name = "nombre_deposito", nullable = false)
private String nombreDeposito;

@Column(name = "email_deposito", nullable = false, unique = true)
private String email;

@Column(name= "direccion_deposito", nullable = false)
private String direccionDeposito;

@Column(name = "telefono_deposito", nullable = false)
private String telefonoDeposito;

@Column(name = "horario_apertura", nullable = false)
private LocalTime aperturaDeposito;

@Column(name = "horario_cierre", nullable = false)
private LocalTime cierreDeposito;

@CreationTimestamp
@Column(name = "creacion_deposito", updatable = false)
private LocalDateTime creacionDeposito;


@PrePersist
public void prePersist() {
    this.uuid = UUID.randomUUID();
}

}