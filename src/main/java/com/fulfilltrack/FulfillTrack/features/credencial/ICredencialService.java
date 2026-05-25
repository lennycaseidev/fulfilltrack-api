package com.fulfilltrack.FulfillTrack.features.credencial;

import com.fulfilltrack.FulfillTrack.features.credencial.dto.CredencialRequestDTO;
import com.fulfilltrack.FulfillTrack.features.credencial.dto.CredencialResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ICredencialService {
    CredencialEntity registrarCredencial(String nombreUsuario, String email, String contrasena);
    CredencialResponseDTO obtenerCredencialPorUuid(UUID uuid);
    List<CredencialResponseDTO> obtenerCredenciales();
    CredencialResponseDTO asignarPermiso(UUID uuid, UUID permisoUuid);
    CredencialResponseDTO obtenerCredencialPorEmail(String email);
    void activarCredencial(UUID uuid);
    void desactivarCredencial(UUID uuid);
    CredencialEntity obtenerCredencialUuid(UUID uuid);
}
