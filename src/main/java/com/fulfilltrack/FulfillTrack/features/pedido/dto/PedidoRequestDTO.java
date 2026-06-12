package com.fulfilltrack.FulfillTrack.features.pedido.dto;

import com.fulfilltrack.FulfillTrack.features.pedido.item.dto.ItemPedidoRequestDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequestDTO {
    @NotBlank(message = "el numero de orden es obligatorio")
    private String numeroOrden;
    @NotBlank(message = "la direccion de entrega es obligatoria")
    private String direccionEntrega;
    @NotBlank(message = "el nombre del destinatario es obligatorio")
    private String nombreDestinatario;
    private UUID empresaUuid;
    @NotEmpty(message = "la lista de items es obligatoria")
    private List<ItemPedidoRequestDTO> items;


}
