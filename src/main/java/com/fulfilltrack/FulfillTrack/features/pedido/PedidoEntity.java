package com.fulfilltrack.FulfillTrack.features.pedido;

import com.fulfilltrack.FulfillTrack.features.empresa.EmpresaEntity;
import com.fulfilltrack.FulfillTrack.features.pedido.item.ItemPedidoEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name= "pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Long idPedido;
    @Column(name ="uuid", nullable = false, unique = true, updatable = false)
    private UUID uuid;
    @Column(name ="numero_orden", nullable = false)
    private String numeroOrden;
    @Column(name ="direccion_entrega", nullable = false)
    private String direccionEntrega;
    @Column(name ="nombre_destinatario", nullable = false)
    private String nombreDestinatario;
    @Enumerated(EnumType.STRING)
    @Column(name ="estado_pedido", nullable = false)
    private EstadoPedido estado;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime fechaRecepcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="id_empresa", nullable = false)
    private EmpresaEntity empresa;

    @PrePersist
    public void prePersist(){
        this.uuid = UUID.randomUUID();
        this.estado = EstadoPedido.RECIBIDO;
    }

    @OneToMany(mappedBy = "pedido", fetch = FetchType.LAZY)
    private List<ItemPedidoEntity> items;


}
