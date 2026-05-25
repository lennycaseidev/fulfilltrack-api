package com.fulfilltrack.FulfillTrack.features.credencial;

import com.fulfilltrack.FulfillTrack.common.exception.EntidadDuplicadaException;
import com.fulfilltrack.FulfillTrack.common.exception.EntidadNoEncontradaException;
import com.fulfilltrack.FulfillTrack.common.exception.OperacionNoPermitidaException;
import com.fulfilltrack.FulfillTrack.common.utils.Estado;
import com.fulfilltrack.FulfillTrack.features.credencial.dto.CredencialResponseDTO;
import com.fulfilltrack.FulfillTrack.features.credencial.mapper.CredencialMapper;
import com.fulfilltrack.FulfillTrack.features.permiso.IPermisoService;
import com.fulfilltrack.FulfillTrack.features.permiso.PermisoEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CredencialService implements ICredencialService{
    private final CredencialRepository credencialRepository;
    private final CredencialMapper credencialMapper;
    private final IPermisoService permisoService;

    public CredencialService(CredencialRepository credencialRepository, CredencialMapper credencialMapper, IPermisoService permisoService) {
        this.credencialRepository = credencialRepository;
        this.credencialMapper = credencialMapper;
        this.permisoService = permisoService;
    }

    @Override
    public CredencialEntity registrarCredencial(String nombreUsuario, String email, String contrasena) {
        if(credencialRepository.existsByEmail(email)){
            throw new EntidadDuplicadaException("El email ya está en uso");
        }
        if(credencialRepository.existsByNombreUsuario(nombreUsuario)){
            throw new EntidadDuplicadaException("El nombre de usuario ya esta en uso");
        }
        CredencialEntity credencial = CredencialEntity.builder().
                contrasena(contrasena)
                .email(email)
                .nombreUsuario(nombreUsuario)
                .build();
        return credencialRepository.save(credencial);
    }

    @Override
    public CredencialResponseDTO obtenerCredencialPorUuid(UUID uuid) {
        CredencialEntity credencial = obtenerCredencialUuid(uuid);
        return credencialMapper.toResponseDTO(credencial);
    }

    @Override
    public List<CredencialResponseDTO> obtenerCredenciales() {
       List<CredencialEntity> credenciales = credencialRepository.findAll();
        return credencialMapper.toResponseList(credenciales);
    }

    @Override
    public CredencialResponseDTO asignarPermiso(UUID uuid, UUID permisoUuid) {
        CredencialEntity credencial = obtenerCredencialUuid(uuid);
        PermisoEntity permiso = permisoService.obtenerPermisoPorUuid(permisoUuid);

        if(permiso.equals(credencial.getPermiso())){
            throw new OperacionNoPermitidaException("El permiso ya está asignado a esta credencial");
        }
        credencial.setPermiso(permiso);
        return credencialMapper.toResponseDTO(credencialRepository.save(credencial));
    }

    @Override
    public CredencialResponseDTO obtenerCredencialPorEmail(String email) {
        CredencialEntity credencial = credencialRepository.findByEmail(email)
                .orElseThrow(()-> new EntidadNoEncontradaException("No se ha encontrado una credencial asociada al email"));
        return credencialMapper.toResponseDTO(credencial);
    }

    @Override
    public void activarCredencial(UUID uuid) {
        CredencialEntity credencial = obtenerCredencialUuid(uuid);
        if(credencial.getEstado() == Estado.ACTIVA){
            throw new OperacionNoPermitidaException("La credencial ya se encuentra activa");
        }
        credencial.setEstado(Estado.ACTIVA);
        credencialRepository.save(credencial);
    }

    @Override
    public void desactivarCredencial(UUID uuid) {
        CredencialEntity credencial = obtenerCredencialUuid(uuid);
        if(credencial.getEstado() == Estado.INACTIVA){
            throw new OperacionNoPermitidaException("La credencial ya se encuentra inactiva");
        }
        credencial.setEstado(Estado.INACTIVA);
        credencialRepository.save(credencial);
    }

    @Override
    public CredencialEntity obtenerCredencialUuid(UUID uuid) {
        return credencialRepository.findByUuid(uuid)
                .orElseThrow(()-> new EntidadNoEncontradaException("No se ha encontrado la credencial"));
    }
}
