package com.fulfilltrack.FulfillTrack.features.usuarioEmpresa;

import com.fulfilltrack.FulfillTrack.features.usuarioEmpresa.dto.UsuarioEmpresaRequestDTO;
import com.fulfilltrack.FulfillTrack.features.usuarioEmpresa.dto.UsuarioEmpresaResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IUsuarioEmpresaService {
    List<UsuarioEmpresaResponseDTO> listarUsuariosEmpresa();
    List<UsuarioEmpresaResponseDTO> listarUsuariosPorEmpresa(UUID empresaUuid);
    UsuarioEmpresaResponseDTO obtenerUsuarioEmpresaPorUuid(UUID uuid);
    UsuarioEmpresaResponseDTO crearUsuarioEmpresa(UsuarioEmpresaRequestDTO request);
    UsuarioEmpresaResponseDTO actualizarUsuarioEmpresa(UUID uuid, UsuarioEmpresaRequestDTO request);
    void activarUsuarioEmpresa(UUID uuid);
    void desactivarUsuarioEmpresa(UUID uuid);
    boolean existeUsuarioEmpresa(UUID uuid);
}