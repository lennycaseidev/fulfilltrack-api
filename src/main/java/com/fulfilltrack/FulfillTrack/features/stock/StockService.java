package com.fulfilltrack.FulfillTrack.features.stock;

import com.fulfilltrack.FulfillTrack.common.exception.EntidadNoEncontradaException;
import com.fulfilltrack.FulfillTrack.features.empresa.EmpresaRepository;
import com.fulfilltrack.FulfillTrack.features.stock.dto.StockResponseDTO;
import com.fulfilltrack.FulfillTrack.features.stock.mapper.StockMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StockService implements IStockService {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;
    private final EmpresaRepository empresaRepository;

    public StockService(StockRepository stockRepository, StockMapper stockMapper, EmpresaRepository empresaRepository) {
        this.stockRepository = stockRepository;
        this.stockMapper = stockMapper;
        this.empresaRepository = empresaRepository;
    }

    @Override
    public StockResponseDTO obtenerStockPorProductoUuid(UUID productoUuid) {
        StockEntity stock = stockRepository.findByProducto_Uuid(productoUuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("Stock no encontrado para el producto indicado"));
        return stockMapper.toResponseDTO(stock);
    }

    @Override
    public List<StockResponseDTO> listarStockPorEmpresa(UUID empresaUuid) {
        if (!empresaRepository.existsByUuid(empresaUuid)) {
            throw new EntidadNoEncontradaException("La empresa no ha sido encontrada");
        }
        return stockMapper.toResponseList(stockRepository.findByProducto_Empresa_Uuid(empresaUuid));
    }

    @Override
    public List<StockResponseDTO> listarTodoElStock() {
        return stockMapper.toResponseList(stockRepository.findAll());
    }
}