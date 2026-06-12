package com.fulfilltrack.FulfillTrack.features.producto.dto;

import com.fulfilltrack.FulfillTrack.common.utils.Estado;
import lombok.*;

import java.util.UUID;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoResponseDTO {
    private UUID uuid;
    private String nombreProducto;
    private String descripcion;
    private String sku;
    private Estado estado;
    private UUID empresaUuid;
    private String nombreEmpresa;
}
