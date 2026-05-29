package com.fulfilltrack.FulfillTrack.features.usuario;

import com.fulfilltrack.FulfillTrack.features.usuario.dto.UsuarioRequestDTO;
import com.fulfilltrack.FulfillTrack.features.usuario.dto.UsuarioResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IUsuarioService {
    UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO request);
    UsuarioResponseDTO obtenerUsuarioPorUuid(UUID uuid);
    List<UsuarioResponseDTO> listarUsuarios();
    List<UsuarioResponseDTO> listarUsuariosActivos();
    UsuarioEntity obtenerUsuarioEntidad(UUID uuid);
}
