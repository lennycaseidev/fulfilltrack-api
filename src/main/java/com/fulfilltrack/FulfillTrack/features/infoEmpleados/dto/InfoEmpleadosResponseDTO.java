package com.fulfilltrack.FulfillTrack.features.infoEmpleados.dto;

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
public class InfoEmpleadosResponseDTO {
    private UUID uuid;
    private String documento;
    private BigDecimal salario;
    private LocalDateTime fechaContratacion;
    private UUID puestoUuid;
    private UUID usuarioUuid;
    private UUID depositoUuid;
    private Estado estado;



}
