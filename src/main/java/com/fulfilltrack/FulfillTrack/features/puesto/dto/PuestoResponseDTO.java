package com.fulfilltrack.FulfillTrack.features.puesto.dto;

import lombok.*;

import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PuestoResponseDTO {
    private UUID uuid;
    private String nombrePuesto;
    private String descripcion;
}
