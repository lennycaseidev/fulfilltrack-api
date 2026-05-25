package com.fulfilltrack.FulfillTrack.features.stock;

import com.fulfilltrack.FulfillTrack.features.producto.ProductoEntity;
import com.fulfilltrack.FulfillTrack.features.stock.dto.StockResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IStockService {
    StockResponseDTO obtenerStockPorProductoUuid(UUID productoUuid);
    List<StockResponseDTO> listarStockPorEmpresa(UUID empresaUuid);
    List<StockResponseDTO> listarTodoElStock();
    void crearStockInicial(ProductoEntity producto);
    boolean tieneStockActivo(UUID productoUuid);
    boolean tieneStockSuficiente(UUID productoUuid, int cantidad);
    void reservarStock(UUID productoUuid, int cantidad);
}