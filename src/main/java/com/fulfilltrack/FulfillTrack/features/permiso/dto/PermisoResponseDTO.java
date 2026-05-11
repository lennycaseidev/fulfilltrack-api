package com.fulfilltrack.FulfillTrack.features.permiso.dto;

import lombok.*;

import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermisoResponseDTO {
    private UUID uuid;
    private String nombrePermiso;
}
