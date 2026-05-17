package com.fulfilltrack.FulfillTrack.features.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockResponseDTO {
    private UUID uuid;
    private UUID productoUuid;
    private String nombreProducto;
    private String sku;
    private Integer cantidadDisponible;
    private Integer cantidadReservada;
    private LocalDateTime fechaActualizacion;
}