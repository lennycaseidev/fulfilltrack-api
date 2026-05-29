package com.fulfilltrack.FulfillTrack.features.infoEmpleados.mapper;

import com.fulfilltrack.FulfillTrack.features.infoEmpleados.InfoEmpleadosEntity;
import com.fulfilltrack.FulfillTrack.features.infoEmpleados.dto.InfoEmpleadosRequestDTO;
import com.fulfilltrack.FulfillTrack.features.infoEmpleados.dto.InfoEmpleadosResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InfoEmpleadosMapper {

    @Mapping(source = "puesto.uuid", target = "puestoUuid")
    @Mapping(source = "usuario.uuid", target = "usuarioUuid")
    @Mapping(source = "deposito.uuid", target = "depositoUuid")
    InfoEmpleadosResponseDTO toResponseDTO(InfoEmpleadosEntity entity);

    List<InfoEmpleadosResponseDTO> toResponseList(List<InfoEmpleadosEntity> list);
}