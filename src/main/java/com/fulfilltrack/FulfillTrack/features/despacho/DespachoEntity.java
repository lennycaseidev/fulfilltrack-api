package com.fulfilltrack.FulfillTrack.features.despacho;

import com.fulfilltrack.FulfillTrack.features.pedido.PedidoEntity;
import com.fulfilltrack.FulfillTrack.features.usuario.UsuarioEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "despacho")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DespachoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_despacho")
    private Long idDespacho;

    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @Column(name = "tracking", nullable = false)
    private String tracking;

    @Column(name = "courier", nullable = false)
    private String courier;

    @CreationTimestamp
    @Column(name = "fecha_despacho", updatable = false)
    private LocalDateTime fechaDespacho;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false, unique = true)
    private PedidoEntity pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuario;

    @PrePersist
    public void prePersist() {
        this.uuid = UUID.randomUUID();
    }
}