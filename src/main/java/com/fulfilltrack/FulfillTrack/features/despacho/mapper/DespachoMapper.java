package com.fulfilltrack.FulfillTrack.features.despacho.mapper;

import com.fulfilltrack.FulfillTrack.features.despacho.DespachoEntity;
import com.fulfilltrack.FulfillTrack.features.despacho.dto.DespachoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DespachoMapper {

    @Mapping(source = "pedido.uuid", target = "pedidoUuid")
    @Mapping(source = "pedido.numeroOrden", target = "numeroOrden")
    @Mapping(source = "usuario.uuid", target = "usuarioUuid")
    @Mapping(source = "usuario.nombre", target = "nombreUsuario")
    @Mapping(source = "usuario.apellido", target = "apellidoUsuario")
    DespachoResponseDTO toResponseDTO(DespachoEntity entity);

    List<DespachoResponseDTO> toResponseList(List<DespachoEntity> entities);
}