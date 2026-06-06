package com.fulfilltrack.FulfillTrack.auth.permisos;


import jakarta.persistence.*;
import lombok.*;

@Entity
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    Permisos permiso;


}

