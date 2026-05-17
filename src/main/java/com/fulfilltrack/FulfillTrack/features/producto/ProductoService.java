package com.fulfilltrack.FulfillTrack.features.producto;

import com.fulfilltrack.FulfillTrack.common.exception.EntidadDuplicadaException;
import com.fulfilltrack.FulfillTrack.common.exception.EntidadNoEncontradaException;
import com.fulfilltrack.FulfillTrack.common.exception.OperacionNoPermitidaException;
import com.fulfilltrack.FulfillTrack.common.utils.Estado;
import com.fulfilltrack.FulfillTrack.features.empresa.EmpresaEntity;
import com.fulfilltrack.FulfillTrack.features.empresa.EmpresaRepository;
import com.fulfilltrack.FulfillTrack.features.producto.dto.ProductoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.producto.dto.ProductoResponseDTO;
import com.fulfilltrack.FulfillTrack.features.producto.mapper.ProductoMapper;
import com.fulfilltrack.FulfillTrack.features.stock.StockEntity;
import com.fulfilltrack.FulfillTrack.features.stock.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ProductoService implements IProductoService{
    private final ProductoRepository productoRepository;
    private final EmpresaRepository empresaRepository;
    private final ProductoMapper productoMapper;
    private final StockRepository stockRepository;

    public ProductoService(ProductoRepository productoRepository, EmpresaRepository empresaRepository, ProductoMapper productoMapper, StockRepository stockRepository) {
        this.productoRepository = productoRepository;
        this.empresaRepository = empresaRepository;
        this.productoMapper = productoMapper;
        this.stockRepository = stockRepository;
    }

    @Override
    @Transactional
    public ProductoResponseDTO crearProducto(ProductoRequestDTO request) {
        EmpresaEntity empresa = empresaRepository.findByUuid(request.getEmpresaUuid())
                .orElseThrow(() -> new EntidadNoEncontradaException("La empresa no ha sido encontrada"));
        if(productoRepository.existsBySkuAndEmpresa_Uuid(request.getSku(), request.getEmpresaUuid())){
            throw new EntidadDuplicadaException("El sku ya está registrado para un producto de esta empresa");
        }
        ProductoEntity producto = productoMapper.toEntity(request);
        producto.setEmpresa(empresa);
        ProductoEntity productoGuardado = productoRepository.save(producto);
        stockRepository.save(StockEntity.builder()
                .producto(productoGuardado)
                .cantidadDisponible(0)
                .cantidadReservada(0)
                .build());
        return productoMapper.toResponseDTO(productoGuardado);
    }

    @Override
    public ProductoResponseDTO obtenerProductoPorUuid(UUID uuid) {
        ProductoEntity producto = productoRepository.findByUuid(uuid)
                .orElseThrow(()-> new EntidadNoEncontradaException("El producto no ha sido encontrado"));
        return productoMapper.toResponseDTO(producto);
    }

    @Override
    public ProductoResponseDTO obtenerProductoPorSku(String sku) {
        ProductoEntity producto = productoRepository.findBySku(sku)
                .orElseThrow(()-> new EntidadNoEncontradaException("El producto no ha sido encontrado"));
        return productoMapper.toResponseDTO(producto);
    }

    @Override
    public List<ProductoResponseDTO> listarProductosPorEmpresa(UUID empresaUuid) {
        if (!empresaRepository.existsByUuid(empresaUuid)) {
            throw new EntidadNoEncontradaException("La empresa no ha sido encontrada");
        }
        return productoMapper.toResponseList(productoRepository.findByEmpresa_Uuid(empresaUuid));
    }

    @Override
    public List<ProductoResponseDTO> listarProductos() {
        return productoMapper.toResponseList(productoRepository.findAll());
    }

    @Override
    public ProductoResponseDTO actualizarProducto(UUID uuid, ProductoRequestDTO request) {
        ProductoEntity producto = productoRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("El producto no ha sido encontrado"));

        if(!producto.getSku().equals(request.getSku()) && productoRepository.existsBySkuAndEmpresa_Uuid(request.getSku(), request.getEmpresaUuid())){
            throw new EntidadDuplicadaException("El sku ya está registrado para un producto de esta empresa");
        }
        producto.setNombreProducto(request.getNombreProducto());
        producto.setDescripcion(request.getDescripcion());
        producto.setSku(request.getSku());

        return productoMapper.toResponseDTO(productoRepository.save(producto));
    }

    @Override
    public void activarProducto(UUID uuid) {
        ProductoEntity producto = productoRepository.findByUuid(uuid)
                .orElseThrow(()-> new EntidadNoEncontradaException("El producto no ha sido encontrado"));
        if(producto.getEstado() == Estado.ACTIVA){
            throw new OperacionNoPermitidaException("El producto ya se encuentra activo");
        }
        producto.setEstado(Estado.ACTIVA);
        productoRepository.save(producto);
    }

    @Override
    public void desactivarProducto(UUID uuid) {
        ProductoEntity producto = productoRepository.findByUuid(uuid)
                .orElseThrow(()-> new EntidadNoEncontradaException("El producto no ha sido encontrado"));
        if(producto.getEstado() == Estado.INACTIVA){
            throw new OperacionNoPermitidaException("El producto ya se encuentra inactivo");
        }
        stockRepository.findByProducto_Uuid(uuid).ifPresent(stock -> {
            if(stock.getCantidadDisponible() > 0 || stock.getCantidadReservada() > 0){
                throw new OperacionNoPermitidaException("No se puede desactivar un producto con stock disponible o reservado");
            }
        });
        producto.setEstado(Estado.INACTIVA);
        productoRepository.save(producto);
    }
}
