package com.fulfilltrack.FulfillTrack.features.stockMovimiento;

import com.fulfilltrack.FulfillTrack.auth.TenantContext;
import com.fulfilltrack.FulfillTrack.auth.credenciales.CredencialEntity;
import com.fulfilltrack.FulfillTrack.common.exception.EntidadNoEncontradaException;
import com.fulfilltrack.FulfillTrack.features.empresa.IEmpresaService;
import com.fulfilltrack.FulfillTrack.features.producto.IProductoService;
import com.fulfilltrack.FulfillTrack.features.producto.ProductoEntity;
import com.fulfilltrack.FulfillTrack.features.stockMovimiento.dto.StockMovimientoResponseDTO;
import com.fulfilltrack.FulfillTrack.features.stockMovimiento.mapper.StockMovimientoMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StockMovimientoService implements IStockMovimientoService {

    private final StockMovimientoRepository movimientoRepository;
    private final StockMovimientoMapper movimientoMapper;
    private final IProductoService productoService;
    private final IEmpresaService empresaService;
    private final TenantContext tenantContext;

    public StockMovimientoService(StockMovimientoRepository movimientoRepository, StockMovimientoMapper movimientoMapper, @Lazy IProductoService productoService,
                                  IEmpresaService empresaService, TenantContext tenantContext) {
        this.movimientoRepository = movimientoRepository;
        this.movimientoMapper = movimientoMapper;
        this.productoService = productoService;
        this.empresaService = empresaService;
        this.tenantContext = tenantContext;
    }

    @Override
    public void registrarMovimiento(ProductoEntity producto, TipoMovimientoStock tipo, String motivo, int cantidad) {
        movimientoRepository.save(StockMovimientoEntity.builder()
                .producto(producto)
                .tipo(tipo)
                .motivo(motivo)
                .cantidad(cantidad)
                .usuarioUuid(resolverUsuarioUuid())
                .build());
    }

    private UUID resolverUsuarioUuid() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CredencialEntity credencial
                && credencial.getUsuario() != null) {
            return credencial.getUsuario().getUuid();
        }
        return null;
    }

    @Override
    public List<StockMovimientoResponseDTO> listarMovimientosPorProducto(UUID productoUuid) {
        productoService.obtenerProductoUuid(productoUuid);
        return movimientoMapper.toResponseList(
                movimientoRepository.findByProducto_UuidOrderByFechaDesc(productoUuid));
    }

    @Override
    public List<StockMovimientoResponseDTO> listarMovimientosPorEmpresa(UUID empresaUuid) {
        UUID efectivo = tenantContext.getEmpresaUuid().orElse(empresaUuid);
        if (!empresaService.existeEmpresaPorUuid(efectivo)) {
            throw new EntidadNoEncontradaException("La empresa no ha sido encontrada");
        }
        return movimientoMapper.toResponseList(
                movimientoRepository.findByProducto_Empresa_UuidOrderByFechaDesc(efectivo));
    }
}