package com.fulfilltrack.FulfillTrack.features.usuario;

import com.fulfilltrack.FulfillTrack.auth.credenciales.CredencialEntity;
import com.fulfilltrack.FulfillTrack.auth.credenciales.CredencialRepository;
import com.fulfilltrack.FulfillTrack.auth.dto.NewAccountRequest;
import com.fulfilltrack.FulfillTrack.auth.permisos.RolEntity;
import com.fulfilltrack.FulfillTrack.auth.permisos.RolRepository;
import com.fulfilltrack.FulfillTrack.auth.permisos.Roles;
import com.fulfilltrack.FulfillTrack.common.exception.EntidadNoEncontradaException;
import com.fulfilltrack.FulfillTrack.common.exception.OperacionNoPermitidaException;
import com.fulfilltrack.FulfillTrack.features.usuario.dto.NuevoUsuarioDTO;
import com.fulfilltrack.FulfillTrack.features.usuario.dto.UsuarioRequestDTO;
import com.fulfilltrack.FulfillTrack.features.usuario.dto.UsuarioResponseDTO;
import com.fulfilltrack.FulfillTrack.features.usuario.mapper.NuevoUsuarioMapper;
import com.fulfilltrack.FulfillTrack.features.usuario.mapper.UsuarioMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Objects;
import java.util.UUID;
@Service
public class UsuarioService implements IUsuarioService{
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final CredencialRepository credencialRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final NuevoUsuarioMapper nuevoUsuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, CredencialRepository credencialRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder, NuevoUsuarioMapper nuevoUsuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.credencialRepository = credencialRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.nuevoUsuarioMapper = nuevoUsuarioMapper;
    }


    @Override
    @Transactional
    public UsuarioResponseDTO guardar(NewAccountRequest newAccountRequest) {
        NuevoUsuarioDTO nuevoUsuarioDTO = new NuevoUsuarioDTO(newAccountRequest.nombre(), newAccountRequest.apellido());

        UsuarioEntity guardado = usuarioRepository.save(nuevoUsuarioMapper.toEntity(nuevoUsuarioDTO));

        RolEntity rolPendiente = rolRepository.findByRol(Roles.ROLE_PENDIENTE)
                .orElseThrow(() -> new EntidadNoEncontradaException("Rol PENDIENTE no encontrado"));

        CredencialEntity nuevaCredencial = CredencialEntity.builder()
                .activo(true)
                .username(newAccountRequest.username())
                .password(passwordEncoder.encode(newAccountRequest.password()))
                .usuario(guardado)
                .roles(new java.util.HashSet<>(java.util.Set.of(rolPendiente)))
                .build();
        credencialRepository.save(nuevaCredencial);


        return usuarioMapper.toResponseDTO(guardado);
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
        return usuarioMapper.toResponseList(
            credencialRepository.findByActivo(true).stream()
                .map(CredencialEntity::getUsuario)
                .filter(Objects::nonNull)
                .toList()
        );
    }

    @Override
    public UsuarioEntity obtenerUsuarioEntidad(UUID uuid) {
        return usuarioRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("El usuario no se ha encontrado"));
    }

    @Override
    @Transactional
    public void asignarRol(UUID usuarioUuid, Roles rol) {
        UsuarioEntity usuario = obtenerUsuarioEntidad(usuarioUuid);
        if (Long.valueOf(1L).equals(usuario.getIdUsuario())) {
            throw new OperacionNoPermitidaException("No se puede modificar el rol del administrador supremo");
        }
        CredencialEntity credencial = credencialRepository.findByUsuario(usuario)
                .orElseThrow(() -> new EntidadNoEncontradaException("Credencial no encontrada"));
        RolEntity rolEntity = rolRepository.findByRol(rol)
                .orElseThrow(() -> new EntidadNoEncontradaException("Rol no encontrado"));
        credencial.getRoles().clear();
        credencial.getRoles().add(rolEntity);
        credencialRepository.save(credencial);
    }
}
