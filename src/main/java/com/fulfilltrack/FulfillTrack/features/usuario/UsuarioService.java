package com.fulfilltrack.FulfillTrack.features.usuario;

import com.fulfilltrack.FulfillTrack.common.exception.EntidadDuplicadaException;
import com.fulfilltrack.FulfillTrack.common.exception.EntidadNoEncontradaException;
import com.fulfilltrack.FulfillTrack.features.credencial.CredencialEntity;
import com.fulfilltrack.FulfillTrack.features.credencial.CredencialRepository;
import com.fulfilltrack.FulfillTrack.features.usuario.dto.UsuarioRequestDTO;
import com.fulfilltrack.FulfillTrack.features.usuario.dto.UsuarioResponseDTO;
import com.fulfilltrack.FulfillTrack.features.usuario.mapper.UsuarioMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public class UsuarioService implements IUsuarioService{
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final CredencialRepository credencialRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, CredencialRepository credencialRepository) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.credencialRepository = credencialRepository;
    }

    @Override
    public UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO request) {
        if (credencialRepository.existsByEmail(request.getEmail())) {
            throw new EntidadDuplicadaException("El email ya está en uso");
        }
        if (credencialRepository.existsByNombreUsuario(request.getNombreUsuario())) {
            throw new EntidadDuplicadaException("El nombre de usuario ya está en uso");
        }

        CredencialEntity credencial = CredencialEntity.builder().
                nombreUsuario(request.getNombreUsuario())
                .email(request.getEmail())
                .contrasena(request.getContrasena())
                .build();
        credencialRepository.save(credencial);

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
        List<UsuarioEntity> usuarios = usuarioRepository.findAll();
        return usuarioMapper.toResponseList(usuarios);
    }
}
