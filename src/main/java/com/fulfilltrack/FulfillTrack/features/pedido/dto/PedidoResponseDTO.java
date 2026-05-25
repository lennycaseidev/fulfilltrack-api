package com.fulfilltrack.FulfillTrack.features.pedido.dto;

import com.fulfilltrack.FulfillTrack.features.pedido.EstadoPedido;
import com.fulfilltrack.FulfillTrack.features.pedido.item.dto.ItemPedidoResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDTO {
    private UUID uuid;
    private String numeroOrden;
    private String direccionEntrega;
    private String nombreDestinatario;
    private EstadoPedido estado;
    private LocalDateTime fechaRecepcion;
    private String nombreEmpresa;
    private List<ItemPedidoResponseDTO> items;

}
