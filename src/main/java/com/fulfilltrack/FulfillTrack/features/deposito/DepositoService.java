package com.fulfilltrack.FulfillTrack.features.deposito;

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
        return null;
    }

    @Override
    public DepositoResponseDTO obtenerDepositoPorUuid(UUID uuid) {
        return null;
    }

    @Override
    public List<DepositoResponseDTO> obtenerDepositos() {
        return List.of();
    }

    @Override
    public DepositoResponseDTO actualizarDeposito(UUID uuid, DepositoRequestDTO request) {
        return null;
    }

    @Override
    public void desactivarDeposito(UUID uuid) {

    }

    @Override
    public void activarDeposito(UUID uuid) {

    }
}