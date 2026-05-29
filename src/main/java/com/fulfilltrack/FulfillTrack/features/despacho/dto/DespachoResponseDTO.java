package com.fulfilltrack.FulfillTrack.features.despacho.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class DespachoResponseDTO {

    private UUID uuid;
    private UUID pedidoUuid;
    private String numeroOrden;
    private UUID usuarioUuid;
    private String nombreUsuario;
    private String apellidoUsuario;
    private String tracking;
    private String courier;
    private LocalDateTime fechaDespacho;
}