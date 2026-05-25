package com.fulfilltrack.FulfillTrack.features.stock;

import com.fulfilltrack.FulfillTrack.common.exception.EntidadNoEncontradaException;
import com.fulfilltrack.FulfillTrack.features.empresa.IEmpresaService;
import com.fulfilltrack.FulfillTrack.features.producto.ProductoEntity;
import com.fulfilltrack.FulfillTrack.features.stock.dto.StockResponseDTO;
import com.fulfilltrack.FulfillTrack.features.stock.mapper.StockMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StockService implements IStockService {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;
    private final IEmpresaService empresaService;

    public StockService(StockRepository stockRepository, StockMapper stockMapper, IEmpresaService empresaService) {
        this.stockRepository = stockRepository;
        this.stockMapper = stockMapper;
        this.empresaService = empresaService;
    }

    @Override
    public StockResponseDTO obtenerStockPorProductoUuid(UUID productoUuid) {
        StockEntity stock = stockRepository.findByProducto_Uuid(productoUuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("Stock no encontrado para el producto indicado"));
        return stockMapper.toResponseDTO(stock);
    }

    @Override
    public List<StockResponseDTO> listarStockPorEmpresa(UUID empresaUuid) {
        if (!empresaService.existeEmpresaPorUuid(empresaUuid)) {
            throw new EntidadNoEncontradaException("La empresa no ha sido encontrada");
        }
        return stockMapper.toResponseList(stockRepository.findByProducto_Empresa_Uuid(empresaUuid));
    }

    @Override
    public List<StockResponseDTO> listarTodoElStock() {
        return stockMapper.toResponseList(stockRepository.findAll());
    }

    @Override
    public void crearStockInicial(ProductoEntity producto) {
        StockEntity stock = StockEntity.builder()
                .producto(producto)
                .cantidadReservada(0)
                .cantidadDisponible(0)
                .build();
        stockRepository.save(stock);
    }

    @Override
    public boolean tieneStockActivo(UUID productoUuid) {
        return stockRepository.findByProducto_Uuid(productoUuid)
                .map(stock -> stock.getCantidadDisponible() > 0 || stock.getCantidadReservada() > 0)
                .orElse(false);
    }

    @Override
    public boolean tieneStockSuficiente(UUID productoUuid, int cantidad) {
        return stockRepository.findByProducto_Uuid(productoUuid)
                .map(stock -> stock.getCantidadDisponible() >= cantidad)
                .orElse(false);
    }

    @Override
    public void reservarStock(UUID productoUuid, int cantidad) {
        StockEntity stock = stockRepository.findByProducto_Uuid(productoUuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("Stock no encontrado para el producto indicado"));
        stock.setCantidadDisponible(stock.getCantidadDisponible() - cantidad);
        stock.setCantidadReservada(stock.getCantidadReservada() + cantidad);
        stockRepository.save(stock);
    }

    @Override
    public StockResponseDTO agregarStock(UUID productoUuid, int cantidad) {
        StockEntity stock = stockRepository.findByProducto_Uuid(productoUuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("Stock no encontrado para el producto indicado"));
        stock.setCantidadDisponible(stock.getCantidadDisponible() + cantidad);
        stockRepository.save(stock);
        return stockMapper.toResponseDTO(stock);
    }

}