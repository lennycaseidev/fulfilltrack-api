package com.fulfilltrack.FulfillTrack.features.usuarioEmpresa;

import com.fulfilltrack.FulfillTrack.common.exception.EntidadNoEncontradaException;
import com.fulfilltrack.FulfillTrack.common.exception.OperacionNoPermitidaException;
import com.fulfilltrack.FulfillTrack.common.utils.Estado;
import com.fulfilltrack.FulfillTrack.features.empresa.EmpresaEntity;
import com.fulfilltrack.FulfillTrack.features.empresa.EmpresaRepository;
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
    private final EmpresaRepository empresaRepository;

    public UsuarioEmpresaService(UsuarioEmpresaRepository usuarioEmpresaRepository,
                                 UsuarioEmpresaMapper usuarioEmpresaMapper,
                                 EmpresaRepository empresaRepository) {
        this.usuarioEmpresaRepository = usuarioEmpresaRepository;
        this.usuarioEmpresaMapper = usuarioEmpresaMapper;
        this.empresaRepository = empresaRepository;
    }

    @Override
    public List<UsuarioEmpresaResponseDTO> listarUsuariosEmpresa() {
        return usuarioEmpresaMapper.toResponseList(usuarioEmpresaRepository.findAll());
    }

    @Override
    public List<UsuarioEmpresaResponseDTO> listarUsuariosPorEmpresa(UUID empresaUuid) {
        if (!empresaRepository.existsByUuid(empresaUuid)) {
            throw new EntidadNoEncontradaException("Empresa no encontrada con UUID " + empresaUuid);
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
        EmpresaEntity empresa = empresaRepository.findByUuid(request.getEmpresaUuid())
                .orElseThrow(() -> new EntidadNoEncontradaException("Empresa no encontrada con UUID " + request.getEmpresaUuid()));

        UsuarioEmpresaEntity usuarioEmpresa = usuarioEmpresaMapper.toEntity(request);
        usuarioEmpresa.setEmpresa(empresa);

        return usuarioEmpresaMapper.toResponseDTO(usuarioEmpresaRepository.save(usuarioEmpresa));
    }

    @Override
    public UsuarioEmpresaResponseDTO actualizarUsuarioEmpresa(UUID uuid, UsuarioEmpresaRequestDTO request) {
        UsuarioEmpresaEntity usuarioEmpresa = usuarioEmpresaRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("Usuario empresa no encontrado con UUID " + uuid));
        EmpresaEntity empresa = empresaRepository.findByUuid(request.getEmpresaUuid())
                .orElseThrow(() -> new EntidadNoEncontradaException("Empresa no encontrada con UUID " + request.getEmpresaUuid()));

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
    }
}