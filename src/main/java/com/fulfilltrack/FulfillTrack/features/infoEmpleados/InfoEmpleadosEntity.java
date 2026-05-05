package com.fulfilltrack.FulfillTrack.features.infoEmpleados;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name= "info_empleados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfoEmpleadosEntity {
    @Column(name ="uuid", nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado")
    private long idEmpleado;

    @Column(name="documento_empleado", nullable = false, unique = true)
    private String documento;

    @Column(name="salario_empleado", nullable = false)
    private BigDecimal salario;


    @Column(name="contratacion_empleado", nullable = false)
    private LocalDateTime fechaContratacion;

    @PrePersist
    public void prePersist() {
        this.uuid = UUID.randomUUID();
    }

    //Falta hacer tabla puesto
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name= "id_puesto", nullable = false)
//    private PuestoEntity puesto;


}
