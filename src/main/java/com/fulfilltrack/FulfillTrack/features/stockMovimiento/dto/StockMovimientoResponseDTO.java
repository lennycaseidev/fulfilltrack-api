package com.fulfilltrack.FulfillTrack.features.stockMovimiento.dto;

import com.fulfilltrack.FulfillTrack.features.stockMovimiento.TipoMovimientoStock;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class StockMovimientoResponseDTO {
    private UUID uuid;
    private UUID productoUuid;
    private String productoNombre;
    private String productoSku;
    private TipoMovimientoStock tipo;
    private String motivo;
    private Integer cantidad;
    private LocalDateTime fecha;
}