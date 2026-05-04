package com.fulfilltrack.FulfillTrack.features.liquidacion;

import com.fulfilltrack.FulfillTrack.features.liquidacion.dto.LiquidacionRequestDTO;
import com.fulfilltrack.FulfillTrack.features.liquidacion.dto.LiquidacionResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/liquidaciones")
@RequiredArgsConstructor
public class LiquidacionController {

    private final ILiquidacionService liquidacionService;

    @GetMapping
    ResponseEntity<List<LiquidacionResponseDTO>> listarLiquidaciones() {
        return ResponseEntity.ok(liquidacionService.listarLiquidaciones());
    }

    @GetMapping("/{uuid}")
    ResponseEntity<LiquidacionResponseDTO> obtenerLiquidacionPorUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(liquidacionService.obtenerLiquidacionPorUuid(uuid));
    }

    @GetMapping("/empresa/{empresaUuid}")
    ResponseEntity<List<LiquidacionResponseDTO>> listarPorEmpresa(@PathVariable UUID empresaUuid) {
        return ResponseEntity.ok(liquidacionService.listarLiquidacionesPorEmpresa(empresaUuid));
    }

    @PostMapping
    ResponseEntity<LiquidacionResponseDTO> crearLiquidacion(@Valid @RequestBody LiquidacionRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(liquidacionService.crearLiquidacion(request));
    }

    @PatchMapping("/{uuid}/marcar-pagada")
    ResponseEntity<Void> marcarComoPagada(@PathVariable UUID uuid) {
        liquidacionService.marcarComoPagada(uuid);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{uuid}/marcar-impaga")
    ResponseEntity<Void> marcarComoImpaga(@PathVariable UUID uuid) {
        liquidacionService.marcarComoImpaga(uuid);
        return ResponseEntity.noContent().build();
    }
}