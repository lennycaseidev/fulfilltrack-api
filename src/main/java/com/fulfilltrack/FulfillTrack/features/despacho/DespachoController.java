package com.fulfilltrack.FulfillTrack.features.despacho;

import com.fulfilltrack.FulfillTrack.features.despacho.dto.DespachoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.despacho.dto.DespachoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Despachos", description = "Gestión de despachos de pedidos")
@RestController
@RequestMapping("/api/despachos")
@RequiredArgsConstructor
public class DespachoController {

    private final IDespachoService despachoService;

    @PreAuthorize("hasAuthority('VER_DESPACHOS')")
    @Operation(summary = "Listar todos los despachos")
    @GetMapping
    ResponseEntity<List<DespachoResponseDTO>> listarDespachos() {
        return ResponseEntity.ok(despachoService.listarDespachos());
    }

    @PreAuthorize("hasAuthority('VER_DESPACHOS')")
    @Operation(summary = "Obtener despacho por pedido")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Despacho encontrado"),
        @ApiResponse(responseCode = "404", description = "Pedido sin despacho asociado")
    })
    @GetMapping("/pedido/{pedidoUuid}")
    ResponseEntity<DespachoResponseDTO> obtenerDespachoPorPedido(@PathVariable UUID pedidoUuid) {
        return ResponseEntity.ok(despachoService.obtenerDespachoPorPedido(pedidoUuid));
    }

    @PreAuthorize("hasAuthority('CREAR_DESPACHO')")
    @Operation(summary = "Crear despacho", description = "Registra el despacho de un pedido y lo avanza al estado DESPACHADO")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Despacho creado"),
        @ApiResponse(responseCode = "400", description = "El pedido no está en estado LISTO_PARA_DESPACHO"),
        @ApiResponse(responseCode = "409", description = "El pedido ya tiene un despacho registrado"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @PostMapping
    ResponseEntity<DespachoResponseDTO> crearDespacho(@Valid @RequestBody DespachoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(despachoService.crearDespacho(request));
    }
}