package com.fulfilltrack.FulfillTrack.features.permiso;

import com.fulfilltrack.FulfillTrack.features.permiso.dto.PermisoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.permiso.dto.PermisoResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IPermisoService {
 List<PermisoResponseDTO> listarPermisos();
 PermisoResponseDTO crearPermiso(PermisoRequestDTO requestDTO);
 PermisoResponseDTO actualizarPermiso(UUID uuid, PermisoRequestDTO request);
 PermisoEntity obtenerPermisoPorUuid(UUID uuid);
}
