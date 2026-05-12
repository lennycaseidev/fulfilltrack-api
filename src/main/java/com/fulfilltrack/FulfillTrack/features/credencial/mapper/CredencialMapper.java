package com.fulfilltrack.FulfillTrack.features.credencial.mapper;

import com.fulfilltrack.FulfillTrack.features.credencial.CredencialEntity;
import com.fulfilltrack.FulfillTrack.features.credencial.dto.CredencialRequestDTO;
import com.fulfilltrack.FulfillTrack.features.credencial.dto.CredencialResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CredencialMapper {
    @Mapping(target = "idCredencial", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "permiso", ignore = true)
    CredencialEntity toEntity(CredencialRequestDTO request);

    @Mapping(source = "permiso.uuid", target = "permisoUuid")
    CredencialResponseDTO toResponseDTO(CredencialEntity entity);

    List<CredencialResponseDTO> toResponseList(List<CredencialEntity> entities);
}
