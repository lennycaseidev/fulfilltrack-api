package com.fulfilltrack.FulfillTrack.features.infoEmpleados.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InfoEmpleadosRequestDTO {
    @NotBlank(message = "El documento es obligatorio")
    private String documento;
    @NotNull(message = "El salario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El salario debe ser mayor a cero")
    private BigDecimal salario;
    @NotNull(message="La fecha de contratacion es obligatoria")
    private LocalDateTime fechaContratacion;
    @NotNull(message = "El UUID del puesto es obligatorio")
    private UUID puestoUuid;
    @NotNull(message = "El UUID del usuario es obligatorio")
    private UUID usuarioUuid;
    @NotNull(message = "El UUID del depósito es obligatorio")
    private UUID depositoUuid;

}
