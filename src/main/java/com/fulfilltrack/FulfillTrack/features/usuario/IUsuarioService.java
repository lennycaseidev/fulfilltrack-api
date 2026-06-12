package com.fulfilltrack.FulfillTrack.features.usuario;

import com.fulfilltrack.FulfillTrack.auth.dto.NewAccountRequest;
import com.fulfilltrack.FulfillTrack.auth.permisos.Roles;
import com.fulfilltrack.FulfillTrack.features.usuario.dto.CambiarPasswordDTO;
import com.fulfilltrack.FulfillTrack.features.usuario.dto.UsuarioResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IUsuarioService {
    UsuarioResponseDTO guardar(NewAccountRequest newAccountRequest);
    UsuarioResponseDTO obtenerUsuarioPorUuid(UUID uuid);
    List<UsuarioResponseDTO> listarUsuarios();
    List<UsuarioResponseDTO> listarUsuariosActivos();
    UsuarioEntity obtenerUsuarioEntidad(UUID uuid);
    void asignarRol(UUID usuarioUuid, Roles rol);
    void cambiarPassword(UUID usuarioUuid, CambiarPasswordDTO dto);
}
