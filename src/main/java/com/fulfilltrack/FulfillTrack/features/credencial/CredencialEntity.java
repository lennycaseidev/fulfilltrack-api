package com.fulfilltrack.FulfillTrack.features.credencial;


import com.fulfilltrack.FulfillTrack.common.utils.Estado;
import com.fulfilltrack.FulfillTrack.features.permiso.PermisoEntity;
import com.fulfilltrack.FulfillTrack.features.usuario.UsuarioEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "credencial")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CredencialEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_credencial")
    private Long idCredencial;

    @Column(name = "uuid", nullable = false, updatable = false, unique = true)
    private UUID uuid;

    @Column(name = "nombre_usuario", nullable = false, unique = true)
    private String nombreUsuario;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "contrasena", nullable = false)
    private String contrasena;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_usuario", nullable = false)
    private Estado estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_permiso")
    private PermisoEntity permiso;

    @OneToOne(mappedBy = "credencial")
    private UsuarioEntity usuario;


    @PrePersist
    public void prePersist(){
        this.uuid = UUID.randomUUID();
        this.estado = Estado.ACTIVA;
    }
}
