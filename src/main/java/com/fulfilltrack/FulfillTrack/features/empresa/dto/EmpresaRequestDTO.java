package com.fulfilltrack.FulfillTrack.features.empresa.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaRequestDTO {
    @NotBlank(message = "El nombre de la empresa es obligatorio")
    private String nombreEmpresa;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String email;

    @NotNull(message = "El costo por envío es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El costo por envío debe ser mayor a cero")
    private BigDecimal costoPorEnvio;

    @NotNull(message = "El UUID del depósito es obligatorio")
    private UUID depositoUuid;
}
