package com.fulfilltrack.FulfillTrack.features.empresa;

import com.fulfilltrack.FulfillTrack.features.empresa.dto.EmpresaRequestDTO;
import com.fulfilltrack.FulfillTrack.features.empresa.dto.EmpresaResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IEmpresaService {
    EmpresaResponseDTO crearEmpresa(EmpresaRequestDTO request);

    EmpresaResponseDTO obtenerEmpresaPorUuid(UUID uuid);

    List<EmpresaResponseDTO> listarEmpresas();

    EmpresaResponseDTO actualizarEmpresa(UUID uuid, EmpresaRequestDTO request);

    void desactivarEmpresa(UUID uuid);

    void activarEmpresa(UUID uuid);
}
