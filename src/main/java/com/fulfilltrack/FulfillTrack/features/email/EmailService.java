package com.fulfilltrack.FulfillTrack.features.email;

import com.fulfilltrack.FulfillTrack.features.liquidacion.LiquidacionEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService{
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    @Async
    @Override
    public void enviarLiquidacionAbonada(LiquidacionEntity liquidacion) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom("hello@shreddingstudio.com");
        mensaje.setTo(liquidacion.getEmpresa().getEmail());
        mensaje.setSubject("Pago Recibido " + liquidacion.getPeriodo() + "-" + liquidacion.getEmpresa().getNombreEmpresa());
        mensaje.setText(  "Estimados,\n\n" +
                "Se ha registrado el pago de su liquidación correspondiente al período " + liquidacion.getPeriodo() + ".\n\n" +
                "Detalles:\n" +
                "  Total despachos: " + liquidacion.getTotalDespachos() + "\n" +
                "  Precio unitario: $" + liquidacion.getPrecioUnitario() + "\n" +
                "  Total: $" + liquidacion.getTotal() + "\n\n" +
                "Gracias por confiar en nosotros.\n\n" +
                "Powered by FulfillTrack");
        javaMailSender.send(mensaje);
    }

    @Async
    @Override
    public void enviarLiquidacionAPagar(LiquidacionEntity liquidacion) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom("hello@shreddingstudio.com");
        mensaje.setTo(liquidacion.getEmpresa().getEmail());
        mensaje.setSubject("Liquidación pendiente " + liquidacion.getPeriodo() + " - " + liquidacion.getEmpresa().getNombreEmpresa());
        mensaje.setText(  "Estimados,\n\n" +
                "Les informamos que se ha generado una nueva liquidación correspondiente al período " + liquidacion.getPeriodo() + ".\n\n" +
                "Detalles:\n" +
                "  Total despachos: " + liquidacion.getTotalDespachos() + "\n" +
                "  Precio unitario: $" + liquidacion.getPrecioUnitario() + "\n" +
                "  Total a abonar: $" + liquidacion.getTotal() + "\n\n" +
                "Por favor, realice el pago a la brevedad.\n\n" +
                "Gracias por confiar en nosotros.\n\n" +
                "Powered by FulfillTrack");
        javaMailSender.send(mensaje);
    }

    @Async
    @Override
    public void enviarPagoAtrasado(LiquidacionEntity liquidacion) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom("hello@shreddingstudio.com");
        mensaje.setTo(liquidacion.getEmpresa().getEmail());
        mensaje.setSubject("Pago atrasado " + liquidacion.getPeriodo() + " - " + liquidacion.getEmpresa().getNombreEmpresa());
        mensaje.setText(  "Estimados,\n\n" +
                "Les recordamos que tienen una liquidación correspondiente al período " + liquidacion.getPeriodo() + " pendiente de pago.\n\n" +
                "Detalles:\n" +
                "  Total despachos: " + liquidacion.getTotalDespachos() + "\n" +
                "  Precio unitario: $" + liquidacion.getPrecioUnitario() + "\n" +
                "  Total adeudado: $" + liquidacion.getTotal() + "\n\n" +
                "Les solicitamos regularizar el pago a la brevedad posible para evitar inconvenientes en el servicio.\n\n" +
                "Gracias por confiar en nosotros.\n\n" +
                "Powered by FulfillTrack");

        javaMailSender.send(mensaje);
    }


}
