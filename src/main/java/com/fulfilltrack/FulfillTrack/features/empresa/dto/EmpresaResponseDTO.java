package com.fulfilltrack.FulfillTrack.features.empresa.dto;


import com.fulfilltrack.FulfillTrack.common.utils.Estado;
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
    private Estado estado;
    private LocalDateTime fechaCreacion;
    private UUID depositoUuid;
    private String nombreDeposito;
}
