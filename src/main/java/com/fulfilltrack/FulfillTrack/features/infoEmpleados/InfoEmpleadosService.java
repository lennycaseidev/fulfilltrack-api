package com.fulfilltrack.FulfillTrack.features.infoEmpleados;

import com.fulfilltrack.FulfillTrack.common.exception.EntidadDuplicadaException;
import com.fulfilltrack.FulfillTrack.common.exception.EntidadNoEncontradaException;
import com.fulfilltrack.FulfillTrack.features.deposito.DepositoEntity;
import com.fulfilltrack.FulfillTrack.features.deposito.DepositoRepository;
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
    private final DepositoRepository depositoRepository;
//    private final PuestoRepository puestoRepository;
    //private final UsuarioRepository usuarioRepository;

    public InfoEmpleadosService(InfoEmpleadosRepository infoEmpleadosRepository, InfoEmpleadosMapper infoEmpleadosMapper, DepositoRepository depositoRepository) {
        this.infoEmpleadosRepository = infoEmpleadosRepository;
        this.infoEmpleadosMapper = infoEmpleadosMapper;
        this.depositoRepository = depositoRepository;
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
        if(infoEmpleadosRepository.existsByDocumento(request.getDocumento())){
            throw new EntidadDuplicadaException("El documento ya esta registrado");
        }
        DepositoEntity deposito = depositoRepository.findByUuid(request.getDepositoUuid())
                .orElseThrow(() -> new EntidadNoEncontradaException("El depósito no ha sido encontrado"));

//        PuestoEntity puesto = puestoRepository.findByUuid(request.getPuestoUuid())
//                .orElseThrow(() -> new EntidadNoEncontradaException("El puesto no ha sido encontrado"));
//        UsuarioEntity usuario = usuarioRepository.findByUuid(request.getUsuarioUuid())
//                .orElseThrow(()-> new EntidadNoEncontradaException("El usuario no ha sido encontrado"));

        InfoEmpleadosEntity empleado = InfoEmpleadosEntity.builder()
                .documento(request.getDocumento())
                .salario(request.getSalario())
                .fechaContratacion(request.getFechaContratacion())
                .deposito(deposito)
                .build();

        return infoEmpleadosMapper.toResponseDTO(infoEmpleadosRepository.save(empleado));
    }

    @Override
    public InfoEmpleadosResponseDTO actualizarEmpleado(UUID uuid, InfoEmpleadosRequestDTO request) {
        InfoEmpleadosEntity empleado = infoEmpleadosRepository.findByUuid(uuid)
                .orElseThrow(()-> new EntidadNoEncontradaException("El empleado no ha sido encontrado"));
        DepositoEntity deposito = depositoRepository.findByUuid(request.getDepositoUuid())
                .orElseThrow(() -> new EntidadNoEncontradaException("El depósito no ha sido encontrado"));

//        PuestoEntity puesto = puestoRepository.findByUuid(request.getPuestoUuid())
//                .orElseThrow(() -> new EntidadNoEncontradaException("El puesto no ha sido encontrado"));

        empleado.setSalario(request.getSalario());
        empleado.setFechaContratacion(request.getFechaContratacion());
        empleado.setDeposito(deposito);
//        empleado.setPuesto(puesto);

        return infoEmpleadosMapper.toResponseDTO(infoEmpleadosRepository.save(empleado));
    }
}
