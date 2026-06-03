package com.fulfilltrack.FulfillTrack.features.liquidacion;

import com.fulfilltrack.FulfillTrack.features.liquidacion.dto.LiquidacionRequestDTO;
import com.fulfilltrack.FulfillTrack.features.liquidacion.dto.LiquidacionResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Liquidaciones", description = "Gestión de liquidaciones periódicas por empresa")
@RestController
@RequestMapping("/api/liquidaciones")
@RequiredArgsConstructor
public class LiquidacionController {
    private final ILiquidacionService liquidacionService;

    @Operation(summary = "Listar todas las liquidaciones")
    @GetMapping
    ResponseEntity<List<LiquidacionResponseDTO>> listarLiquidaciones() {
        return ResponseEntity.ok(liquidacionService.listarLiquidaciones());
    }

    @Operation(summary = "Obtener liquidación por UUID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liquidación encontrada"),
        @ApiResponse(responseCode = "404", description = "Liquidación no encontrada")
    })
    @GetMapping("/{uuid}")
    ResponseEntity<LiquidacionResponseDTO> obtenerLiquidacionPorUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(liquidacionService.obtenerLiquidacionPorUuid(uuid));
    }

    @Operation(summary = "Listar liquidaciones por empresa")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liquidaciones de la empresa"),
        @ApiResponse(responseCode = "404", description = "Empresa no encontrada")
    })
    @GetMapping("/empresa/{empresaUuid}")
    ResponseEntity<List<LiquidacionResponseDTO>> listarPorEmpresa(@PathVariable UUID empresaUuid) {
        return ResponseEntity.ok(liquidacionService.listarLiquidacionesPorEmpresa(empresaUuid));
    }

    @Operation(summary = "Crear liquidación manualmente", description = "Las liquidaciones también se generan automáticamente cada mes vía scheduler")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Liquidación creada"),
        @ApiResponse(responseCode = "404", description = "Empresa no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    ResponseEntity<LiquidacionResponseDTO> crearLiquidacion(@Valid @RequestBody LiquidacionRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(liquidacionService.crearLiquidacion(request));
    }

    @Operation(summary = "Actualizar liquidación")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liquidación actualizada"),
        @ApiResponse(responseCode = "404", description = "Liquidación no encontrada")
    })
    @PutMapping("/{uuid}")
    ResponseEntity<LiquidacionResponseDTO> actualizarLiquidacion(@PathVariable UUID uuid, @Valid @RequestBody LiquidacionRequestDTO request) {
        return ResponseEntity.ok(liquidacionService.actualizarLiquidacion(uuid, request));
    }

    @Operation(summary = "Marcar liquidación como pagada")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Liquidación marcada como pagada"),
        @ApiResponse(responseCode = "404", description = "Liquidación no encontrada"),
        @ApiResponse(responseCode = "400", description = "La liquidación ya está pagada")
    })
    @PatchMapping("/{uuid}/marcar-pagada")
    ResponseEntity<Void> marcarComoPagada(@PathVariable UUID uuid) {
        liquidacionService.marcarComoPagada(uuid);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Marcar liquidación como impaga")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Liquidación marcada como impaga"),
        @ApiResponse(responseCode = "404", description = "Liquidación no encontrada"),
        @ApiResponse(responseCode = "400", description = "La liquidación ya está impaga")
    })
    @PatchMapping("/{uuid}/marcar-impaga")
    ResponseEntity<Void> marcarComoImpaga(@PathVariable UUID uuid) {
        liquidacionService.marcarComoImpaga(uuid);
        return ResponseEntity.noContent().build();
    }
}