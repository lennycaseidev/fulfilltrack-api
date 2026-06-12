package com.fulfilltrack.FulfillTrack.features.liquidacion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LiquidacionRequestDTO {

    @NotBlank(message = "El período es obligatorio")
    private String periodo;

    @NotNull(message = "El UUID de la empresa es obligatorio")
    private UUID empresaUuid;
}