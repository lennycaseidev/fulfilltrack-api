package com.fulfilltrack.FulfillTrack.features.usuario;

import com.fulfilltrack.FulfillTrack.auth.credenciales.CredencialEntity;
import com.fulfilltrack.FulfillTrack.auth.credenciales.CredencialRepository;
import com.fulfilltrack.FulfillTrack.auth.dto.NewAccountRequest;
import com.fulfilltrack.FulfillTrack.auth.permisos.RolEntity;
import com.fulfilltrack.FulfillTrack.auth.permisos.RolRepository;
import com.fulfilltrack.FulfillTrack.auth.permisos.Roles;
import com.fulfilltrack.FulfillTrack.common.exception.EntidadNoEncontradaException;
import com.fulfilltrack.FulfillTrack.common.exception.OperacionNoPermitidaException;
import com.fulfilltrack.FulfillTrack.features.usuario.dto.CambiarPasswordDTO;
import com.fulfilltrack.FulfillTrack.features.usuario.dto.UsuarioResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.UUID;
@Service
public class UsuarioService implements IUsuarioService{
    private final UsuarioRepository usuarioRepository;
    private final CredencialRepository credencialRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, CredencialRepository credencialRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.credencialRepository = credencialRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    public UsuarioResponseDTO guardar(NewAccountRequest newAccountRequest) {
        UsuarioEntity guardado = usuarioRepository.save(UsuarioEntity.builder()
                .nombre(newAccountRequest.nombre())
                .apellido(newAccountRequest.apellido())
                .build());

        boolean esElPrimero = credencialRepository.count() == 0;
        Roles rolAsignado = esElPrimero ? Roles.ROLE_ADMIN : Roles.ROLE_PENDIENTE;
        RolEntity rol = rolRepository.findByRol(rolAsignado)
                .orElseThrow(() -> new EntidadNoEncontradaException("Rol " + rolAsignado + " no encontrado"));

        CredencialEntity nuevaCredencial = CredencialEntity.builder()
                .activo(true)
                .username(newAccountRequest.username())
                .email(newAccountRequest.email())
                .password(passwordEncoder.encode(newAccountRequest.password()))
                .usuario(guardado)
                .roles(new java.util.HashSet<>(java.util.Set.of(rol)))
                .build();
        CredencialEntity credencialGuardada = credencialRepository.save(nuevaCredencial);

        return buildResponse(guardado, credencialGuardada);
    }




    @Override
    public UsuarioResponseDTO obtenerUsuarioPorUuid(UUID uuid) {
        UsuarioEntity usuario = usuarioRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("El usuario no se ha encontrado"));
        CredencialEntity credencial = credencialRepository.findByUsuario(usuario).orElse(null);
        return buildResponse(usuario, credencial);
    }

    @Override
    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(u -> buildResponse(u, credencialRepository.findByUsuario(u).orElse(null)))
                .toList();
    }

    @Override
    public List<UsuarioResponseDTO> listarUsuariosActivos() {
        return credencialRepository.findByActivo(true).stream()
                .filter(c -> c.getUsuario() != null)
                .map(c -> buildResponse(c.getUsuario(), c))
                .toList();
    }

    @Override
    public UsuarioEntity obtenerUsuarioEntidad(UUID uuid) {
        return usuarioRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("El usuario no se ha encontrado"));
    }

    private UsuarioResponseDTO buildResponse(UsuarioEntity usuario, CredencialEntity credencial) {
        String rol = null;
        if (credencial != null && !credencial.getRoles().isEmpty()) {
            rol = credencial.getRoles().iterator().next().getRol().name();
        }
        return UsuarioResponseDTO.builder()
                .uuid(usuario.getUuid())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .username(credencial != null ? credencial.getUsername() : null)
                .email(credencial != null ? credencial.getEmail() : null)
                .rol(rol)
                .activo(credencial != null ? credencial.getActivo() : null)
                .build();
    }

    @Override
    @Transactional
    public void cambiarPassword(UUID usuarioUuid, CambiarPasswordDTO dto) {
        UsuarioEntity usuario = obtenerUsuarioEntidad(usuarioUuid);
        CredencialEntity credencial = credencialRepository.findByUsuario(usuario)
                .orElseThrow(() -> new EntidadNoEncontradaException("Credencial no encontrada"));
        if (!passwordEncoder.matches(dto.getPasswordActual(), credencial.getPassword())) {
            throw new OperacionNoPermitidaException("La contraseña actual es incorrecta");
        }
        credencial.setPassword(passwordEncoder.encode(dto.getPasswordNueva()));
        credencialRepository.save(credencial);
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
