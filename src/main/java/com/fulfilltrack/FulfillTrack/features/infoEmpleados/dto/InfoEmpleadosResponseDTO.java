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
    private String nombrePuesto;
    private UUID usuarioUuid;
    private String nombreUsuario;
    private String apellidoUsuario;
    private UUID depositoUuid;
    private String nombreDeposito;
    private Estado estado;



}
