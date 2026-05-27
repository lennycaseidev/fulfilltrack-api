package com.fulfilltrack.FulfillTrack.features.pedidoMovimiento;

import com.fulfilltrack.FulfillTrack.features.pedidoMovimiento.dto.PedidoMovimientoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoMovimientoController {

    private final IPedidoMovimientoService pedidoMovimientoService;

    @GetMapping("/{uuid}/movimientos")
    ResponseEntity<List<PedidoMovimientoResponseDTO>> listarMovimientos(@PathVariable UUID uuid) {
        return null;
    }
}
