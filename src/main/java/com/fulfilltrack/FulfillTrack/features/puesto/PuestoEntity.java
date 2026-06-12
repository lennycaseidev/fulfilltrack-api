package com.fulfilltrack.FulfillTrack.features.puesto;


import com.fulfilltrack.FulfillTrack.features.infoEmpleados.InfoEmpleadosEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "puesto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PuestoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_puesto")
    private Long idPuesto;

    @Column(name = "uuid", nullable = false, updatable = false, unique = true)
    private UUID uuid;

    @Column(name="nombre_puesto", nullable = false)
    private String nombrePuesto;

    @Column(name="descripcion_puesto")
    private String descripcion;

    @OneToMany(mappedBy = "puesto")
    private List<InfoEmpleadosEntity> empleados;

@PrePersist
public void prePersist() {
    this.uuid = UUID.randomUUID();
}
}
