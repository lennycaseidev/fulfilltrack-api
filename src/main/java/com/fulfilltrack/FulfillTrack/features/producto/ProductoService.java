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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductoService implements IProductoService{
    private final ProductoRepository productoRepository;
    private final EmpresaRepository empresaRepository;
    private final ProductoMapper productoMapper;

    public ProductoService(ProductoRepository productoRepository, EmpresaRepository empresaRepository, ProductoMapper productoMapper) {
        this.productoRepository = productoRepository;
        this.empresaRepository = empresaRepository;
        this.productoMapper = productoMapper;
    }

    @Override
    public ProductoResponseDTO crearProducto(ProductoRequestDTO request) {
        if(productoRepository.existsBySku(request.getSku())){
            throw new EntidadDuplicadaException("El sku ya esta registrado para otro producto");
        }
        EmpresaEntity empresa = empresaRepository.findByUuid(request.getEmpresaUuid())
                .orElseThrow(() -> new EntidadNoEncontradaException("La empresa no ha sido encontrada"));
        ProductoEntity producto = productoMapper.toEntity(request);
        producto.setEmpresa(empresa);
        return productoMapper.toResponseDTO(productoRepository.save(producto));
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

        if (!producto.getSku().equals(request.getSku()) && productoRepository.existsBySku(request.getSku())) {
            throw new EntidadDuplicadaException("El sku ya está registrado para otro producto");
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
        producto.setEstado(Estado.INACTIVA);
        productoRepository.save(producto);
    }
}
