package com.fulfilltrack.FulfillTrack.features.deposito.mapper;

import com.fulfilltrack.FulfillTrack.features.deposito.DepositoEntity;
import com.fulfilltrack.FulfillTrack.features.deposito.dto.DepositoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.deposito.dto.DepositoResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepositoMapper {
    DepositoEntity toEntity(DepositoRequestDTO request);
    DepositoResponseDTO toResponseDTO(DepositoEntity entity);
    List<DepositoResponseDTO> toResponseList(List<DepositoEntity> depositos);
}