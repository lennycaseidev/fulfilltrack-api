package com.fulfilltrack.FulfillTrack.features.credencial.dto;

import com.fulfilltrack.FulfillTrack.common.utils.Estado;
import com.fulfilltrack.FulfillTrack.features.permiso.PermisoEntity;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CredencialResponseDTO {
    private UUID uuid;
    private String nombreUsuario;
    private String email;
    private Estado estado;
    private UUID permisoUuid;
}
