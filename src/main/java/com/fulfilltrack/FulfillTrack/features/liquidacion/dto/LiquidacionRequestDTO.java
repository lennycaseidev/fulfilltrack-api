package com.fulfilltrack.FulfillTrack.features.liquidacion.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
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
public class LiquidacionRequestDTO {

    @NotBlank(message = "El período es obligatorio")
    private String periodo;

    @NotNull(message = "El total de despachos es obligatorio")
    @Min(value = 1, message = "El total de despachos debe ser mayor a cero")
    private Integer totalDespachos;

    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio unitario debe ser mayor a cero")
    private BigDecimal precioUnitario;

    @NotNull(message = "El UUID de la empresa es obligatorio")
    private UUID empresaUuid;
}