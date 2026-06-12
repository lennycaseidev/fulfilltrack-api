package com.fulfilltrack.FulfillTrack.features.pedido;

import com.fulfilltrack.FulfillTrack.auth.TenantContext;
import com.fulfilltrack.FulfillTrack.common.exception.OperacionNoPermitidaException;
import com.fulfilltrack.FulfillTrack.features.empresa.IEmpresaService;
import com.fulfilltrack.FulfillTrack.features.pedido.item.ItemPedidoEntity;
import com.fulfilltrack.FulfillTrack.features.pedido.item.ItemPedidoRepository;
import com.fulfilltrack.FulfillTrack.features.pedido.mapper.PedidoMapper;
import com.fulfilltrack.FulfillTrack.features.pedidoMovimiento.IPedidoMovimientoService;
import com.fulfilltrack.FulfillTrack.features.producto.IProductoService;
import com.fulfilltrack.FulfillTrack.features.producto.ProductoEntity;
import com.fulfilltrack.FulfillTrack.features.stock.IStockService;
import com.fulfilltrack.FulfillTrack.features.stockMovimiento.TipoMovimientoStock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock PedidoRepository pedidoRepository;
    @Mock ItemPedidoRepository itemPedidoRepository;
    @Mock PedidoMapper pedidoMapper;
    @Mock IEmpresaService empresaService;
    @Mock IProductoService productoService;
    @Mock IStockService stockService;
    @Mock IPedidoMovimientoService pedidoMovimientoService;
    @Mock TenantContext tenantContext;

    @InjectMocks PedidoService pedidoService;

    private PedidoEntity pedidoEnEstado(EstadoPedido estado) {
        ProductoEntity producto = new ProductoEntity();
        ItemPedidoEntity item = ItemPedidoEntity.builder()
                .producto(producto)
                .cantidad(2)
                .build();
        PedidoEntity pedido = PedidoEntity.builder()
                .uuid(UUID.randomUUID())
                .numeroOrden("ORD-001")
                .estado(estado)
                .items(List.of(item))
                .build();
        when(pedidoRepository.findByUuid(pedido.getUuid())).thenReturn(Optional.of(pedido));
        return pedido;
    }

    // --- confirmarPedido ---

    @Test
    void confirmarPedido_transicionValida_deRECIBIDOaCONFIRMADO() {
        PedidoEntity pedido = pedidoEnEstado(EstadoPedido.RECIBIDO);
        when(pedidoRepository.save(any())).thenReturn(pedido);

        pedidoService.confirmarPedido(pedido.getUuid());

        verify(stockService).confirmarConsumoStock(any(), eq(2), any());
        verify(pedidoMovimientoService).registrarMovimiento(any(), eq(EstadoPedido.RECIBIDO), eq(EstadoPedido.CONFIRMADO));
    }

    @Test
    void confirmarPedido_lanzaExcepcionSiNoEstaEnRECIBIDO() {
        PedidoEntity pedido = pedidoEnEstado(EstadoPedido.CONFIRMADO);

        assertThatThrownBy(() -> pedidoService.confirmarPedido(pedido.getUuid()))
                .isInstanceOf(OperacionNoPermitidaException.class)
                .hasMessageContaining("RECIBIDO");
    }

    // --- iniciarPreparacion ---

    @Test
    void iniciarPreparacion_transicionValida_deCONFIRMADOaEN_PREPARACION() {
        PedidoEntity pedido = pedidoEnEstado(EstadoPedido.CONFIRMADO);
        when(pedidoRepository.save(any())).thenReturn(pedido);

        pedidoService.iniciarPreparacion(pedido.getUuid());

        verify(pedidoMovimientoService).registrarMovimiento(any(), eq(EstadoPedido.CONFIRMADO), eq(EstadoPedido.EN_PREPARACION));
    }

    @Test
    void iniciarPreparacion_lanzaExcepcionSiNoEstaEnCONFIRMADO() {
        PedidoEntity pedido = pedidoEnEstado(EstadoPedido.RECIBIDO);

        assertThatThrownBy(() -> pedidoService.iniciarPreparacion(pedido.getUuid()))
                .isInstanceOf(OperacionNoPermitidaException.class)
                .hasMessageContaining("CONFIRMADO");
    }

    // --- devolverPedido ---

    @Test
    void devolverPedido_enEstadoRECIBIDO_liberaReservaDeStock() {
        PedidoEntity pedido = pedidoEnEstado(EstadoPedido.RECIBIDO);
        when(pedidoRepository.save(any())).thenReturn(pedido);

        pedidoService.devolverPedido(pedido.getUuid());

        verify(stockService).liberarReservaStock(any(), eq(2), any());
        verify(stockService, never()).agregarStock(any(), anyInt(), any(), any());
    }

    @Test
    void devolverPedido_enEstadoCONFIRMADO_reintegraCantidadAlStock() {
        PedidoEntity pedido = pedidoEnEstado(EstadoPedido.CONFIRMADO);
        when(pedidoRepository.save(any())).thenReturn(pedido);

        pedidoService.devolverPedido(pedido.getUuid());

        verify(stockService).agregarStock(any(), eq(2), eq(TipoMovimientoStock.DEVOLUCION), any());
        verify(stockService, never()).liberarReservaStock(any(), anyInt(), any());
    }

    @Test
    void devolverPedido_lanzaExcepcionSiPedidoYaEstaDevuelto() {
        PedidoEntity pedido = pedidoEnEstado(EstadoPedido.DEVUELTO);

        assertThatThrownBy(() -> pedidoService.devolverPedido(pedido.getUuid()))
                .isInstanceOf(OperacionNoPermitidaException.class)
                .hasMessageContaining("DEVUELTO");
    }
}