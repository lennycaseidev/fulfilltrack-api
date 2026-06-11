package com.fulfilltrack.FulfillTrack.auth;

import com.fulfilltrack.FulfillTrack.auth.credenciales.CredencialRepository;
import com.fulfilltrack.FulfillTrack.features.usuarioEmpresa.UsuarioEmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TenantContext {

    private final CredencialRepository credencialRepository;
    private final UsuarioEmpresaRepository usuarioEmpresaRepository;

    public Optional<UUID> getEmpresaUuid() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return Optional.empty();
        return credencialRepository.findByUsername(auth.getName())
                .filter(c -> c.getUsuario() != null)
                .flatMap(c -> usuarioEmpresaRepository.findByUsuario(c.getUsuario()))
                .map(ue -> ue.getEmpresa().getUuid());
    }
}