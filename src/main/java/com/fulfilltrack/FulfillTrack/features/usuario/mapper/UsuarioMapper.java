package com.fulfilltrack.FulfillTrack.features.usuario.mapper;

import com.fulfilltrack.FulfillTrack.features.usuario.UsuarioEntity;
import com.fulfilltrack.FulfillTrack.features.usuario.dto.UsuarioResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioResponseDTO toResponseDTO(UsuarioEntity entity);
    List<UsuarioResponseDTO> toResponseList(List<UsuarioEntity> usuarios);
}
