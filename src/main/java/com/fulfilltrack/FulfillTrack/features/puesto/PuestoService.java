package com.fulfilltrack.FulfillTrack.features.puesto;

import com.fulfilltrack.FulfillTrack.features.puesto.dto.PuestoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.puesto.dto.PuestoResponseDTO;

import java.util.List;
import java.util.UUID;

public class PuestoService implements IPuestoService{
    @Override
    public List<PuestoResponseDTO> listarpuestos() {
        return List.of();
    }

    @Override
    public PuestoResponseDTO obtenerPuestoPorUuid(UUID uuid) {
        return null;
    }

    @Override
    public PuestoResponseDTO actualizarPuesto(UUID uuid, PuestoRequestDTO request) {
        return null;
    }

    @Override
    public void eliminarPuesto(UUID uuid) {

    }

    @Override
    public PuestoResponseDTO crearPuesto(PuestoRequestDTO request) {
        return null;
    }
}
