package com.fulfilltrack.FulfillTrack.features.deposito.dto;

import com.fulfilltrack.FulfillTrack.common.utils.Estado;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepositoResponseDTO {
    private UUID uuid;
    private String nombreDeposito;
    private String email;
    private String direccionDeposito;
    private String telefonoDeposito;
    private LocalTime aperturaDeposito;
    private LocalTime cierreDeposito;
    private LocalDateTime creacionDeposito;
    private Estado estado;
}