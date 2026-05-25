package com.fulfilltrack.FulfillTrack.features.permiso;

import com.fulfilltrack.FulfillTrack.common.exception.EntidadDuplicadaException;
import com.fulfilltrack.FulfillTrack.common.exception.EntidadNoEncontradaException;
import com.fulfilltrack.FulfillTrack.features.permiso.dto.PermisoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.permiso.dto.PermisoResponseDTO;
import com.fulfilltrack.FulfillTrack.features.permiso.mapper.PermisoMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PermisoService implements IPermisoService{
    private final PermisoRepository permisoRepository;
    private final PermisoMapper permisoMapper;

    public PermisoService(PermisoRepository permisoRepository, PermisoMapper permisoMapper) {
        this.permisoRepository = permisoRepository;
        this.permisoMapper = permisoMapper;
    }


    @Override
    public List<PermisoResponseDTO> listarPermisos() {
        List<PermisoEntity> permisos = permisoRepository.findAll();
        return permisoMapper.toResponseList(permisos);
    }

    @Override
    public PermisoResponseDTO crearPermiso(PermisoRequestDTO request) {
        if (permisoRepository.existsByNombrePermiso(request.getNombrePermiso())) {
            throw new EntidadDuplicadaException("Ya existe un permiso con ese nombre");
        }
        PermisoEntity permiso = permisoMapper.toEntity(request);
        return permisoMapper.toResponseDTO(permisoRepository.save(permiso));
    }

    @Override
    public PermisoEntity obtenerPermisoPorUuid(UUID uuid) {
        return permisoRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("El permiso no se ha encontrado"));
    }

    @Override
    public PermisoResponseDTO actualizarPermiso(UUID uuid, PermisoRequestDTO request) {
        PermisoEntity permiso = permisoRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("el permiso no se ha encontrado"));
        if (permisoRepository.existsByNombrePermiso(request.getNombrePermiso())) {
            throw new EntidadDuplicadaException("Ya existe un permiso con ese nombre");
        }
        permiso.setNombrePermiso(request.getNombrePermiso());
        return permisoMapper.toResponseDTO(permisoRepository.save(permiso));
    }
}
