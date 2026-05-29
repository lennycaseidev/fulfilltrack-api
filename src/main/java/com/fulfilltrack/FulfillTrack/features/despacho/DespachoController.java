package com.fulfilltrack.FulfillTrack.features.despacho;

import com.fulfilltrack.FulfillTrack.features.despacho.dto.DespachoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.despacho.dto.DespachoResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    ResponseEntity<List<DespachoResponseDTO>> listarDespachos() { return null; }

    @GetMapping("/{uuid}")
    ResponseEntity<DespachoResponseDTO> obtenerDespacho(@PathVariable UUID uuid) { return null; }

    @GetMapping("/pedido/{pedidoUuid}")
    ResponseEntity<DespachoResponseDTO> obtenerDespachoPorPedido(@PathVariable UUID pedidoUuid) { return null; }

    @PostMapping
    ResponseEntity<DespachoResponseDTO> crearDespacho(@Valid @RequestBody DespachoRequestDTO request) { return null; }

    @PatchMapping("/{uuid}/entregar")
    ResponseEntity<DespachoResponseDTO> marcarEntregado(@PathVariable UUID uuid) { return null; }
}