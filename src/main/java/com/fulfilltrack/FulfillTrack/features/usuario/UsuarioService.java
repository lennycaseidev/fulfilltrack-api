package com.fulfilltrack.FulfillTrack.features.usuario;

import com.fulfilltrack.FulfillTrack.features.usuario.dto.UsuarioRequestDTO;
import com.fulfilltrack.FulfillTrack.features.usuario.dto.UsuarioResponseDTO;

import java.util.List;
import java.util.UUID;

public class UsuarioService implements IUsuarioService{
    @Override
    public UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO request) {
        return null;
    }

    @Override
    public UsuarioResponseDTO obtenerUsuarioPorUuid(UUID uuid) {
        return null;
    }

    @Override
    public List<UsuarioResponseDTO> listarUsuarios() {
        return List.of();
    }
}
