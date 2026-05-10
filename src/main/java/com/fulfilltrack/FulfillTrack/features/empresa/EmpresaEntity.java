package com.fulfilltrack.FulfillTrack.features.empresa;


import com.fulfilltrack.FulfillTrack.common.utils.Estado;
import com.fulfilltrack.FulfillTrack.features.deposito.DepositoEntity;
import com.fulfilltrack.FulfillTrack.features.liquidacion.LiquidacionEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name= "empresa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpresaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    private Long idEmpresa;

    @Column(name ="uuid", nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @Column(name = "nombre_empresa", nullable = false)
    private String nombreEmpresa;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name ="costo_por_envio",precision = 10, scale = 2)
    private BigDecimal costoPorEnvio;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private Estado estado;

@CreationTimestamp
@Column(updatable = false)
private LocalDateTime fechaCreacion;

    @PrePersist
    public void prePersist() {
        this.uuid = UUID.randomUUID();
        this.estado = Estado.ACTIVA;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_deposito", nullable = false)
    private DepositoEntity deposito;

    @OneToMany(mappedBy = "empresa")
    private List<LiquidacionEntity> liquidaciones;

}
