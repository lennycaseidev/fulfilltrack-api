package com.fulfilltrack.FulfillTrack.features.puesto;

import com.fulfilltrack.FulfillTrack.common.exception.EntidadNoEncontradaException;
import com.fulfilltrack.FulfillTrack.features.puesto.dto.PuestoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.puesto.dto.PuestoResponseDTO;
import com.fulfilltrack.FulfillTrack.features.puesto.mapper.PuestoMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PuestoService implements IPuestoService{
    private final PuestoRepository puestoRepository;
    private final PuestoMapper puestoMapper;

    public PuestoService(PuestoRepository puestoRepository, PuestoMapper puestoMapper) {
        this.puestoRepository = puestoRepository;
        this.puestoMapper = puestoMapper;
    }

    @Override
    public List<PuestoResponseDTO> listarPuestos() {
        List<PuestoEntity> puestos = puestoRepository.findAll();
        return puestoMapper.toResponseList(puestos);
    }

    @Override
    public PuestoResponseDTO obtenerPuestoPorUuid(UUID uuid) {
        PuestoEntity puesto = obtenerPuestoEntidad(uuid);
        return puestoMapper.toResponseDTO(puesto);
    }

    @Override
    public PuestoResponseDTO actualizarPuesto(UUID uuid, PuestoRequestDTO request) {
        PuestoEntity puesto = obtenerPuestoEntidad(uuid);
        puesto.setNombrePuesto(request.getNombrePuesto());
        puesto.setDescripcion(request.getDescripcion());
        return puestoMapper.toResponseDTO(puestoRepository.save(puesto));
    }

    @Override
    public void eliminarPuesto(UUID uuid) {
        PuestoEntity puesto = obtenerPuestoEntidad(uuid);
        puestoRepository.delete(puesto);
    }

    @Override
    public PuestoResponseDTO crearPuesto(PuestoRequestDTO request) {
        PuestoEntity puesto = puestoMapper.toEntity(request);
        return puestoMapper.toResponseDTO(puestoRepository.save(puesto));
    }

    @Override
    public PuestoEntity obtenerPuestoEntidad(UUID uuid) {
        return puestoRepository.findByUuid(uuid)
                .orElseThrow(()-> new EntidadNoEncontradaException("el puesto no se ha encontrado"));
    }
}
