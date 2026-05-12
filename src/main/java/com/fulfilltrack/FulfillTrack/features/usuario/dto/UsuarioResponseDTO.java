package com.fulfilltrack.FulfillTrack.features.usuario.dto;

import lombok.*;

import java.util.UUID;

@Getter

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {
    private UUID uuid;
    private String nombre;
    private String apellido;
    private String nombrePermiso;
    private UUID credencialUuid;
}
