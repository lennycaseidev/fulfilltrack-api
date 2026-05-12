package com.fulfilltrack.FulfillTrack.features.credencial;

import com.fulfilltrack.FulfillTrack.features.credencial.dto.CredencialRequestDTO;
import com.fulfilltrack.FulfillTrack.features.credencial.dto.CredencialResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ICredencialService {
    CredencialResponseDTO registrarCredencial(CredencialRequestDTO request);
    CredencialResponseDTO obtenerCredencialPorUuid(UUID uuid);
    List<CredencialResponseDTO> obtenerCredenciales();
    CredencialResponseDTO asignarPermiso(UUID uuid, UUID permisoUuid);
    CredencialResponseDTO activarCredencial(UUID uuid);
    CredencialResponseDTO desactivarCredencial(UUID uuid);
}
