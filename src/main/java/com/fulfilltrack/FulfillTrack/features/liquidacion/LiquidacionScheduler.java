package com.fulfilltrack.FulfillTrack.features.liquidacion;

import com.fulfilltrack.FulfillTrack.common.utils.Estado;
import com.fulfilltrack.FulfillTrack.features.email.IEmailService;
import com.fulfilltrack.FulfillTrack.features.empresa.EmpresaEntity;
import com.fulfilltrack.FulfillTrack.features.empresa.EmpresaRepository;
import com.fulfilltrack.FulfillTrack.features.despacho.DespachoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LiquidacionScheduler {

    private final LiquidacionRepository liquidacionRepository;
    private final EmpresaRepository empresaRepository;
    private final DespachoRepository despachoRepository;
    private final IEmailService emailService;

    // @Scheduled(cron = "0 */5 * * * ?") // cada 5 minutos (para demo)
    @Scheduled(cron = "0 0 8 1 * ?") //el 1ro de cada mes a las 8:00am.
    @Transactional
    public void procesarLiquidacionesMensuales() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicioMesAnterior = ahora.minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime finMesAnterior = ahora.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0).minusSeconds(1);
        String periodo = inicioMesAnterior.getYear() + "-" + String.format("%02d", inicioMesAnterior.getMonthValue());

        enviarRecordatoriosPagoAtrasado();
        generarLiquidaciones(inicioMesAnterior, finMesAnterior, periodo);
    }

    private void enviarRecordatoriosPagoAtrasado() {
        liquidacionRepository.findByEstadoPago(EstadoPago.IMPAGO)
                .forEach(liq -> {
                    try {
                        emailService.enviarPagoAtrasado(liq);
                    } catch (Exception e) {
                        // fallo de email no debe detener el resto de recordatorios
                    }
                });
    }

    private void generarLiquidaciones(LocalDateTime inicio, LocalDateTime fin, String periodo) {
        List<EmpresaEntity> empresasActivas = empresaRepository.findByEstado(Estado.ACTIVA);

        for (EmpresaEntity empresa : empresasActivas) {
            if (liquidacionRepository.existsByEmpresa_UuidAndPeriodo(empresa.getUuid(), periodo)) {
                continue;
            }
            int totalDespachos = despachoRepository.countByPedido_Empresa_UuidAndFechaDespachoBetween(
                    empresa.getUuid(), inicio, fin);
            if (totalDespachos == 0) continue;

            LiquidacionEntity liquidacion = LiquidacionEntity.builder()
                    .empresa(empresa)
                    .periodo(periodo)
                    .totalDespachos(totalDespachos)
                    .precioUnitario(empresa.getCostoPorEnvio())
                    .total(empresa.getCostoPorEnvio().multiply(BigDecimal.valueOf(totalDespachos)))
                    .build();

            LiquidacionEntity guardada = liquidacionRepository.save(liquidacion);
            try {
                emailService.enviarLiquidacionAPagar(guardada);
            } catch (Exception e) {
                // fallo de email no debe revertir la liquidación guardada
            }
        }
    }
}