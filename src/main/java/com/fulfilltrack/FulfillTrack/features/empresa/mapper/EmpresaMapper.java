package com.fulfilltrack.FulfillTrack.features.empresa.mapper;

import com.fulfilltrack.FulfillTrack.features.empresa.EmpresaEntity;
import com.fulfilltrack.FulfillTrack.features.empresa.dto.EmpresaRequestDTO;
import com.fulfilltrack.FulfillTrack.features.empresa.dto.EmpresaResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmpresaMapper {
    EmpresaEntity toEntity(EmpresaRequestDTO request);
    EmpresaResponseDTO toResponseDTO(EmpresaEntity entity);
    List<EmpresaResponseDTO> toResponseList(List<EmpresaEntity> empresas);
}
