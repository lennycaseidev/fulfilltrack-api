package com.fulfilltrack.FulfillTrack.features.stockMovimiento;

import com.fulfilltrack.FulfillTrack.features.stockMovimiento.dto.StockMovimientoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/stock-movimientos")
@RequiredArgsConstructor
public class StockMovimientoController {

    private final IStockMovimientoService stockMovimientoService;

    @GetMapping("/producto/{productoUuid}")
    ResponseEntity<List<StockMovimientoResponseDTO>> listarMovimientosPorProducto(@PathVariable UUID productoUuid) {
        return ResponseEntity.ok(stockMovimientoService.listarMovimientosPorProducto(productoUuid));
    }

    @GetMapping("/empresa/{empresaUuid}")
    ResponseEntity<List<StockMovimientoResponseDTO>> listarMovimientosPorEmpresa(@PathVariable UUID empresaUuid) {
        return ResponseEntity.ok(stockMovimientoService.listarMovimientosPorEmpresa(empresaUuid));
    }
}