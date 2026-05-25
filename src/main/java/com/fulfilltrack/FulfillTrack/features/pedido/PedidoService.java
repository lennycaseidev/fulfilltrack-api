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
import com.fulfilltrack.FulfillTrack.features.producto.IProductoService;
import com.fulfilltrack.FulfillTrack.features.producto.ProductoEntity;
import com.fulfilltrack.FulfillTrack.features.stock.IStockService;
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

    public PedidoService(PedidoRepository pedidoRepository, ItemPedidoRepository itemPedidoRepository, PedidoMapper pedidoMapper, IEmpresaService empresaService, IProductoService productoService, IStockService stockService) {
        this.pedidoRepository = pedidoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
        this.pedidoMapper = pedidoMapper;
        this.empresaService = empresaService;
        this.productoService = productoService;
        this.stockService = stockService;
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
                throw new OperacionNoPermitidaException("no hay stock suficiente para el producto" + producto.getSku());
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

        guardarItemsYReservarStock(request.getItems(), pedidoGuardado, productos);

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
        EmpresaEntity empresa = empresaService.obtenerEmpresaUuid(empresaUuid);
        return pedidoMapper.toResponseList(pedidoRepository.findByEmpresa_Uuid(empresaUuid));
    }

    @Override
    public PedidoEntity obtenerPedidoUuid(UUID uuid) {
        return pedidoRepository.findByUuid(uuid)
                .orElseThrow(()-> new EntidadNoEncontradaException("no se ha encontrado el pedido"));
    }

    private void guardarItemsYReservarStock(List<ItemPedidoRequestDTO> items, PedidoEntity pedidoGuardado, List<ProductoEntity> productos){
        for (int i = 0; i < items.size(); i++) {
            ItemPedidoRequestDTO itemRequest = items.get(i);
            itemPedidoRepository.save(ItemPedidoEntity.builder()
                    .pedido(pedidoGuardado)
                    .producto(productos.get(i))
                    .cantidad(itemRequest.getCantidad())
                    .build());
            stockService.reservarStock(itemRequest.getProductoUuid(), itemRequest.getCantidad());
        }
    }

}
