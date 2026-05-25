package com.fulfilltrack.FulfillTrack.features.pedido;

import com.fulfilltrack.FulfillTrack.features.pedido.dto.PedidoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.pedido.dto.PedidoResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final IPedidoService pedidoService;

    @GetMapping
    ResponseEntity<List<PedidoResponseDTO>> listarPedidos() {
        return ResponseEntity.ok(pedidoService.listarPedidos());
    }

    @GetMapping("/empresa/{empresaUuid}")
    ResponseEntity<List<PedidoResponseDTO>> listarPedidosPorEmpresa(@PathVariable UUID empresaUuid) {
        return ResponseEntity.ok(pedidoService.listarPedidosPorEmpresa(empresaUuid));
    }

    @GetMapping("/{uuid}")
    ResponseEntity<PedidoResponseDTO> obtenerPedido(@PathVariable UUID uuid) {
        return ResponseEntity.ok(pedidoService.obtenerPedidoPorUuid(uuid));
    }

    @PostMapping
    ResponseEntity<PedidoResponseDTO> crearPedido(@Valid @RequestBody PedidoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crearPedido(request));
    }
}