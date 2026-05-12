package com.fulfilltrack.FulfillTrack.features.permiso;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "permiso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermisoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_permiso")
    private Long idPermiso;

    @Column(name = "uuid", nullable = false, updatable = false, unique = true)
    private UUID uuid;

    @Column(name = "nombre_permiso", nullable = false, unique = true)
    private String nombrePermiso;

    @PrePersist

    public void prePersist() {
        this.uuid = UUID.randomUUID();
    }
}

