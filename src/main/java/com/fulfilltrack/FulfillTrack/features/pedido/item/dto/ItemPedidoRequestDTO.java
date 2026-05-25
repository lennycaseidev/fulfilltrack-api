package com.fulfilltrack.FulfillTrack.features.pedido.item.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoRequestDTO {
    @NotNull(message = "la cantidad es obligatoria")
    @Min(value = 1, message = "la cantidad debe ser >= a 1")
    private Integer cantidad;
    @NotNull(message = "el uuid del producto es obligatorio")
    private UUID productoUuid;
}
