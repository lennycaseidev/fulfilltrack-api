package com.fulfilltrack.FulfillTrack.features.usuarioEmpresa;

import com.fulfilltrack.FulfillTrack.common.utils.Estado;
import com.fulfilltrack.FulfillTrack.features.empresa.EmpresaEntity;
import com.fulfilltrack.FulfillTrack.features.usuario.UsuarioEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "usuario_empresa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioEmpresaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario_empresa")
    private Long idUsuarioEmpresa;

    @Column(name = "uuid", nullable = false, updatable = false, unique = true)
    private UUID uuid;

    @Column(name = "cargo", nullable = false)
    private String cargo;

    @Column(name = "telefono", nullable = false)
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa", nullable = false)
    private EmpresaEntity empresa;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private UsuarioEntity usuario;

    @PrePersist
    public void prePersist() {
        this.uuid = UUID.randomUUID();
        this.estado = Estado.ACTIVA;
    }
}