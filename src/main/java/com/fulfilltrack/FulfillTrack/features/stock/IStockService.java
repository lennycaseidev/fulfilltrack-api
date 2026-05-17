package com.fulfilltrack.FulfillTrack.features.stock;

import com.fulfilltrack.FulfillTrack.features.stock.dto.StockResponseDTO;

import java.util.UUID;

public interface IStockService {
    StockResponseDTO obtenerStockPorProductoUuid(UUID productoUuid);
}