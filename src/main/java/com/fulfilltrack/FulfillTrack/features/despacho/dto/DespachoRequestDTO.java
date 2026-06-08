package com.fulfilltrack.FulfillTrack.features.despacho.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DespachoRequestDTO {

    @NotNull(message = "el uuid del pedido no puede ser nulo")
    private UUID pedidoUuid;

    @NotNull
    private UUID usuarioUuid;

    @NotBlank(message = "el courier no puede ser nulo")
    private String courier;
}
