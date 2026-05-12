package com.fulfilltrack.FulfillTrack.features.producto;

import com.fulfilltrack.FulfillTrack.features.producto.dto.ProductoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.producto.dto.ProductoResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IProductoService {
    ProductoResponseDTO crearProducto(ProductoRequestDTO request);
    ProductoResponseDTO obtenerProductoPorUuid(UUID uuid);
    ProductoResponseDTO obtenerProductoPorSku(String sku);
    List<ProductoResponseDTO> listarProductoPorEmpresa(UUID empresaUuid);
    List<ProductoResponseDTO> listarProductos();
    void activarProducto(UUID uuid);
    void desactivarProducto(UUID uuid);
}
