package com.fulfilltrack.FulfillTrack.features.stock;

import com.fulfilltrack.FulfillTrack.auth.TenantContext;
import com.fulfilltrack.FulfillTrack.common.exception.EntidadNoEncontradaException;
import com.fulfilltrack.FulfillTrack.features.empresa.IEmpresaService;
import com.fulfilltrack.FulfillTrack.features.producto.ProductoEntity;
import com.fulfilltrack.FulfillTrack.features.stock.dto.StockResponseDTO;
import com.fulfilltrack.FulfillTrack.features.stock.mapper.StockMapper;
import com.fulfilltrack.FulfillTrack.features.stockMovimiento.IStockMovimientoService;
import com.fulfilltrack.FulfillTrack.features.stockMovimiento.TipoMovimientoStock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class StockService implements IStockService {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;
    private final IEmpresaService empresaService;
    private final IStockMovimientoService stockMovimientoService;
    private final TenantContext tenantContext;

    public StockService(StockRepository stockRepository, StockMapper stockMapper,
                        IEmpresaService empresaService, IStockMovimientoService stockMovimientoService,
                        TenantContext tenantContext) {
        this.stockRepository = stockRepository;
        this.stockMapper = stockMapper;
        this.empresaService = empresaService;
        this.stockMovimientoService = stockMovimientoService;
        this.tenantContext = tenantContext;
    }

    @Override
    public StockResponseDTO obtenerStockPorProductoUuid(UUID productoUuid) {
        StockEntity stock = stockRepository.findByProducto_Uuid(productoUuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("Stock no encontrado para el producto indicado"));
        return stockMapper.toResponseDTO(stock);
    }

    @Override
    public List<StockResponseDTO> listarStockPorEmpresa(UUID empresaUuid) {
        UUID efectivo = tenantContext.getEmpresaUuid().orElse(empresaUuid);
        if (!empresaService.existeEmpresaPorUuid(efectivo)) {
            throw new EntidadNoEncontradaException("La empresa no ha sido encontrada");
        }
        return stockMapper.toResponseList(stockRepository.findByProducto_Empresa_Uuid(efectivo));
    }

    @Override
    public List<StockResponseDTO> listarTodoElStock() {
        return tenantContext.getEmpresaUuid()
                .map(uuid -> stockMapper.toResponseList(stockRepository.findByProducto_Empresa_Uuid(uuid)))
                .orElseGet(() -> stockMapper.toResponseList(stockRepository.findAll()));
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
    @Transactional
    public void reservarStock(UUID productoUuid, int cantidad, String motivo) {
        StockEntity stock = stockRepository.findByProducto_Uuid(productoUuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("Stock no encontrado para el producto indicado"));
        stock.setCantidadDisponible(stock.getCantidadDisponible() - cantidad);
        stock.setCantidadReservada(stock.getCantidadReservada() + cantidad);
        stockRepository.save(stock);
        stockMovimientoService.registrarMovimiento(stock.getProducto(), TipoMovimientoStock.RESERVA, motivo, cantidad);
    }

    @Override
    @Transactional
    public void confirmarConsumoStock(UUID productoUuid, int cantidad, String motivo) {
        StockEntity stock = stockRepository.findByProducto_Uuid(productoUuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("Stock no encontrado para el producto indicado"));
        stock.setCantidadReservada(stock.getCantidadReservada() - cantidad);
        stockRepository.save(stock);
        stockMovimientoService.registrarMovimiento(stock.getProducto(), TipoMovimientoStock.CONSUMO, motivo, cantidad);
    }

    @Override
    @Transactional
    public void liberarReservaStock(UUID productoUuid, int cantidad, String motivo) {
        StockEntity stock = stockRepository.findByProducto_Uuid(productoUuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("Stock no encontrado para el producto indicado"));
        stock.setCantidadReservada(stock.getCantidadReservada() - cantidad);
        stock.setCantidadDisponible(stock.getCantidadDisponible() + cantidad);
        stockRepository.save(stock);
        stockMovimientoService.registrarMovimiento(stock.getProducto(), TipoMovimientoStock.LIBERACION, motivo, cantidad);
    }

    @Override
    @Transactional
    public StockResponseDTO agregarStock(UUID productoUuid, int cantidad, TipoMovimientoStock tipo, String motivo) {
        StockEntity stock = stockRepository.findByProducto_Uuid(productoUuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("Stock no encontrado para el producto indicado"));
        stock.setCantidadDisponible(stock.getCantidadDisponible() + cantidad);
        stockRepository.save(stock);
        stockMovimientoService.registrarMovimiento(stock.getProducto(), tipo, motivo, cantidad);
        return stockMapper.toResponseDTO(stock);
    }
}