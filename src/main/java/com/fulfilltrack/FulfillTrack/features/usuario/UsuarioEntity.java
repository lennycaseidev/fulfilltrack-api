package com.fulfilltrack.FulfillTrack.features.usuario;


import com.fulfilltrack.FulfillTrack.features.credencial.CredencialEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "uuid", nullable = false, updatable = false, unique = true)
    private UUID uuid;
    @Column(name="nombre",nullable = false)
    private String nombre;
    @Column(name="apellido",nullable = false)
    private String apellido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_credencial", nullable = false)
    private CredencialEntity credencial;

    @PrePersist
    public void prePersist() {
        this.uuid = UUID.randomUUID();
    }
}
