package com.fulfilltrack.FulfillTrack.features.empresa;

import com.fulfilltrack.FulfillTrack.common.exception.EntidadDuplicadaException;
import com.fulfilltrack.FulfillTrack.common.exception.EntidadNoEncontradaException;
import com.fulfilltrack.FulfillTrack.common.exception.OperacionNoPermitidaException;
import com.fulfilltrack.FulfillTrack.features.empresa.dto.EmpresaRequestDTO;
import com.fulfilltrack.FulfillTrack.features.empresa.dto.EmpresaResponseDTO;
import com.fulfilltrack.FulfillTrack.features.empresa.mapper.EmpresaMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmpresaService implements IEmpresaService{
    private final EmpresaRepository empresaRepository;
    private final EmpresaMapper empresaMapper;

    public EmpresaService(EmpresaRepository empresaRepository, EmpresaMapper empresaMapper) {
        this.empresaRepository = empresaRepository;
        this.empresaMapper = empresaMapper;
    }


    @Override
    public EmpresaResponseDTO crearEmpresa(EmpresaRequestDTO request) {
        if (empresaRepository.existsByEmail(request.getEmail())) {
            throw new EntidadDuplicadaException("Ya existe una empresa registrada con el email: " + request.getEmail());
        }
        EmpresaEntity empresa = empresaMapper.toEntity(request);
        EmpresaEntity saved = empresaRepository.save(empresa);
        return empresaMapper.toResponseDTO(saved);
    }

    @Override
    public EmpresaResponseDTO obtenerEmpresaPorUuid(UUID uuid) {
        EmpresaEntity empresa = empresaRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("Empresa no encontrada con UUID: " + uuid));
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
                .orElseThrow(() -> new EntidadNoEncontradaException("Empresa no encontrada"));
        if (empresaRepository.existsByEmailAndUuidNot(request.getEmail(), uuid)) {
            throw new EntidadDuplicadaException("Ya existe otra empresa registrada con el email: " + request.getEmail());
        }
        empresa.setNombreEmpresa(request.getNombreEmpresa());
        empresa.setEmail(request.getEmail());
        empresa.setCostoPorEnvio(request.getCostoPorEnvio());

        EmpresaEntity empresaActualizada = empresaRepository.save(empresa);
        return empresaMapper.toResponseDTO(empresaActualizada);
    }

    @Override
    public void desactivarEmpresa(UUID uuid) {
        EmpresaEntity empresa = empresaRepository.findByUuid(uuid)
                .orElseThrow(()->new EntidadNoEncontradaException("Empresa no encontrada"));

        if (empresa.getEstado() == EstadoEmpresa.INACTIVA) {
            throw new OperacionNoPermitidaException("La empresa ya se encuentra inactiva");
        }
        empresa.setEstado(EstadoEmpresa.INACTIVA);

        empresaRepository.save(empresa);
    }

    @Override
    public void activarEmpresa(UUID uuid) {
        EmpresaEntity empresa = empresaRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("Empresa no encontrada"));
        if(empresa.getEstado() == EstadoEmpresa.ACTIVA){
            throw new OperacionNoPermitidaException("La empresa ya se encuentra activada");
        }
        empresa.setEstado(EstadoEmpresa.ACTIVA);
        empresaRepository.save(empresa);
    }




}
