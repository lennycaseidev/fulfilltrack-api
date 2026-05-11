package com.fulfilltrack.FulfillTrack.features.usuarioEmpresa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEmpresaRequestDTO {

    @NotBlank(message = "El cargo es obligatorio")
    @Size(max = 100, message = "El cargo no puede superar los 100 caracteres")
    private String cargo;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
    private String telefono;

    @NotNull(message = "El UUID de la empresa es obligatorio")
    private UUID empresaUuid;

//    @NotNull(message = "El UUID del usuario es obligatorio")
//    private UUID usuarioUuid;
}