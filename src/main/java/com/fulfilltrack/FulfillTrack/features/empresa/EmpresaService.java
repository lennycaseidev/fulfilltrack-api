package com.fulfilltrack.FulfillTrack.features.empresa;

import com.fulfilltrack.FulfillTrack.common.exception.EntidadDuplicadaException;
import com.fulfilltrack.FulfillTrack.common.exception.EntidadNoEncontradaException;
import com.fulfilltrack.FulfillTrack.common.exception.OperacionNoPermitidaException;
import com.fulfilltrack.FulfillTrack.common.utils.Estado;
import com.fulfilltrack.FulfillTrack.features.deposito.DepositoEntity;
import com.fulfilltrack.FulfillTrack.features.deposito.DepositoRepository;
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
    private final DepositoRepository depositoRepository;

    public EmpresaService(EmpresaRepository empresaRepository, EmpresaMapper empresaMapper, DepositoRepository depositoRepository) {
        this.empresaRepository = empresaRepository;
        this.empresaMapper = empresaMapper;
        this.depositoRepository = depositoRepository;
    }


    @Override
    public EmpresaResponseDTO crearEmpresa(EmpresaRequestDTO request) {
        if (empresaRepository.existsByEmail(request.getEmail())) {
            throw new EntidadDuplicadaException("Ya existe una empresa registrada con el email: " + request.getEmail());
        }
        DepositoEntity deposito = depositoRepository.findByUuid(request.getDepositoUuid())
                .orElseThrow(() -> new EntidadNoEncontradaException("Depósito no encontrado con UUID " + request.getDepositoUuid()));

        EmpresaEntity empresa = empresaMapper.toEntity(request);
        empresa.setDeposito(deposito);
        return empresaMapper.toResponseDTO(empresaRepository.save(empresa));
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
        DepositoEntity deposito = depositoRepository.findByUuid(request.getDepositoUuid())
                .orElseThrow(() -> new EntidadNoEncontradaException("Depósito no encontrado con UUID " + request.getDepositoUuid()));

        empresa.setNombreEmpresa(request.getNombreEmpresa());
        empresa.setEmail(request.getEmail());
        empresa.setCostoPorEnvio(request.getCostoPorEnvio());
        empresa.setDeposito(deposito);

        return empresaMapper.toResponseDTO(empresaRepository.save(empresa));
    }

    @Override
    public void desactivarEmpresa(UUID uuid) {
        EmpresaEntity empresa = empresaRepository.findByUuid(uuid)
                .orElseThrow(()->new EntidadNoEncontradaException("Empresa no encontrada"));

        if (empresa.getEstado() == Estado.INACTIVA) {
            throw new OperacionNoPermitidaException("La empresa ya se encuentra inactiva");
        }
        empresa.setEstado(Estado.INACTIVA);

        empresaRepository.save(empresa);
    }

    @Override
    public void activarEmpresa(UUID uuid) {
        EmpresaEntity empresa = empresaRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("Empresa no encontrada"));
        if(empresa.getEstado() == Estado.ACTIVA){
            throw new OperacionNoPermitidaException("La empresa ya se encuentra activada");
        }
        empresa.setEstado(Estado.ACTIVA);
        empresaRepository.save(empresa);
    }




}
