package com.fulfilltrack.FulfillTrack.auth.credenciales;

import com.fulfilltrack.FulfillTrack.auth.permisos.RolEntity;
import com.fulfilltrack.FulfillTrack.features.usuario.UsuarioEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "credenciales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CredencialEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy =
            jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean activo;

    @Column(name = "refresh_token", length = 2048, unique = true)
    private String refreshToken;

    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id_usuario", unique
            = true)
    private UsuarioEntity usuario;
    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinTable(
            name = "credentials_roles",
            joinColumns = @JoinColumn(name = "credential_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
)
    private Set<RolEntity> roles = new HashSet<>();
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        roles.forEach(rol ->
            rol.getPermisos().forEach(permiso ->
                authorities.add(new SimpleGrantedAuthority(permiso.getPermiso().name()))
            )
        );
        return authorities;
    }
    @Override
    public String getPassword() {
        return this.password;
    }
    @Override
    public String getUsername() {
        return this.username;
    }
    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(this.activo);
    }


}


