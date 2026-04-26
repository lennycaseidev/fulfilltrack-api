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
        EmpresaEntity empresa = empresaMapper.toEntity(request);
        EmpresaEntity saved = empresaRepository.save(empresa);
        return empresaMapper.toResponseDTO(saved);
    }

    @Override
    public EmpresaResponseDTO obtenerEmpresaPorUuid(UUID uuid) {
        EmpresaEntity empresa = empresaRepository.findByUuid(uuid)
                .orElseThrow(()->new RuntimeException("Empresa no encontrada"));
        return empresaMapper.toResponseDTO(empresa);
    }

    @Override
    public List<EmpresaResponseDTO> listarEmpresas() {
       List<EmpresaEntity> empresas = empresaRepository.findAll();
        return empresaMapper.toResponseList(empresas);
    }

    @Override
    public EmpresaResponseDTO actualizarEmpresa(UUID uuid, EmpresaRequestDTO request) {
        EmpresaEntity empresa = empresaRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));
        empresa.setNombreEmpresa(request.getNombreEmpresa());
        empresa.setEmail(request.getEmail());
        empresa.setCostoPorEnvio(request.getCostoPorEnvio());

        EmpresaEntity empresaActualizada = empresaRepository.save(empresa);
        return empresaMapper.toResponseDTO(empresaActualizada);
    }

    @Override
    public void desactivarEmpresa(UUID uuid) {
        EmpresaEntity empresa = empresaRepository.findByUuid(uuid)
                .orElseThrow(()->new RuntimeException("Empresa no encontrada"));

        if (empresa.getEstado() == EstadoEmpresa.INACTIVA) {
            throw new RuntimeException("La empresa ya se encuentra inactiva");
        }
        empresa.setEstado(EstadoEmpresa.INACTIVA);

        empresaRepository.save(empresa);
    }

}
