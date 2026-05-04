package com.fulfilltrack.FulfillTrack.features.deposito.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepositoRequestDTO {
    @NotBlank(message = "El nombre del deposito es obligatorio")
    private String nombreDeposito;
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String email;
    @NotBlank(message = "La direccion del deposito es obligatoria")
    private String direccionDeposito;
    @NotNull(message = "El telefono del deposito es obligatorio")
    private String telefonoDeposito;
    @NotNull(message = "El horario de apertura es obligatorio")
    private LocalTime aperturaDeposito;
    @NotNull(message = "El horario de cierre es obligatorio")
    private LocalTime cierreDeposito;

}