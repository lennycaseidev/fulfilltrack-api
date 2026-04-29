package com.fulfilltrack.FulfillTrack.features.empresa.dto;


import com.fulfilltrack.FulfillTrack.features.empresa.EstadoEmpresa;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpresaResponseDTO {
    private UUID uuid;
    private String nombreEmpresa;
    private String email;
    private BigDecimal costoPorEnvio;
    private EstadoEmpresa estado;
    private LocalDateTime fechaCreacion;
}
