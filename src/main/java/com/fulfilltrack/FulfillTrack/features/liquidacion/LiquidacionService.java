package com.fulfilltrack.FulfillTrack.features.liquidacion;

import com.fulfilltrack.FulfillTrack.auth.TenantContext;
import com.fulfilltrack.FulfillTrack.common.exception.EntidadNoEncontradaException;
import com.fulfilltrack.FulfillTrack.common.exception.OperacionNoPermitidaException;
import com.fulfilltrack.FulfillTrack.features.despacho.IDespachoService;
import com.fulfilltrack.FulfillTrack.features.email.IEmailService;
import com.fulfilltrack.FulfillTrack.features.empresa.EmpresaEntity;
import com.fulfilltrack.FulfillTrack.features.empresa.IEmpresaService;
import com.fulfilltrack.FulfillTrack.features.liquidacion.dto.LiquidacionRequestDTO;
import com.fulfilltrack.FulfillTrack.features.liquidacion.dto.LiquidacionResponseDTO;
import com.fulfilltrack.FulfillTrack.features.liquidacion.mapper.LiquidacionMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class LiquidacionService implements ILiquidacionService {

    private final LiquidacionRepository liquidacionRepository;
    private final LiquidacionMapper liquidacionMapper;
    private final IEmpresaService empresaService;
    private final IEmailService emailService;
    private final TenantContext tenantContext;
    private final IDespachoService despachoService;

    public LiquidacionService(LiquidacionRepository liquidacionRepository, LiquidacionMapper liquidacionMapper, IEmpresaService empresaService, IEmailService emailService, TenantContext tenantContext, @Lazy IDespachoService despachoService) {
        this.liquidacionRepository = liquidacionRepository;
        this.liquidacionMapper = liquidacionMapper;
        this.empresaService = empresaService;
        this.emailService = emailService;
        this.tenantContext = tenantContext;
        this.despachoService = despachoService;
    }

    @Override
    @Transactional
    public LiquidacionResponseDTO crearLiquidacion(LiquidacionRequestDTO request) {
        EmpresaEntity empresa = empresaService.obtenerEmpresaUuid(request.getEmpresaUuid());
        int totalDespachos = despachoService.contarDespachosPorEmpresa(empresa.getUuid());
        BigDecimal precioUnitario = empresa.getCostoPorEnvio();
        LiquidacionEntity liquidacion = LiquidacionEntity.builder()
                .empresa(empresa)
                .periodo(request.getPeriodo())
                .totalDespachos(totalDespachos)
                .precioUnitario(precioUnitario)
                .total(precioUnitario.multiply(BigDecimal.valueOf(totalDespachos)))
                .build();
        LiquidacionEntity liquidacionGuardada = liquidacionRepository.save(liquidacion);
        emailService.enviarLiquidacionAPagar(liquidacionGuardada);
        return liquidacionMapper.toResponseDTO(liquidacionGuardada);
    }

    @Override
    public LiquidacionResponseDTO obtenerLiquidacionPorUuid(UUID uuid) {
        LiquidacionEntity liquidacion = obtenerLiquidacionUuid(uuid);
        return liquidacionMapper.toResponseDTO(liquidacion);
    }

    @Override
    public List<LiquidacionResponseDTO> listarLiquidaciones() {
        return tenantContext.getEmpresaUuid()
                .map(uuid -> liquidacionMapper.toResponseList(liquidacionRepository.findByEmpresa_Uuid(uuid)))
                .orElseGet(() -> liquidacionMapper.toResponseList(liquidacionRepository.findAll()));
    }

    @Override
    public List<LiquidacionResponseDTO> listarLiquidacionesPorEmpresa(UUID empresaUuid) {
        UUID efectivo = tenantContext.getEmpresaUuid().orElse(empresaUuid);
        if (!empresaService.existeEmpresaPorUuid(efectivo)) {
            throw new EntidadNoEncontradaException("Empresa no encontrada");
        }
        return liquidacionMapper.toResponseList(liquidacionRepository.findByEmpresa_Uuid(efectivo));
    }

    @Override
    @Transactional
    public LiquidacionResponseDTO actualizarLiquidacion(UUID uuid, LiquidacionRequestDTO request) {
        LiquidacionEntity liquidacion = obtenerLiquidacionUuid(uuid);
        if (liquidacion.getEstadoPago() == EstadoPago.PAGO) {
            throw new OperacionNoPermitidaException("No se puede modificar una liquidación ya pagada");
        }
        EmpresaEntity empresa = empresaService.obtenerEmpresaUuid(request.getEmpresaUuid());
        int totalDespachos = despachoService.contarDespachosPorEmpresa(empresa.getUuid());
        BigDecimal precioUnitario = empresa.getCostoPorEnvio();

        liquidacion.setPeriodo(request.getPeriodo());
        liquidacion.setTotalDespachos(totalDespachos);
        liquidacion.setPrecioUnitario(precioUnitario);
        liquidacion.setTotal(precioUnitario.multiply(BigDecimal.valueOf(totalDespachos)));
        liquidacion.setEmpresa(empresa);

        return liquidacionMapper.toResponseDTO(liquidacionRepository.save(liquidacion));
    }

    @Override
    @Transactional
    public void marcarComoPagada(UUID uuid) {
        LiquidacionEntity liquidacion = obtenerLiquidacionUuid(uuid);
        if (liquidacion.getEstadoPago() == EstadoPago.PAGO) {
            throw new OperacionNoPermitidaException("La liquidación ya se encuentra pagada");
        }
        liquidacion.setEstadoPago(EstadoPago.PAGO);
        liquidacionRepository.save(liquidacion);
        emailService.enviarLiquidacionAbonada(liquidacion);
    }

    @Override
    public void marcarComoImpaga(UUID uuid) {
        LiquidacionEntity liquidacion = obtenerLiquidacionUuid(uuid);
        if (liquidacion.getEstadoPago() == EstadoPago.IMPAGO) {
            throw new OperacionNoPermitidaException("La liquidación ya se encuentra impaga");
        }
        liquidacion.setEstadoPago(EstadoPago.IMPAGO);
        liquidacionRepository.save(liquidacion);
    }

    @Override
    public LiquidacionEntity obtenerLiquidacionUuid(UUID uuid) {
        return liquidacionRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("Liquidación no encontrada"));
    }
}