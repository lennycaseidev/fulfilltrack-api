package com.fulfilltrack.FulfillTrack.features.stockMovimiento;

import com.fulfilltrack.FulfillTrack.features.producto.ProductoEntity;
import com.fulfilltrack.FulfillTrack.features.stockMovimiento.dto.StockMovimientoResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IStockMovimientoService {
    void registrarMovimiento(ProductoEntity producto, TipoMovimientoStock tipo, String motivo, int cantidad);
    List<StockMovimientoResponseDTO> listarMovimientosPorProducto(UUID productoUuid);
    List<StockMovimientoResponseDTO> listarMovimientosPorEmpresa(UUID empresaUuid);
}