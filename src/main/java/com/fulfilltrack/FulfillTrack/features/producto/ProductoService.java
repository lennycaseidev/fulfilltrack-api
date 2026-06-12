package com.fulfilltrack.FulfillTrack.features.producto;

import com.fulfilltrack.FulfillTrack.auth.TenantContext;
import com.fulfilltrack.FulfillTrack.common.exception.EntidadDuplicadaException;
import com.fulfilltrack.FulfillTrack.common.exception.EntidadNoEncontradaException;
import com.fulfilltrack.FulfillTrack.common.exception.OperacionNoPermitidaException;
import com.fulfilltrack.FulfillTrack.common.utils.Estado;
import com.fulfilltrack.FulfillTrack.features.empresa.EmpresaEntity;
import com.fulfilltrack.FulfillTrack.features.empresa.IEmpresaService;
import com.fulfilltrack.FulfillTrack.features.producto.dto.ProductoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.producto.dto.ProductoResponseDTO;
import com.fulfilltrack.FulfillTrack.features.producto.mapper.ProductoMapper;
import com.fulfilltrack.FulfillTrack.features.stock.IStockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ProductoService implements IProductoService{
    private final ProductoRepository productoRepository;
    private final IEmpresaService empresaService;
    private final ProductoMapper productoMapper;
    private final IStockService stockService;
    private final TenantContext tenantContext;

    public ProductoService(ProductoRepository productoRepository, IEmpresaService empresaService, ProductoMapper productoMapper, IStockService stockService, TenantContext tenantContext) {
        this.productoRepository = productoRepository;
        this.empresaService = empresaService;
        this.productoMapper = productoMapper;
        this.stockService = stockService;
        this.tenantContext = tenantContext;
    }

    @Override
    @Transactional
    public ProductoResponseDTO crearProducto(ProductoRequestDTO request) {
        UUID empresaId = tenantContext.getEmpresaUuid().orElse(request.getEmpresaUuid());
        EmpresaEntity empresa = empresaService.obtenerEmpresaUuid(empresaId);
        if(productoRepository.existsBySkuAndEmpresa_Uuid(request.getSku(), empresaId)){
            throw new EntidadDuplicadaException("El sku ya está registrado para un producto de esta empresa");
        }
        ProductoEntity producto = productoMapper.toEntity(request);
        producto.setEmpresa(empresa);
        ProductoEntity productoGuardado = productoRepository.save(producto);
        stockService.crearStockInicial(productoGuardado);
        return productoMapper.toResponseDTO(productoGuardado);
    }

    @Override
    public ProductoResponseDTO obtenerProductoPorUuid(UUID uuid) {
        ProductoEntity producto = obtenerProductoUuid(uuid);
        return productoMapper.toResponseDTO(producto);
    }

    @Override
    public ProductoResponseDTO obtenerProductoPorSku(String sku) {
        ProductoEntity producto = tenantContext.getEmpresaUuid()
                .map(empresaUuid -> productoRepository.findBySkuAndEmpresa_Uuid(sku, empresaUuid)
                        .orElseThrow(() -> new EntidadNoEncontradaException("El producto no ha sido encontrado")))
                .orElseGet(() -> productoRepository.findBySku(sku)
                        .orElseThrow(() -> new EntidadNoEncontradaException("El producto no ha sido encontrado")));
        return productoMapper.toResponseDTO(producto);
    }

    @Override
    public List<ProductoResponseDTO> listarProductosPorEmpresa(UUID empresaUuid) {
        UUID efectivo = tenantContext.getEmpresaUuid().orElse(empresaUuid);
        if (!empresaService.existeEmpresaPorUuid(efectivo)) {
            throw new EntidadNoEncontradaException("La empresa no ha sido encontrada");
        }
        return productoMapper.toResponseList(productoRepository.findByEmpresa_Uuid(efectivo));
    }

    @Override
    public List<ProductoResponseDTO> listarProductos() {
        return tenantContext.getEmpresaUuid()
                .map(uuid -> productoMapper.toResponseList(productoRepository.findByEmpresa_Uuid(uuid)))
                .orElseGet(() -> productoMapper.toResponseList(productoRepository.findAll()));
    }

    @Override
    public ProductoResponseDTO actualizarProducto(UUID uuid, ProductoRequestDTO request) {
        ProductoEntity producto = obtenerProductoUuid(uuid);
        UUID empresaId = tenantContext.getEmpresaUuid().orElse(request.getEmpresaUuid());

        if(!producto.getSku().equals(request.getSku()) && productoRepository.existsBySkuAndEmpresa_Uuid(request.getSku(), empresaId)){
            throw new EntidadDuplicadaException("El sku ya está registrado para un producto de esta empresa");
        }
        producto.setNombreProducto(request.getNombreProducto());
        producto.setDescripcion(request.getDescripcion());
        producto.setSku(request.getSku());

        return productoMapper.toResponseDTO(productoRepository.save(producto));
    }

    @Override
    public void activarProducto(UUID uuid) {
        ProductoEntity producto = obtenerProductoUuid(uuid);
        if(producto.getEstado() == Estado.ACTIVA){
            throw new OperacionNoPermitidaException("El producto ya se encuentra activo");
        }
        producto.setEstado(Estado.ACTIVA);
        productoRepository.save(producto);
    }

    @Override
    public void desactivarProducto(UUID uuid) {
        ProductoEntity producto = obtenerProductoUuid(uuid);
        if(producto.getEstado() == Estado.INACTIVA){
            throw new OperacionNoPermitidaException("El producto ya se encuentra inactivo");
        }
            if(stockService.tieneStockActivo(uuid)){
                throw new OperacionNoPermitidaException("No se puede desactivar un producto con stock disponible o reservado");
            }

        producto.setEstado(Estado.INACTIVA);
        productoRepository.save(producto);
    }

    @Override
    public ProductoEntity obtenerProductoUuid(UUID uuid) {
        return productoRepository.findByUuid(uuid)
                .orElseThrow(()-> new EntidadNoEncontradaException("El producto no ha sido encontrado"));
    }


}
