package com.fulfilltrack.FulfillTrack.features.deposito.mapper;

import com.fulfilltrack.FulfillTrack.features.deposito.DepositoEntity;
import com.fulfilltrack.FulfillTrack.features.deposito.dto.DepositoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.deposito.dto.DepositoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepositoMapper {
    @Mapping(target = "idDeposito", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "creacionDeposito", ignore = true)
    @Mapping(target = "empresas", ignore = true)
    DepositoEntity toEntity(DepositoRequestDTO request);
    DepositoResponseDTO toResponseDTO(DepositoEntity entity);
    List<DepositoResponseDTO> toResponseList(List<DepositoEntity> depositos);
}