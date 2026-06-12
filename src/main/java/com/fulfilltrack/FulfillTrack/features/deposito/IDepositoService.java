package com.fulfilltrack.FulfillTrack.features.deposito;

import com.fulfilltrack.FulfillTrack.features.deposito.dto.DepositoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.deposito.dto.DepositoResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IDepositoService {
DepositoResponseDTO crearDeposito(DepositoRequestDTO request);
DepositoResponseDTO obtenerDepositoPorUuid(UUID uuid);
List<DepositoResponseDTO> obtenerDepositos();
DepositoResponseDTO actualizarDeposito(UUID uuid, DepositoRequestDTO request);
void desactivarDeposito(UUID uuid);
void activarDeposito(UUID uuid);
DepositoEntity obtenerDepositoUuid(UUID uuid);

}