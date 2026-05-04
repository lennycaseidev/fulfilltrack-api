package com.fulfilltrack.FulfillTrack.features.infoEmpleados.mapper;

import com.fulfilltrack.FulfillTrack.features.infoEmpleados.InfoEmpleadosEntity;
import com.fulfilltrack.FulfillTrack.features.infoEmpleados.InfoEmpleadosRepository;
import com.fulfilltrack.FulfillTrack.features.infoEmpleados.dto.InfoEmpleadosRequestDTO;
import com.fulfilltrack.FulfillTrack.features.infoEmpleados.dto.InfoEmpleadosResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InfoEmpleadosMapper {
    InfoEmpleadosEntity toEntity(InfoEmpleadosRequestDTO request);
    InfoEmpleadosResponseDTO toResponseDTO(InfoEmpleadosEntity entity);
    List<InfoEmpleadosResponseDTO> toResponseList(List<InfoEmpleadosEntity> list);
}
