package com.fulfilltrack.FulfillTrack.features.producto;

import com.fulfilltrack.FulfillTrack.features.producto.dto.ProductoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.producto.dto.ProductoResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IProductoService {
    ProductoResponseDTO crearProducto(ProductoRequestDTO request);
    ProductoResponseDTO obtenerProductoPorUuid(UUID uuid);
    ProductoResponseDTO obtenerProductoPorSku(String sku);
    List<ProductoResponseDTO> listarProductosPorEmpresa(UUID empresaUuid);
    List<ProductoResponseDTO> listarProductos();
    ProductoResponseDTO actualizarProducto(UUID uuid, ProductoRequestDTO request);
    void activarProducto(UUID uuid);
    void desactivarProducto(UUID uuid);
}
