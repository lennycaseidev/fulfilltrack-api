package com.fulfilltrack.FulfillTrack.features.infoEmpleados;

import com.fulfilltrack.FulfillTrack.common.exception.EntidadDuplicadaException;
import com.fulfilltrack.FulfillTrack.common.exception.EntidadNoEncontradaException;
import com.fulfilltrack.FulfillTrack.common.exception.OperacionNoPermitidaException;
import com.fulfilltrack.FulfillTrack.common.utils.Estado;
import com.fulfilltrack.FulfillTrack.features.deposito.DepositoEntity;
import com.fulfilltrack.FulfillTrack.features.deposito.IDepositoService;
import com.fulfilltrack.FulfillTrack.features.infoEmpleados.dto.InfoEmpleadosRequestDTO;
import com.fulfilltrack.FulfillTrack.features.infoEmpleados.dto.InfoEmpleadosResponseDTO;
import com.fulfilltrack.FulfillTrack.features.infoEmpleados.mapper.InfoEmpleadosMapper;
import com.fulfilltrack.FulfillTrack.features.puesto.IPuestoService;
import com.fulfilltrack.FulfillTrack.features.puesto.PuestoEntity;
import com.fulfilltrack.FulfillTrack.features.usuario.IUsuarioService;
import com.fulfilltrack.FulfillTrack.features.usuario.UsuarioEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public class InfoEmpleadosService implements IInfoEmpleadosService{
    private final InfoEmpleadosRepository infoEmpleadosRepository;
    private final InfoEmpleadosMapper infoEmpleadosMapper;
    private final IDepositoService depositoService;
    private final IPuestoService puestoService;
    private final IUsuarioService usuarioService;

    public InfoEmpleadosService(InfoEmpleadosRepository infoEmpleadosRepository, InfoEmpleadosMapper infoEmpleadosMapper, IDepositoService depositoService, IPuestoService puestoService, IUsuarioService usuarioService) {
        this.infoEmpleadosRepository = infoEmpleadosRepository;
        this.infoEmpleadosMapper = infoEmpleadosMapper;
        this.depositoService = depositoService;
        this.puestoService = puestoService;
        this.usuarioService = usuarioService;
    }

    @Override
    public List<InfoEmpleadosResponseDTO> listarEmpleados() {
        List<InfoEmpleadosEntity> empleados = infoEmpleadosRepository.findAll();
        return infoEmpleadosMapper.toResponseList(empleados);
    }

    @Override
    public InfoEmpleadosResponseDTO obtenerEmpleadoPorUuid(UUID uuid) {
        InfoEmpleadosEntity empleado = obtenerEmpleadoUuid(uuid);
        return infoEmpleadosMapper.toResponseDTO(empleado);
    }

    @Override
    public InfoEmpleadosResponseDTO crearEmpleado(InfoEmpleadosRequestDTO request) {
        if(infoEmpleadosRepository.existsByDocumento(request.getDocumento())){
            throw new EntidadDuplicadaException("El documento ya esta registrado");
        }
        DepositoEntity deposito = depositoService.obtenerDepositoUuid(request.getDepositoUuid());

        PuestoEntity puesto = puestoRepository.findByUuid(request.getPuestoUuid())
                .orElseThrow(() -> new EntidadNoEncontradaException("El puesto no ha sido encontrado"));
        UsuarioEntity usuario = usuarioRepository.findByUuid(request.getUsuarioUuid())
                .orElseThrow(()-> new EntidadNoEncontradaException("El usuario no ha sido encontrado"));

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
        InfoEmpleadosEntity empleado = obtenerEmpleadoUuid(uuid);
        DepositoEntity deposito = depositoService.obtenerDepositoUuid(request.getDepositoUuid());


//        PuestoEntity puesto = puestoRepository.findByUuid(request.getPuestoUuid())
//                .orElseThrow(() -> new EntidadNoEncontradaException("El puesto no ha sido encontrado"));

        empleado.setSalario(request.getSalario());
        empleado.setFechaContratacion(request.getFechaContratacion());
        empleado.setDeposito(deposito);
//        empleado.setPuesto(puesto);

        return infoEmpleadosMapper.toResponseDTO(infoEmpleadosRepository.save(empleado));
    }

    @Override
    public void activarEmpleado(UUID uuid) {
        InfoEmpleadosEntity empleado = obtenerEmpleadoUuid(uuid);
        if (empleado.getEstado() == Estado.ACTIVA) {
            throw new OperacionNoPermitidaException("El empleado ya está activo");
        }
        empleado.setEstado(Estado.ACTIVA);
        infoEmpleadosRepository.save(empleado);
    }

    @Override
    public void desactivarEmpleado(UUID uuid) {
        InfoEmpleadosEntity empleado = obtenerEmpleadoUuid(uuid);
        if (empleado.getEstado() == Estado.INACTIVA) {
            throw new OperacionNoPermitidaException("El empleado ya está inactivo");
        }
        empleado.setEstado(Estado.INACTIVA);
        infoEmpleadosRepository.save(empleado);
    }

    @Override
    public InfoEmpleadosEntity obtenerEmpleadoUuid(UUID uuid) {
        return infoEmpleadosRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("El empleado no ha sido encontrado"));
    }
}
