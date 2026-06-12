package com.fulfilltrack.FulfillTrack.features.liquidacion.dto;

import com.fulfilltrack.FulfillTrack.features.liquidacion.EstadoPago;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LiquidacionResponseDTO {
    private UUID uuid;
    private String periodo;
    private Integer totalDespachos;
    private BigDecimal precioUnitario;
    private BigDecimal total;
    private EstadoPago estadoPago;
    private LocalDateTime fechaCreacion;
    private UUID empresaUuid;
    private String nombreEmpresa;
}