package com.fulfilltrack.FulfillTrack.features.stockMovimiento;

import com.fulfilltrack.FulfillTrack.features.producto.ProductoEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "stock_movimiento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockMovimientoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_stock_movimiento")
    private Long idStockMovimiento;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private ProductoEntity producto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimientoStock tipo;

    @Column(nullable = false)
    private String motivo;

    @Column(nullable = false)
    private Integer cantidad;

    // pendiente hasta implementar autenticación
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