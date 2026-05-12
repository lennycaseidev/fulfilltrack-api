package com.fulfilltrack.FulfillTrack.features.producto;


import com.fulfilltrack.FulfillTrack.common.utils.Estado;
import com.fulfilltrack.FulfillTrack.features.empresa.EmpresaEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "producto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long idProducto;

    @Column(name = "uuid", nullable = false, updatable = false, unique = true)
    private UUID uuid;
    @Column(name="nombre_producto", nullable = false)
    private String nombreProducto;
    @Column(name="descripcion_producto")
    private String descripcion;
    @Column(name = "sku",unique = true,nullable = false)
    private String sku;
    @Enumerated(EnumType.STRING)
    @Column(name="estado")
    private Estado estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_empresa", nullable = false)
    private EmpresaEntity empresa;

    @PrePersist
    public void prePersist() {
        this.uuid = UUID.randomUUID();
        this.estado = Estado.ACTIVA;
    }


}
