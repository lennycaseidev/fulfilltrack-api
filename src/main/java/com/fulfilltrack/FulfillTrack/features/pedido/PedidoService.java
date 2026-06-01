package com.fulfilltrack.FulfillTrack.features.pedido;

import com.fulfilltrack.FulfillTrack.common.exception.EntidadNoEncontradaException;
import com.fulfilltrack.FulfillTrack.common.exception.OperacionNoPermitidaException;
import com.fulfilltrack.FulfillTrack.features.empresa.EmpresaEntity;
import com.fulfilltrack.FulfillTrack.features.empresa.IEmpresaService;
import com.fulfilltrack.FulfillTrack.features.pedido.dto.PedidoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.pedido.dto.PedidoResponseDTO;
import com.fulfilltrack.FulfillTrack.features.pedido.item.ItemPedidoEntity;
import com.fulfilltrack.FulfillTrack.features.pedido.item.ItemPedidoRepository;
import com.fulfilltrack.FulfillTrack.features.pedido.item.dto.ItemPedidoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.pedido.mapper.PedidoMapper;
import com.fulfilltrack.FulfillTrack.features.pedidoMovimiento.IPedidoMovimientoService;
import com.fulfilltrack.FulfillTrack.features.producto.IProductoService;
import com.fulfilltrack.FulfillTrack.features.producto.ProductoEntity;
import com.fulfilltrack.FulfillTrack.features.stock.IStockService;
import com.fulfilltrack.FulfillTrack.features.stockMovimiento.TipoMovimientoStock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PedidoService implements IPedidoService{
    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final PedidoMapper pedidoMapper;
    private final IEmpresaService empresaService;
    private final IProductoService productoService;
    private final IStockService stockService;
    private final IPedidoMovimientoService pedidoMovimientoService;
    public PedidoService(PedidoRepository pedidoRepository, ItemPedidoRepository itemPedidoRepository, PedidoMapper pedidoMapper, IEmpresaService empresaService, IProductoService productoService, IStockService stockService, IPedidoMovimientoService pedidoMovimientoService) {
        this.pedidoRepository = pedidoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
        this.pedidoMapper = pedidoMapper;
        this.empresaService = empresaService;
        this.productoService = productoService;
        this.stockService = stockService;
        this.pedidoMovimientoService = pedidoMovimientoService;
    }

    @Override
    @Transactional
    public PedidoResponseDTO crearPedido(PedidoRequestDTO request) {
        EmpresaEntity empresa = empresaService.obtenerEmpresaUuid(request.getEmpresaUuid());
        List<ProductoEntity> productos = new ArrayList<>();

        for(ItemPedidoRequestDTO itemRequest: request.getItems()){
            ProductoEntity producto = productoService.obtenerProductoUuid(itemRequest.getProductoUuid());
            if(!producto.getEmpresa().getUuid().equals(empresa.getUuid())){
                throw new OperacionNoPermitidaException("este producto no pertenece a la empresa indicada");
            }
            if(!stockService.tieneStockSuficiente(producto.getUuid(), itemRequest.getCantidad())){
                throw new OperacionNoPermitidaException("no hay stock suficiente para el producto " + producto.getSku());
            }
            productos.add(producto);
        }
        PedidoEntity pedido = PedidoEntity.builder()
                .numeroOrden(request.getNumeroOrden())
                .direccionEntrega(request.getDireccionEntrega())
                .nombreDestinatario(request.getNombreDestinatario())
                .empresa(empresa)
                .build();
        PedidoEntity pedidoGuardado = pedidoRepository.save(pedido);

        pedidoGuardado.setItems(guardarItemsYReservarStock(request.getItems(), pedidoGuardado, productos));

        return pedidoMapper.toResponseDTO(pedidoGuardado);
    }

    @Override
    public PedidoResponseDTO obtenerPedidoPorUuid(UUID uuid) {
        return pedidoMapper.toResponseDTO(obtenerPedidoUuid(uuid));
    }

    @Override
    public List<PedidoResponseDTO> listarPedidos() {
        return pedidoMapper.toResponseList(pedidoRepository.findAll());
    }

    @Override
    public List<PedidoResponseDTO> listarPedidosPorEmpresa(UUID empresaUuid) {
        return pedidoMapper.toResponseList(pedidoRepository.findByEmpresa_Uuid(empresaUuid));
    }

    @Override
    public PedidoEntity obtenerPedidoUuid(UUID uuid) {
        return pedidoRepository.findByUuid(uuid)
                .orElseThrow(()-> new EntidadNoEncontradaException("no se ha encontrado el pedido"));
    }

    @Override
    @Transactional
    public PedidoResponseDTO confirmarPedido(UUID uuid) {
        PedidoEntity pedido = obtenerPedidoUuid(uuid);
        validarTransicion(pedido.getEstado(), EstadoPedido.RECIBIDO, EstadoPedido.CONFIRMADO);
        pedido.getItems().forEach(item ->
                stockService.confirmarConsumoStock(item.getProducto().getUuid(), item.getCantidad(),
                        "Confirmación de pedido " + pedido.getNumeroOrden()));
        pedido.setEstado(EstadoPedido.CONFIRMADO);
        PedidoEntity guardado = pedidoRepository.save(pedido);
        pedidoMovimientoService.registrarMovimiento(guardado, EstadoPedido.RECIBIDO, EstadoPedido.CONFIRMADO);
        return pedidoMapper.toResponseDTO(guardado);
    }

    @Override
    @Transactional
    public PedidoResponseDTO iniciarPreparacion(UUID uuid) {
        PedidoEntity pedido = obtenerPedidoUuid(uuid);
        validarTransicion(pedido.getEstado(), EstadoPedido.CONFIRMADO, EstadoPedido.EN_PREPARACION);
        pedido.setEstado(EstadoPedido.EN_PREPARACION);
        PedidoEntity guardado = pedidoRepository.save(pedido);
        pedidoMovimientoService.registrarMovimiento(guardado, EstadoPedido.CONFIRMADO, EstadoPedido.EN_PREPARACION);
        return pedidoMapper.toResponseDTO(guardado);
    }

    @Override
    @Transactional
    public PedidoResponseDTO marcarListoParaDespacho(UUID uuid) {
        PedidoEntity pedido = obtenerPedidoUuid(uuid);
        validarTransicion(pedido.getEstado(), EstadoPedido.EN_PREPARACION, EstadoPedido.LISTO_PARA_DESPACHO);
        pedido.setEstado(EstadoPedido.LISTO_PARA_DESPACHO);
        PedidoEntity guardado = pedidoRepository.save(pedido);
        pedidoMovimientoService.registrarMovimiento(guardado, EstadoPedido.EN_PREPARACION, EstadoPedido.LISTO_PARA_DESPACHO);
        return pedidoMapper.toResponseDTO(guardado);
    }

    @Override
    @Transactional
    public PedidoResponseDTO marcarDespachado(UUID uuid) {
        PedidoEntity pedido = obtenerPedidoUuid(uuid);
        validarTransicion(pedido.getEstado(), EstadoPedido.LISTO_PARA_DESPACHO, EstadoPedido.DESPACHADO);
        pedido.setEstado(EstadoPedido.DESPACHADO);
        PedidoEntity guardado = pedidoRepository.save(pedido);
        pedidoMovimientoService.registrarMovimiento(guardado, EstadoPedido.LISTO_PARA_DESPACHO, EstadoPedido.DESPACHADO);
        return pedidoMapper.toResponseDTO(guardado);
    }

    @Override
    @Transactional
    public PedidoResponseDTO devolverPedido(UUID uuid) {
        PedidoEntity pedido = obtenerPedidoUuid(uuid);
        EstadoPedido estadoAnterior = pedido.getEstado();
        if (estadoAnterior == EstadoPedido.DESPACHADO || estadoAnterior == EstadoPedido.DEVUELTO) {
            throw new OperacionNoPermitidaException("no se puede devolver un pedido en estado " + estadoAnterior);
        }
        if (estadoAnterior == EstadoPedido.RECIBIDO) {
            pedido.getItems().forEach(item ->
                    stockService.liberarReservaStock(item.getProducto().getUuid(), item.getCantidad(),
                            "Liberación de reserva - pedido " + pedido.getNumeroOrden()));
        } else {
            pedido.getItems().forEach(item ->
                    stockService.agregarStock(item.getProducto().getUuid(), item.getCantidad(),
                            TipoMovimientoStock.DEVOLUCION, "Devolución de pedido " + pedido.getNumeroOrden()));
        }
        pedido.setEstado(EstadoPedido.DEVUELTO);
        PedidoEntity guardado = pedidoRepository.save(pedido);
        pedidoMovimientoService.registrarMovimiento(guardado, estadoAnterior, EstadoPedido.DEVUELTO);
        return pedidoMapper.toResponseDTO(guardado);
    }

    private void validarTransicion(EstadoPedido actual, EstadoPedido requerido, EstadoPedido siguiente) {
        if (actual != requerido) {
            throw new OperacionNoPermitidaException(
                    "el pedido debe estar en estado " + requerido + " para pasar a " + siguiente + ". Estado actual: " + actual);
        }
    }

    private List<ItemPedidoEntity> guardarItemsYReservarStock(List<ItemPedidoRequestDTO> items, PedidoEntity pedidoGuardado, List<ProductoEntity> productos){
        List<ItemPedidoEntity> itemsGuardados = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            ItemPedidoRequestDTO itemRequest = items.get(i);
            itemsGuardados.add(itemPedidoRepository.save(ItemPedidoEntity.builder()
                    .pedido(pedidoGuardado)
                    .producto(productos.get(i))
                    .cantidad(itemRequest.getCantidad())
                    .build()));
            stockService.reservarStock(itemRequest.getProductoUuid(), itemRequest.getCantidad(),
                    "Reserva por pedido " + pedidoGuardado.getNumeroOrden());
        }
        return itemsGuardados;
    }

}
