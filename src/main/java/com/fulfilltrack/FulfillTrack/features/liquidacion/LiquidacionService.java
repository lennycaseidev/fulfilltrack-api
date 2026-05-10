package com.fulfilltrack.FulfillTrack.features.liquidacion;

import com.fulfilltrack.FulfillTrack.common.exception.EntidadNoEncontradaException;
import com.fulfilltrack.FulfillTrack.common.exception.OperacionNoPermitidaException;
import com.fulfilltrack.FulfillTrack.features.empresa.EmpresaEntity;
import com.fulfilltrack.FulfillTrack.features.empresa.EmpresaRepository;
import com.fulfilltrack.FulfillTrack.features.liquidacion.dto.LiquidacionRequestDTO;
import com.fulfilltrack.FulfillTrack.features.liquidacion.dto.LiquidacionResponseDTO;
import com.fulfilltrack.FulfillTrack.features.liquidacion.mapper.LiquidacionMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class LiquidacionService implements ILiquidacionService {

    private final LiquidacionRepository liquidacionRepository;
    private final LiquidacionMapper liquidacionMapper;
    private final EmpresaRepository empresaRepository;

    public LiquidacionService(LiquidacionRepository liquidacionRepository,
                               LiquidacionMapper liquidacionMapper,
                               EmpresaRepository empresaRepository) {
        this.liquidacionRepository = liquidacionRepository;
        this.liquidacionMapper = liquidacionMapper;
        this.empresaRepository = empresaRepository;
    }

    @Override
    public LiquidacionResponseDTO crearLiquidacion(LiquidacionRequestDTO request) {
        EmpresaEntity empresa = empresaRepository.findByUuid(request.getEmpresaUuid())
                .orElseThrow(() -> new EntidadNoEncontradaException("Empresa no encontrada con UUID " + request.getEmpresaUuid()));

        LiquidacionEntity liquidacion = liquidacionMapper.toEntity(request);
        liquidacion.setEmpresa(empresa);
        liquidacion.setTotal(
                request.getPrecioUnitario().multiply(BigDecimal.valueOf(request.getTotalDespachos()))
        );

        return liquidacionMapper.toResponseDTO(liquidacionRepository.save(liquidacion));
    }

    @Override
    public LiquidacionResponseDTO obtenerLiquidacionPorUuid(UUID uuid) {
        LiquidacionEntity liquidacion = liquidacionRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("Liquidación no encontrada con UUID " + uuid));
        return liquidacionMapper.toResponseDTO(liquidacion);
    }

    @Override
    public List<LiquidacionResponseDTO> listarLiquidaciones() {
        return liquidacionMapper.toResponseList(liquidacionRepository.findAll());
    }

    @Override
    public List<LiquidacionResponseDTO> listarLiquidacionesPorEmpresa(UUID empresaUuid) {
        if (!empresaRepository.existsByUuid(empresaUuid)) {
            throw new EntidadNoEncontradaException("Empresa no encontrada con UUID " + empresaUuid);
        }
        return liquidacionMapper.toResponseList(liquidacionRepository.findByEmpresa_Uuid(empresaUuid));
    }

    @Override
    public LiquidacionResponseDTO actualizarLiquidacion(UUID uuid, LiquidacionRequestDTO request) {
        LiquidacionEntity liquidacion = liquidacionRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("Liquidación no encontrada con UUID " + uuid));
        if (liquidacion.getEstadoPago() == EstadoPago.PAGO) {
            throw new OperacionNoPermitidaException("No se puede modificar una liquidación ya pagada");
        }
        EmpresaEntity empresa = empresaRepository.findByUuid(request.getEmpresaUuid())
                .orElseThrow(() -> new EntidadNoEncontradaException("Empresa no encontrada con UUID " + request.getEmpresaUuid()));

        liquidacion.setPeriodo(request.getPeriodo());
        liquidacion.setTotalDespachos(request.getTotalDespachos());
        liquidacion.setPrecioUnitario(request.getPrecioUnitario());
        liquidacion.setTotal(request.getPrecioUnitario().multiply(BigDecimal.valueOf(request.getTotalDespachos())));
        liquidacion.setEmpresa(empresa);

        return liquidacionMapper.toResponseDTO(liquidacionRepository.save(liquidacion));
    }

    @Override
    public void marcarComoPagada(UUID uuid) {
        LiquidacionEntity liquidacion = liquidacionRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("Liquidación no encontrada con UUID " + uuid));
        if (liquidacion.getEstadoPago() == EstadoPago.PAGO) {
            throw new OperacionNoPermitidaException("La liquidación ya se encuentra pagada");
        }
        liquidacion.setEstadoPago(EstadoPago.PAGO);
        liquidacionRepository.save(liquidacion);
    }

    @Override
    public void marcarComoImpaga(UUID uuid) {
        LiquidacionEntity liquidacion = liquidacionRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("Liquidación no encontrada con UUID " + uuid));
        if (liquidacion.getEstadoPago() == EstadoPago.IMPAGO) {
            throw new OperacionNoPermitidaException("La liquidación ya se encuentra impaga");
        }
        liquidacion.setEstadoPago(EstadoPago.IMPAGO);
        liquidacionRepository.save(liquidacion);
    }
}