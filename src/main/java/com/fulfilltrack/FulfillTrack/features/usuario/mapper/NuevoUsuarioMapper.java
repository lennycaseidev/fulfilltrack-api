package com.fulfilltrack.FulfillTrack.features.usuario.mapper;

import com.fulfilltrack.FulfillTrack.features.usuario.UsuarioEntity;
import com.fulfilltrack.FulfillTrack.features.usuario.dto.NuevoUsuarioDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NuevoUsuarioMapper {
    UsuarioEntity toEntity(NuevoUsuarioDTO nuevoUsuarioDTO);
    NuevoUsuarioDTO toDTO(UsuarioEntity usuario);
}
