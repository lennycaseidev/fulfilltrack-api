package com.fulfilltrack.FulfillTrack.features.usuario;

import com.fulfilltrack.FulfillTrack.common.exception.EntidadNoEncontradaException;
import com.fulfilltrack.FulfillTrack.common.utils.Estado;
import com.fulfilltrack.FulfillTrack.features.credencial.CredencialEntity;
import com.fulfilltrack.FulfillTrack.features.credencial.ICredencialService;
import com.fulfilltrack.FulfillTrack.features.usuario.dto.UsuarioRequestDTO;
import com.fulfilltrack.FulfillTrack.features.usuario.dto.UsuarioResponseDTO;
import com.fulfilltrack.FulfillTrack.features.usuario.mapper.UsuarioMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
@Service
public class UsuarioService implements IUsuarioService{
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final ICredencialService credencialService;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, ICredencialService credencialService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.credencialService = credencialService;
    }

    @Override
    @Transactional
   public UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO request) {
        CredencialEntity credencial = credencialService.registrarCredencial(request.getNombreUsuario(), request.getEmail(), request.getContrasena());

        UsuarioEntity usuario = UsuarioEntity.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .credencial(credencial)
                .build();

        return usuarioMapper.toResponseDTO(usuarioRepository.save(usuario));
    }

    @Override
    public UsuarioResponseDTO obtenerUsuarioPorUuid(UUID uuid) {
        UsuarioEntity usuario = usuarioRepository.findByUuid(uuid)
                .orElseThrow(()-> new EntidadNoEncontradaException("El usuario no se ha encontrado"));
        return usuarioMapper.toResponseDTO(usuario);
    }

    @Override
    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioMapper.toResponseList(usuarioRepository.findAll());
    }

    @Override
    public List<UsuarioResponseDTO> listarUsuariosActivos() {
        return usuarioMapper.toResponseList(usuarioRepository.findByCredencial_Estado(Estado.ACTIVA));
    }

    @Override
    public UsuarioEntity obtenerUsuarioEntidad(UUID uuid) {
        return usuarioRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("El usuario no se ha encontrado"));
    }
}
