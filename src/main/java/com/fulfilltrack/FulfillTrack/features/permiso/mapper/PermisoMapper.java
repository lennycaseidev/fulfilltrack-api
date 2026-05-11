package com.fulfilltrack.FulfillTrack.features.permiso.mapper;

import com.fulfilltrack.FulfillTrack.features.permiso.PermisoEntity;
import com.fulfilltrack.FulfillTrack.features.permiso.dto.PermisoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.permiso.dto.PermisoResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermisoMapper {
    PermisoResponseDTO toResponseDTO(PermisoEntity entity);
    PermisoEntity toEntity(PermisoRequestDTO request);
    List<PermisoResponseDTO> toResponseList(List<PermisoEntity> permisos);

}
