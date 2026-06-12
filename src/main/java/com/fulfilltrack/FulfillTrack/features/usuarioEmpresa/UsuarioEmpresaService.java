package com.fulfilltrack.FulfillTrack.features.usuarioEmpresa;

import com.fulfilltrack.FulfillTrack.auth.credenciales.CredencialRepository;
import com.fulfilltrack.FulfillTrack.common.exception.EntidadDuplicadaException;
import com.fulfilltrack.FulfillTrack.common.exception.EntidadNoEncontradaException;
import com.fulfilltrack.FulfillTrack.common.exception.OperacionNoPermitidaException;
import com.fulfilltrack.FulfillTrack.common.utils.Estado;
import com.fulfilltrack.FulfillTrack.features.empresa.EmpresaEntity;
import com.fulfilltrack.FulfillTrack.features.empresa.IEmpresaService;
import com.fulfilltrack.FulfillTrack.features.infoEmpleados.IInfoEmpleadosService;
import com.fulfilltrack.FulfillTrack.features.usuario.IUsuarioService;
import com.fulfilltrack.FulfillTrack.features.usuario.UsuarioEntity;
import com.fulfilltrack.FulfillTrack.features.usuarioEmpresa.dto.UsuarioEmpresaRequestDTO;
import com.fulfilltrack.FulfillTrack.features.usuarioEmpresa.dto.UsuarioEmpresaResponseDTO;
import com.fulfilltrack.FulfillTrack.features.usuarioEmpresa.mapper.UsuarioEmpresaMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioEmpresaService implements IUsuarioEmpresaService {

    private final UsuarioEmpresaRepository usuarioEmpresaRepository;
    private final UsuarioEmpresaMapper usuarioEmpresaMapper;
    private final IEmpresaService empresaService;
    private final IInfoEmpleadosService infoEmpleadosService;
    private final IUsuarioService usuarioService;
    private final CredencialRepository credencialRepository;

    public UsuarioEmpresaService(UsuarioEmpresaRepository usuarioEmpresaRepository, UsuarioEmpresaMapper usuarioEmpresaMapper, IEmpresaService empresaService, IInfoEmpleadosService infoEmpleadosService, IUsuarioService usuarioService, CredencialRepository credencialRepository) {
        this.usuarioEmpresaRepository = usuarioEmpresaRepository;
        this.usuarioEmpresaMapper = usuarioEmpresaMapper;
        this.empresaService = empresaService;
        this.infoEmpleadosService = infoEmpleadosService;
        this.usuarioService = usuarioService;
        this.credencialRepository = credencialRepository;
    }

    @Override
    public List<UsuarioEmpresaResponseDTO> listarUsuariosEmpresa() {
        return usuarioEmpresaMapper.toResponseList(usuarioEmpresaRepository.findAll());
    }

    @Override
    public List<UsuarioEmpresaResponseDTO> listarUsuariosPorEmpresa(UUID empresaUuid) {
        if (!empresaService.existeEmpresaPorUuid(empresaUuid)) {
            throw new EntidadNoEncontradaException("Empresa no encontrada");
        }
        return usuarioEmpresaMapper.toResponseList(usuarioEmpresaRepository.findByEmpresa_Uuid(empresaUuid));
    }

    @Override
    public UsuarioEmpresaResponseDTO obtenerUsuarioEmpresaPorUuid(UUID uuid) {
        UsuarioEmpresaEntity usuarioEmpresa = usuarioEmpresaRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("Usuario empresa no encontrado con UUID " + uuid));
        return usuarioEmpresaMapper.toResponseDTO(usuarioEmpresa);
    }

    @Override
    public UsuarioEmpresaResponseDTO crearUsuarioEmpresa(UsuarioEmpresaRequestDTO request) {
        if (usuarioEmpresaRepository.existsByUsuario_Uuid(request.getUsuarioUuid())) {
            throw new EntidadDuplicadaException("El usuario ya está vinculado a una empresa");
        }
        if (infoEmpleadosService.existeEmpleadoPorUsuario(request.getUsuarioUuid())) {
            throw new OperacionNoPermitidaException("El usuario ya está registrado como empleado");
        }

        EmpresaEntity empresa = empresaService.obtenerEmpresaUuid(request.getEmpresaUuid());
        UsuarioEntity usuario = usuarioService.obtenerUsuarioEntidad(request.getUsuarioUuid());

        UsuarioEmpresaEntity usuarioEmpresa = usuarioEmpresaMapper.toEntity(request);
        usuarioEmpresa.setEmpresa(empresa);
        usuarioEmpresa.setUsuario(usuario);

        return usuarioEmpresaMapper.toResponseDTO(usuarioEmpresaRepository.save(usuarioEmpresa));
    }

    @Override
    public UsuarioEmpresaResponseDTO actualizarUsuarioEmpresa(UUID uuid, UsuarioEmpresaRequestDTO request) {
        UsuarioEmpresaEntity usuarioEmpresa = usuarioEmpresaRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("Usuario empresa no encontrado con UUID " + uuid));
        EmpresaEntity empresa = empresaService.obtenerEmpresaUuid(request.getEmpresaUuid());

        usuarioEmpresa.setCargo(request.getCargo());
        usuarioEmpresa.setTelefono(request.getTelefono());
        usuarioEmpresa.setEmpresa(empresa);

        return usuarioEmpresaMapper.toResponseDTO(usuarioEmpresaRepository.save(usuarioEmpresa));
    }

    @Override
    public void activarUsuarioEmpresa(UUID uuid) {
        UsuarioEmpresaEntity usuarioEmpresa = usuarioEmpresaRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("Usuario empresa no encontrado con UUID " + uuid));
        if (usuarioEmpresa.getEstado() == Estado.ACTIVA) {
            throw new OperacionNoPermitidaException("El usuario ya se encuentra activo");
        }
        usuarioEmpresa.setEstado(Estado.ACTIVA);
        usuarioEmpresaRepository.save(usuarioEmpresa);
        credencialRepository.findByUsuario(usuarioEmpresa.getUsuario()).ifPresent(credencial -> {
            credencial.setActivo(true);
            credencialRepository.save(credencial);
        });
    }

    @Override
    public void desactivarUsuarioEmpresa(UUID uuid) {
        UsuarioEmpresaEntity usuarioEmpresa = usuarioEmpresaRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("Usuario empresa no encontrado con UUID " + uuid));
        if (usuarioEmpresa.getEstado() == Estado.INACTIVA) {
            throw new OperacionNoPermitidaException("El usuario ya se encuentra inactivo");
        }
        usuarioEmpresa.setEstado(Estado.INACTIVA);
        usuarioEmpresaRepository.save(usuarioEmpresa);
        credencialRepository.findByUsuario(usuarioEmpresa.getUsuario()).ifPresent(credencial -> {
            credencial.setActivo(false);
            credencial.setRefreshToken(null);
            credencialRepository.save(credencial);
        });
    }

    @Override
    public boolean existeUsuarioEmpresa(UUID uuid){
        return usuarioEmpresaRepository.existsByUsuario_Uuid(uuid);
    }


}