package com.fulfilltrack.FulfillTrack.features.empresa.mapper;

import com.fulfilltrack.FulfillTrack.features.empresa.EmpresaEntity;
import com.fulfilltrack.FulfillTrack.features.empresa.dto.EmpresaRequestDTO;
import com.fulfilltrack.FulfillTrack.features.empresa.dto.EmpresaResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmpresaMapper {

    @Mapping(target = "idEmpresa", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "deposito", ignore = true)
    EmpresaEntity toEntity(EmpresaRequestDTO request);

    @Mapping(source = "deposito.uuid", target = "depositoUuid")
    EmpresaResponseDTO toResponseDTO(EmpresaEntity entity);

    List<EmpresaResponseDTO> toResponseList(List<EmpresaEntity> empresas);
}
