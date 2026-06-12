package com.fulfilltrack.FulfillTrack.features.deposito;

import com.fulfilltrack.FulfillTrack.common.exception.EntidadDuplicadaException;
import com.fulfilltrack.FulfillTrack.common.exception.EntidadNoEncontradaException;
import com.fulfilltrack.FulfillTrack.common.exception.OperacionNoPermitidaException;
import com.fulfilltrack.FulfillTrack.common.utils.Estado;
import com.fulfilltrack.FulfillTrack.features.deposito.dto.DepositoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.deposito.dto.DepositoResponseDTO;
import com.fulfilltrack.FulfillTrack.features.deposito.mapper.DepositoMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DepositoService implements IDepositoService {

    private final DepositoRepository depositoRepository;
    private final DepositoMapper depositoMapper;

    public DepositoService(DepositoRepository depositoRepository, DepositoMapper depositoMapper) {
        this.depositoRepository = depositoRepository;
        this.depositoMapper = depositoMapper;
    }

    @Override
    public DepositoResponseDTO crearDeposito(DepositoRequestDTO request) {
        if(depositoRepository.existsByEmail(request.getEmail())){
            throw new EntidadDuplicadaException("Ya existe un deposito registrado con el email " + request.getEmail());
        }
        if((!request.getAperturaDeposito().isBefore(request.getCierreDeposito()))){
            throw new OperacionNoPermitidaException("El horario de cierre debe ser mayor al de apertura");
        }
        DepositoEntity deposito = depositoMapper.toEntity(request);
        return depositoMapper.toResponseDTO(depositoRepository.save(deposito));
    }

    @Override
    public DepositoResponseDTO obtenerDepositoPorUuid(UUID uuid) {
        DepositoEntity deposito = obtenerDepositoUuid(uuid);
        return depositoMapper.toResponseDTO(deposito);
    }

    @Override
    public List<DepositoResponseDTO> obtenerDepositos() {
        List<DepositoEntity> depositos= depositoRepository.findAll();
        return depositoMapper.toResponseList(depositos);
    }

    @Override
    public DepositoResponseDTO actualizarDeposito(UUID uuid, DepositoRequestDTO request) {
        DepositoEntity deposito = obtenerDepositoUuid(uuid);
        if((!request.getAperturaDeposito().isBefore(request.getCierreDeposito()))){
            throw new OperacionNoPermitidaException("El horario de cierre debe ser mayor al de apertura");
        }
        if(depositoRepository.existsByEmailAndUuidNot(request.getEmail(),uuid)){
            throw new EntidadDuplicadaException("Ya existe otro deposito registrado con el email " + request.getEmail());
        }
        deposito.setNombreDeposito(request.getNombreDeposito());
        deposito.setEmail(request.getEmail());
        deposito.setDireccionDeposito(request.getDireccionDeposito());
        deposito.setTelefonoDeposito(request.getTelefonoDeposito());
        deposito.setAperturaDeposito(request.getAperturaDeposito());
        deposito.setCierreDeposito(request.getCierreDeposito());

        return depositoMapper.toResponseDTO(depositoRepository.save(deposito));
    }

    @Override
    public void desactivarDeposito(UUID uuid) {
        DepositoEntity deposito = obtenerDepositoUuid(uuid);
        if (deposito.getEstado() == Estado.INACTIVA) {
            throw new OperacionNoPermitidaException("El depósito ya se encuentra inactivo");
        }
        deposito.setEstado(Estado.INACTIVA);
        depositoRepository.save(deposito);
    }

    @Override
    public void activarDeposito(UUID uuid) {
        DepositoEntity deposito = obtenerDepositoUuid(uuid);
        if (deposito.getEstado() == Estado.ACTIVA) {
            throw new OperacionNoPermitidaException("El depósito ya se encuentra activo");
        }
        deposito.setEstado(Estado.ACTIVA);
        depositoRepository.save(deposito);
    }

    @Override
    public DepositoEntity obtenerDepositoUuid(UUID uuid) {
        return depositoRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("Depósito no encontrado"));
    }
}