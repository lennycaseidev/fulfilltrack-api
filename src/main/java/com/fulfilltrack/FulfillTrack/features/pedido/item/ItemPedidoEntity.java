package com.fulfilltrack.FulfillTrack.features.pedido.item;

import com.fulfilltrack.FulfillTrack.features.pedido.PedidoEntity;
import com.fulfilltrack.FulfillTrack.features.producto.ProductoEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name= "item_pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item_pedido")
    private Long idItemPedido;
    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    private UUID uuid;
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name ="id_pedido", nullable = false)
    private PedidoEntity pedido;
    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name ="id_producto", nullable = false)
    private ProductoEntity producto;

    @PrePersist
    public void prePersist(){
        this.uuid = UUID.randomUUID();
    }
}
