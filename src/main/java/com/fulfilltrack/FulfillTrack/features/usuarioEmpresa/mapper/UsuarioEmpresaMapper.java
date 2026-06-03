package com.fulfilltrack.FulfillTrack.features.usuarioEmpresa.mapper;

import com.fulfilltrack.FulfillTrack.features.usuarioEmpresa.UsuarioEmpresaEntity;
import com.fulfilltrack.FulfillTrack.features.usuarioEmpresa.dto.UsuarioEmpresaRequestDTO;
import com.fulfilltrack.FulfillTrack.features.usuarioEmpresa.dto.UsuarioEmpresaResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioEmpresaMapper {

    @Mapping(target = "idUsuarioEmpresa", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    UsuarioEmpresaEntity toEntity(UsuarioEmpresaRequestDTO request);

    @Mapping(source = "empresa.uuid", target = "empresaUuid")
    @Mapping(source = "usuario.uuid", target = "usuarioUuid")
    UsuarioEmpresaResponseDTO toResponseDTO(UsuarioEmpresaEntity entity);

    List<UsuarioEmpresaResponseDTO> toResponseList(List<UsuarioEmpresaEntity> usuariosEmpresa);
}