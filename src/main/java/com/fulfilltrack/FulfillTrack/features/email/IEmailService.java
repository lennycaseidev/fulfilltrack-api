package com.fulfilltrack.FulfillTrack.features.email;

import com.fulfilltrack.FulfillTrack.features.liquidacion.LiquidacionEntity;

public interface IEmailService {
    void enviarLiquidacionAbonada(LiquidacionEntity liquidacion);
    void enviarLiquidacionAPagar(LiquidacionEntity liquidacion);
    void enviarPagoAtrasado(LiquidacionEntity liquidacion);
}
