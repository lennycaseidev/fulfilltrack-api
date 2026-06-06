package com.fulfilltrack.FulfillTrack.auth.permisos;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private Roles rol;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "rol_permisos",
            joinColumns = @JoinColumn(name = "rol_id"),
            inverseJoinColumns = @JoinColumn(name = "permit_id"))

    private final Set<PermisoEntity> permisos = new HashSet<>();

    public RolEntity(Roles nombre) {
        this.rol = nombre;
    }

    public void agregarPermiso(PermisoEntity permiso) {
        this.permisos.add(permiso);
    }







}
