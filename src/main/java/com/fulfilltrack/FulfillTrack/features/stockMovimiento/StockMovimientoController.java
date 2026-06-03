package com.fulfilltrack.FulfillTrack.features.stockMovimiento;

import com.fulfilltrack.FulfillTrack.features.stockMovimiento.dto.StockMovimientoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Movimientos de Stock", description = "Auditoría de movimientos de stock (reservas, ingresos, consumos, devoluciones)")
@RestController
@RequestMapping("/api/stock-movimientos")
@RequiredArgsConstructor
public class StockMovimientoController {

    private final IStockMovimientoService stockMovimientoService;

    @Operation(summary = "Listar movimientos de un producto", description = "Retorna el historial ordenado por fecha descendente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Historial de movimientos"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/producto/{productoUuid}")
    ResponseEntity<List<StockMovimientoResponseDTO>> listarMovimientosPorProducto(@PathVariable UUID productoUuid) {
        return ResponseEntity.ok(stockMovimientoService.listarMovimientosPorProducto(productoUuid));
    }

    @Operation(summary = "Listar movimientos por empresa", description = "Retorna el historial de todos los productos de la empresa, ordenado por fecha descendente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Historial de movimientos"),
        @ApiResponse(responseCode = "404", description = "Empresa no encontrada")
    })
    @GetMapping("/empresa/{empresaUuid}")
    ResponseEntity<List<StockMovimientoResponseDTO>> listarMovimientosPorEmpresa(@PathVariable UUID empresaUuid) {
        return ResponseEntity.ok(stockMovimientoService.listarMovimientosPorEmpresa(empresaUuid));
    }
}