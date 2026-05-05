package com.fulfilltrack.FulfillTrack.features.infoEmpleados;

import com.fulfilltrack.FulfillTrack.features.infoEmpleados.dto.InfoEmpleadosRequestDTO;
import com.fulfilltrack.FulfillTrack.features.infoEmpleados.dto.InfoEmpleadosResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IInfoEmpleadosService {
    List<InfoEmpleadosResponseDTO> listarEmpleados();
    InfoEmpleadosResponseDTO obtenerEmpleadoPorUuid(UUID uuid);
    InfoEmpleadosResponseDTO crearEmpleado(InfoEmpleadosRequestDTO request);
    InfoEmpleadosResponseDTO actualizarEmpleado(UUID uuid, InfoEmpleadosRequestDTO request);
    void activarEmpleado(UUID uuid);
    void desactivarEmpleado(UUID uuid);



}
