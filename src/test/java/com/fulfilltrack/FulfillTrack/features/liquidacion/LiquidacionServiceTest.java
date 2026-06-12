package com.fulfilltrack.FulfillTrack.features.liquidacion;

import com.fulfilltrack.FulfillTrack.auth.TenantContext;
import com.fulfilltrack.FulfillTrack.common.exception.OperacionNoPermitidaException;
import com.fulfilltrack.FulfillTrack.features.email.IEmailService;
import com.fulfilltrack.FulfillTrack.features.empresa.EmpresaEntity;
import com.fulfilltrack.FulfillTrack.features.empresa.IEmpresaService;
import com.fulfilltrack.FulfillTrack.features.liquidacion.dto.LiquidacionRequestDTO;
import com.fulfilltrack.FulfillTrack.features.liquidacion.mapper.LiquidacionMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LiquidacionServiceTest {

    @Mock LiquidacionRepository liquidacionRepository;
    @Mock LiquidacionMapper liquidacionMapper;
    @Mock IEmpresaService empresaService;
    @Mock IEmailService emailService;
    @Mock TenantContext tenantContext;

    @InjectMocks LiquidacionService liquidacionService;

    // --- crearLiquidacion ---

    @Test
    void crearLiquidacion_calculaTotalComoProductoDeDespachosPorPrecio() {
        UUID empresaUuid = UUID.randomUUID();
        LiquidacionRequestDTO request = new LiquidacionRequestDTO(
                "2025-06", 10, new BigDecimal("1500.00"), empresaUuid
        );

        LiquidacionEntity entidadBase = new LiquidacionEntity();
        entidadBase.setPeriodo("2025-06");
        entidadBase.setTotalDespachos(10);
        entidadBase.setPrecioUnitario(new BigDecimal("1500.00"));

        when(liquidacionMapper.toEntity(request)).thenReturn(entidadBase);
        when(empresaService.obtenerEmpresaUuid(empresaUuid)).thenReturn(new EmpresaEntity());
        when(liquidacionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(liquidacionMapper.toResponseDTO(any())).thenAnswer(inv -> {
            LiquidacionEntity e = inv.getArgument(0);
            var dto = new com.fulfilltrack.FulfillTrack.features.liquidacion.dto.LiquidacionResponseDTO();
            dto.setTotal(e.getTotal());
            return dto;
        });

        var resultado = liquidacionService.crearLiquidacion(request);

        assertThat(resultado.getTotal()).isEqualByComparingTo(new BigDecimal("15000.00"));
        verify(emailService).enviarLiquidacionAPagar(any());
    }

    // --- marcarComoPagada ---

    @Test
    void marcarComoPagada_lanzaExcepcionSiYaEstaPagada() {
        UUID uuid = UUID.randomUUID();
        LiquidacionEntity liquidacion = new LiquidacionEntity();
        liquidacion.setEstadoPago(EstadoPago.PAGO);
        when(liquidacionRepository.findByUuid(uuid)).thenReturn(Optional.of(liquidacion));

        assertThatThrownBy(() -> liquidacionService.marcarComoPagada(uuid))
                .isInstanceOf(OperacionNoPermitidaException.class)
                .hasMessageContaining("ya se encuentra pagada");
    }

    @Test
    void marcarComoPagada_cambiaEstadoYEnviaEmail() {
        UUID uuid = UUID.randomUUID();
        LiquidacionEntity liquidacion = new LiquidacionEntity();
        liquidacion.setEstadoPago(EstadoPago.IMPAGO);
        when(liquidacionRepository.findByUuid(uuid)).thenReturn(Optional.of(liquidacion));
        when(liquidacionRepository.save(any())).thenReturn(liquidacion);

        liquidacionService.marcarComoPagada(uuid);

        assertThat(liquidacion.getEstadoPago()).isEqualTo(EstadoPago.PAGO);
        verify(emailService).enviarLiquidacionAbonada(liquidacion);
    }

    // --- actualizarLiquidacion ---

    @Test
    void actualizarLiquidacion_lanzaExcepcionSiYaEstaPagada() {
        UUID uuid = UUID.randomUUID();
        LiquidacionEntity liquidacion = new LiquidacionEntity();
        liquidacion.setEstadoPago(EstadoPago.PAGO);
        when(liquidacionRepository.findByUuid(uuid)).thenReturn(Optional.of(liquidacion));

        LiquidacionRequestDTO request = new LiquidacionRequestDTO(
                "2025-06", 5, new BigDecimal("1000.00"), UUID.randomUUID()
        );

        assertThatThrownBy(() -> liquidacionService.actualizarLiquidacion(uuid, request))
                .isInstanceOf(OperacionNoPermitidaException.class)
                .hasMessageContaining("ya pagada");
    }
}