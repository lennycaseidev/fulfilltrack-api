package com.fulfilltrack.FulfillTrack.features.producto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoRequestDTO {
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 100, message = "El nombre del producto no puede superar los 100 caracteres")
    private String nombreProducto;
    @Size(max = 200, message = "La descripcion del producto no puede superar los 200 caracteres")
    private String descripcion;
    @NotBlank(message = "El sku del producto es obligatorio")
    @Size(max = 20, message = "el sku no puede superar los 20 caracteres")
    private String sku;
    private UUID empresaUuid;
}
