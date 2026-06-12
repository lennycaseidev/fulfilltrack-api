package com.fulfilltrack.FulfillTrack.features.puesto;

import com.fulfilltrack.FulfillTrack.features.puesto.dto.PuestoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.puesto.dto.PuestoResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IPuestoService {
    List<PuestoResponseDTO> listarPuestos();
    PuestoResponseDTO obtenerPuestoPorUuid(UUID uuid);
    PuestoResponseDTO actualizarPuesto(UUID uuid, PuestoRequestDTO request);
    void eliminarPuesto(UUID uuid);
    PuestoResponseDTO crearPuesto(PuestoRequestDTO request);
    PuestoEntity obtenerPuestoEntidad(UUID uuid);
}
