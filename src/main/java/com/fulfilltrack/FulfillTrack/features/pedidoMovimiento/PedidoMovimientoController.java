package com.fulfilltrack.FulfillTrack.features.pedidoMovimiento;

import com.fulfilltrack.FulfillTrack.features.pedidoMovimiento.dto.PedidoMovimientoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Pedidos", description = "Gestión de pedidos y su ciclo de vida")
@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoMovimientoController {

    private final IPedidoMovimientoService pedidoMovimientoService;

    @PreAuthorize("hasAuthority('VER_PEDIDOS')")
    @Operation(summary = "Listar historial de estados de un pedido", description = "Retorna cada transición de estado registrada para el pedido")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Historial de movimientos"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @GetMapping("/{uuid}/movimientos")
    ResponseEntity<List<PedidoMovimientoResponseDTO>> listarMovimientos(@PathVariable UUID uuid) {
        return ResponseEntity.ok(pedidoMovimientoService.listarMovimientosPorPedido(uuid));
    }
}