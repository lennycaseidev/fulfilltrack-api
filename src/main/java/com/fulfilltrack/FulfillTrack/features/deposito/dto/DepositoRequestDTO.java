package com.fulfilltrack.FulfillTrack.features.deposito.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepositoRequestDTO {
    @NotBlank(message = "El nombre del deposito es obligatorio")
    @Size(max = 100, message = "El nombre del depósito no puede superar los 100 caracteres")
    private String nombreDeposito;
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String email;
    @NotBlank(message = "La direccion del deposito es obligatoria")
    @Size(max = 200, message = "La dirección no puede superar los 200 caracteres")
    private String direccionDeposito;
    @NotNull(message = "El telefono del deposito es obligatorio")
    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
    private String telefonoDeposito;
    @NotNull(message = "El horario de apertura es obligatorio")
    private LocalTime aperturaDeposito;
    @NotNull(message = "El horario de cierre es obligatorio")
    private LocalTime cierreDeposito;

}