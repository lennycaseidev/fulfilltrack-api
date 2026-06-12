package com.fulfilltrack.FulfillTrack.features.stock;

import com.fulfilltrack.FulfillTrack.auth.TenantContext;
import com.fulfilltrack.FulfillTrack.common.exception.EntidadNoEncontradaException;
import com.fulfilltrack.FulfillTrack.features.empresa.IEmpresaService;
import com.fulfilltrack.FulfillTrack.features.producto.ProductoEntity;
import com.fulfilltrack.FulfillTrack.features.stock.mapper.StockMapper;
import com.fulfilltrack.FulfillTrack.features.stockMovimiento.IStockMovimientoService;
import com.fulfilltrack.FulfillTrack.features.stockMovimiento.TipoMovimientoStock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock StockRepository stockRepository;
    @Mock StockMapper stockMapper;
    @Mock IEmpresaService empresaService;
    @Mock IStockMovimientoService stockMovimientoService;
    @Mock TenantContext tenantContext;

    @InjectMocks StockService stockService;

    private StockEntity stockConCantidades(int disponible, int reservado) {
        ProductoEntity producto = new ProductoEntity();
        return StockEntity.builder()
                .producto(producto)
                .cantidadDisponible(disponible)
                .cantidadReservada(reservado)
                .build();
    }

    // --- tieneStockSuficiente ---

    @Test
    void tieneStockSuficiente_retornaTrueCuandoHayStockSuficiente() {
        UUID uuid = UUID.randomUUID();
        when(stockRepository.findByProducto_Uuid(uuid))
                .thenReturn(Optional.of(stockConCantidades(10, 0)));

        assertThat(stockService.tieneStockSuficiente(uuid, 5)).isTrue();
    }

    @Test
    void tieneStockSuficiente_retornaFalseCuandoStockEsInsuficiente() {
        UUID uuid = UUID.randomUUID();
        when(stockRepository.findByProducto_Uuid(uuid))
                .thenReturn(Optional.of(stockConCantidades(3, 0)));

        assertThat(stockService.tieneStockSuficiente(uuid, 5)).isFalse();
    }

    @Test
    void tieneStockSuficiente_retornaFalseCuandoNoExisteStock() {
        UUID uuid = UUID.randomUUID();
        when(stockRepository.findByProducto_Uuid(uuid)).thenReturn(Optional.empty());

        assertThat(stockService.tieneStockSuficiente(uuid, 1)).isFalse();
    }

    // --- reservarStock ---

    @Test
    void reservarStock_reduceCantidadDisponibleYAumentaReservada() {
        UUID uuid = UUID.randomUUID();
        StockEntity stock = stockConCantidades(10, 2);
        when(stockRepository.findByProducto_Uuid(uuid)).thenReturn(Optional.of(stock));
        when(stockRepository.save(any())).thenReturn(stock);

        stockService.reservarStock(uuid, 4, "Reserva por pedido ORD-001");

        assertThat(stock.getCantidadDisponible()).isEqualTo(6);
        assertThat(stock.getCantidadReservada()).isEqualTo(6);
        verify(stockMovimientoService).registrarMovimiento(any(), eq(TipoMovimientoStock.RESERVA), any(), eq(4));
    }

    @Test
    void reservarStock_lanzaExcepcionCuandoNoExisteStock() {
        UUID uuid = UUID.randomUUID();
        when(stockRepository.findByProducto_Uuid(uuid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> stockService.reservarStock(uuid, 1, "motivo"))
                .isInstanceOf(EntidadNoEncontradaException.class);
    }

    // --- confirmarConsumoStock ---

    @Test
    void confirmarConsumoStock_reduceStockReservado() {
        UUID uuid = UUID.randomUUID();
        StockEntity stock = stockConCantidades(6, 4);
        when(stockRepository.findByProducto_Uuid(uuid)).thenReturn(Optional.of(stock));
        when(stockRepository.save(any())).thenReturn(stock);

        stockService.confirmarConsumoStock(uuid, 4, "Confirmación pedido ORD-001");

        assertThat(stock.getCantidadReservada()).isEqualTo(0);
        assertThat(stock.getCantidadDisponible()).isEqualTo(6);
        verify(stockMovimientoService).registrarMovimiento(any(), eq(TipoMovimientoStock.CONSUMO), any(), eq(4));
    }

    // --- liberarReservaStock ---

    @Test
    void liberarReservaStock_restauraCantidadDisponible() {
        UUID uuid = UUID.randomUUID();
        StockEntity stock = stockConCantidades(6, 4);
        when(stockRepository.findByProducto_Uuid(uuid)).thenReturn(Optional.of(stock));
        when(stockRepository.save(any())).thenReturn(stock);

        stockService.liberarReservaStock(uuid, 4, "Liberación por devolución");

        assertThat(stock.getCantidadReservada()).isEqualTo(0);
        assertThat(stock.getCantidadDisponible()).isEqualTo(10);
        verify(stockMovimientoService).registrarMovimiento(any(), eq(TipoMovimientoStock.LIBERACION), any(), eq(4));
    }

    // --- crearStockInicial ---

    @Test
    void crearStockInicial_guardaStockConCantidadesEnCero() {
        ProductoEntity producto = new ProductoEntity();

        stockService.crearStockInicial(producto);

        verify(stockRepository).save(argThat(stock ->
                stock.getCantidadDisponible() == 0 && stock.getCantidadReservada() == 0
        ));
    }
}