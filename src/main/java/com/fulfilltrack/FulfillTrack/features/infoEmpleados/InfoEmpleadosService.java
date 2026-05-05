package com.fulfilltrack.FulfillTrack.features.infoEmpleados;

import com.fulfilltrack.FulfillTrack.common.exception.EntidadNoEncontradaException;
import com.fulfilltrack.FulfillTrack.features.infoEmpleados.dto.InfoEmpleadosRequestDTO;
import com.fulfilltrack.FulfillTrack.features.infoEmpleados.dto.InfoEmpleadosResponseDTO;
import com.fulfilltrack.FulfillTrack.features.infoEmpleados.mapper.InfoEmpleadosMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public class InfoEmpleadosService implements IInfoEmpleadosService{
    private final InfoEmpleadosRepository infoEmpleadosRepository;
    private final InfoEmpleadosMapper infoEmpleadosMapper;
    public InfoEmpleadosService(InfoEmpleadosRepository infoEmpleadosRepository, InfoEmpleadosMapper infoEmpleadosMapper) {
        this.infoEmpleadosRepository = infoEmpleadosRepository;
        this.infoEmpleadosMapper = infoEmpleadosMapper;
    }

    @Override
    public List<InfoEmpleadosResponseDTO> listarEmpleados() {
        List<InfoEmpleadosEntity> empleados = infoEmpleadosRepository.findAll();
        return infoEmpleadosMapper.toResponseList(empleados);
    }

    @Override
    public InfoEmpleadosResponseDTO obtenerEmpleadoPorUuid(UUID uuid) {
        InfoEmpleadosEntity empleado = infoEmpleadosRepository.findByUuid(uuid)
                .orElseThrow(()-> new EntidadNoEncontradaException("El empleado no ha sido encontrado"));
        return infoEmpleadosMapper.toResponseDTO(empleado);
    }

    @Override
    public InfoEmpleadosResponseDTO crearEmpleado(InfoEmpleadosRequestDTO request) {
        if(infoEmpleadosRepository.existsByDocumento())
        return null;
    }

    @Override
    public InfoEmpleadosResponseDTO actualizarEmpleado(UUID uuid, InfoEmpleadosRequestDTO request) {
        return null;
    }
}
