package com.fulfilltrack.FulfillTrack.features.credencial;

import com.fulfilltrack.FulfillTrack.common.exception.EntidadDuplicadaException;
import com.fulfilltrack.FulfillTrack.common.exception.EntidadNoEncontradaException;
import com.fulfilltrack.FulfillTrack.common.exception.OperacionNoPermitidaException;
import com.fulfilltrack.FulfillTrack.common.utils.Estado;
import com.fulfilltrack.FulfillTrack.features.credencial.dto.CredencialRequestDTO;
import com.fulfilltrack.FulfillTrack.features.credencial.dto.CredencialResponseDTO;
import com.fulfilltrack.FulfillTrack.features.credencial.mapper.CredencialMapper;
import com.fulfilltrack.FulfillTrack.features.permiso.PermisoEntity;
import com.fulfilltrack.FulfillTrack.features.permiso.PermisoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CredencialService implements ICredencialService{
    private final CredencialRepository credencialRepository;
    private final CredencialMapper credencialMapper;
    private final PermisoRepository permisoRepository;
    public CredencialService(CredencialRepository credencialRepository, CredencialMapper credencialMapper, PermisoRepository permisoRepository) {
        this.credencialRepository = credencialRepository;
        this.credencialMapper = credencialMapper;
        this.permisoRepository = permisoRepository;
    }

    @Override
    public CredencialResponseDTO registrarCredencial(CredencialRequestDTO request) {
        if(credencialRepository.existsByEmail(request.getEmail())){
            throw new EntidadDuplicadaException("El email ya está en uso");
        }
        if(credencialRepository.existsByNombreUsuario(request.getNombreUsuario())){
            throw new EntidadDuplicadaException("El nombre de usuario ya esta en uso");
        }
        CredencialEntity credencial = credencialMapper.toEntity(request);
        return credencialMapper.toResponseDTO(credencialRepository.save(credencial));
    }

    @Override
    public CredencialResponseDTO obtenerCredencialPorUuid(UUID uuid) {
        CredencialEntity credencial = credencialRepository.findByUuid(uuid)
                .orElseThrow(()-> new EntidadNoEncontradaException("No se ha encontrado la credencial"));
        return credencialMapper.toResponseDTO(credencial);
    }

    @Override
    public List<CredencialResponseDTO> obtenerCredenciales() {
       List<CredencialEntity> credenciales = credencialRepository.findAll();
        return credencialMapper.toResponseList(credenciales);
    }

    @Override
    public CredencialResponseDTO asignarPermiso(UUID uuid, UUID permisoUuid) {
        CredencialEntity credencial = credencialRepository.findByUuid(uuid)
                .orElseThrow(()-> new EntidadNoEncontradaException("No se ha encontrado la credencial"));

        PermisoEntity permiso = permisoRepository.findByUuid(permisoUuid)
                .orElseThrow(()-> new EntidadNoEncontradaException("No se ha encontrado el permiso"));

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
        CredencialEntity credencial = credencialRepository.findByUuid(uuid)
                .orElseThrow(()-> new EntidadNoEncontradaException("No se ha encontrado la credencial"));
        if(credencial.getEstado() == Estado.ACTIVA){
            throw new OperacionNoPermitidaException("La credencial ya se encuentra activa");
        }
        credencial.setEstado(Estado.ACTIVA);
        credencialRepository.save(credencial);
    }

    @Override
    public void desactivarCredencial(UUID uuid) {
        CredencialEntity credencial = credencialRepository.findByUuid(uuid)
                .orElseThrow(()-> new EntidadNoEncontradaException("No se ha encontrado la credencial"));
        if(credencial.getEstado() == Estado.INACTIVA){
            throw new OperacionNoPermitidaException("La credencial ya se encuentra inactiva");
        }
        credencial.setEstado(Estado.INACTIVA);
        credencialRepository.save(credencial);
    }
}
