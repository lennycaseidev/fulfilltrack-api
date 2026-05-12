package com.fulfilltrack.FulfillTrack.features.producto.mapper;

import com.fulfilltrack.FulfillTrack.features.producto.ProductoEntity;
import com.fulfilltrack.FulfillTrack.features.producto.dto.ProductoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.producto.dto.ProductoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductoMapper {
    @Mapping(source = "empresa.uuid", target = "empresaUuid")
    ProductoResponseDTO toResponseDTO(ProductoEntity entity);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "idProducto", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "estado", ignore = true)
    ProductoEntity toEntity(ProductoRequestDTO request);

    List<ProductoResponseDTO> toResponseList(List<ProductoEntity> productos);
}
