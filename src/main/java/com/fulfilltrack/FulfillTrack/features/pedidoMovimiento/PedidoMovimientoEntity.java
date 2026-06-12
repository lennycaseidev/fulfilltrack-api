package com.fulfilltrack.FulfillTrack.features.pedidoMovimiento;

import com.fulfilltrack.FulfillTrack.features.pedido.EstadoPedido;
import com.fulfilltrack.FulfillTrack.features.pedido.PedidoEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pedido_movimiento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoMovimientoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido_movimiento")
    private Long idPedidoMovimiento;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false)
    private PedidoEntity pedido;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_anterior", nullable = false)
    private EstadoPedido estadoAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_nuevo", nullable = false)
    private EstadoPedido estadoNuevo;

    @Column(name = "usuario_uuid")
    private UUID usuarioUuid;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime fecha;

    @PrePersist
    public void prePersist() {
        this.uuid = UUID.randomUUID();
    }
}