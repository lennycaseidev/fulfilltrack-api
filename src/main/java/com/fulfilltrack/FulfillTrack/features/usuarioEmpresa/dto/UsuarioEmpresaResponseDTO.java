package com.fulfilltrack.FulfillTrack.features.usuarioEmpresa.dto;

import com.fulfilltrack.FulfillTrack.common.utils.Estado;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioEmpresaResponseDTO {

    private UUID uuid;
    private String cargo;
    private String telefono;
    private Estado estado;
    private UUID empresaUuid;
    private UUID usuarioUuid;
}