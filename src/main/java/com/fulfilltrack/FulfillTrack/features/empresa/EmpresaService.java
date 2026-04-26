package com.fulfilltrack.FulfillTrack.features.empresa;

import com.fulfilltrack.FulfillTrack.features.empresa.dto.EmpresaRequestDTO;
import com.fulfilltrack.FulfillTrack.features.empresa.dto.EmpresaResponseDTO;
import com.fulfilltrack.FulfillTrack.features.empresa.mapper.EmpresaMapper;

import java.util.List;
import java.util.UUID;

public class EmpresaService implements IEmpresaService{
    private final EmpresaRepository empresaRepository;
    private final EmpresaMapper empresaMapper;

    public EmpresaService(EmpresaRepository empresaRepository, EmpresaMapper empresaMapper) {
        this.empresaRepository = empresaRepository;
        this.empresaMapper = empresaMapper;
    }


    @Override
    public EmpresaResponseDTO crearEmpresa(EmpresaRequestDTO request) {
        return null;
    }

    @Override
    public EmpresaResponseDTO obtenerEmpresaPorUuid(UUID uuid) {
        return null;
    }

    @Override
    public List<EmpresaResponseDTO> listarEmpresas() {
        return List.of();
    }

    @Override
    public EmpresaResponseDTO actualizarEmpresa(UUID uuid, EmpresaRequestDTO request) {
        return null;
    }

    @Override
    public void eliminarEmpresa(UUID uuid) {

    }
}
