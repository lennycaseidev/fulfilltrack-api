package com.fulfilltrack.FulfillTrack.features.despacho.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DespachoRequestDTO {

    @NotNull
    private UUID pedidoUuid;

    @NotNull
    private UUID usuarioUuid;

    @NotBlank
    private String courier;
}
