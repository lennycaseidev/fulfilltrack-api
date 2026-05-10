package com.fulfilltrack.FulfillTrack.features.puesto.mapper;

import com.fulfilltrack.FulfillTrack.features.puesto.PuestoEntity;
import com.fulfilltrack.FulfillTrack.features.puesto.PuestoRepository;
import com.fulfilltrack.FulfillTrack.features.puesto.dto.PuestoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.puesto.dto.PuestoResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PuestoMapper {
    PuestoResponseDTO toResponseDTO(PuestoEntity entity);
    PuestoEntity toEntity(PuestoRequestDTO request);
    List<PuestoResponseDTO> toResponseList(List<PuestoEntity> puestos);
}
