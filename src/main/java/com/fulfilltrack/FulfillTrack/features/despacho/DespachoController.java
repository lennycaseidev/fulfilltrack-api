package com.fulfilltrack.FulfillTrack.features.despacho;

import com.fulfilltrack.FulfillTrack.features.despacho.dto.DespachoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.despacho.dto.DespachoResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/despachos")
@RequiredArgsConstructor
public class DespachoController {

    private final IDespachoService despachoService;

    @GetMapping
    ResponseEntity<List<DespachoResponseDTO>> listarDespachos() {
        return ResponseEntity.ok(despachoService.listarDespachos());
    }

    @GetMapping("/{uuid}")
    ResponseEntity<DespachoResponseDTO> obtenerDespacho(@PathVariable UUID uuid) {
        return ResponseEntity.ok(despachoService.obtenerDespachoPorUuid(uuid));
    }

    @GetMapping("/pedido/{pedidoUuid}")
    ResponseEntity<DespachoResponseDTO> obtenerDespachoPorPedido(@PathVariable UUID pedidoUuid) {
        return ResponseEntity.ok(despachoService.obtenerDespachoPorPedido(pedidoUuid));
    }

    @PostMapping
    ResponseEntity<DespachoResponseDTO> crearDespacho(@Valid @RequestBody DespachoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(despachoService.crearDespacho(request));
    }
}