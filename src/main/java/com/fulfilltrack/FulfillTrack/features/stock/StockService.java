package com.fulfilltrack.FulfillTrack.features.stock;

import com.fulfilltrack.FulfillTrack.common.exception.EntidadNoEncontradaException;
import com.fulfilltrack.FulfillTrack.features.stock.dto.StockResponseDTO;
import com.fulfilltrack.FulfillTrack.features.stock.mapper.StockMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StockService implements IStockService {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;

    public StockService(StockRepository stockRepository, StockMapper stockMapper) {
        this.stockRepository = stockRepository;
        this.stockMapper = stockMapper;
    }

    @Override
    public StockResponseDTO obtenerStockPorProductoUuid(UUID productoUuid) {
        StockEntity stock = stockRepository.findByProducto_Uuid(productoUuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("Stock no encontrado para el producto indicado"));
        return stockMapper.toResponseDTO(stock);
    }
}