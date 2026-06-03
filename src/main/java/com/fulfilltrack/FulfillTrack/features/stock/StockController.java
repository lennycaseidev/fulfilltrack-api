package com.fulfilltrack.FulfillTrack.features.stock;

import com.fulfilltrack.FulfillTrack.features.stock.dto.StockResponseDTO;
import com.fulfilltrack.FulfillTrack.features.stockMovimiento.TipoMovimientoStock;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Stock", description = "Gestión de stock de productos")
@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {

    private final IStockService stockService;

    @Operation(summary = "Listar todo el stock del sistema")
    @GetMapping
    ResponseEntity<List<StockResponseDTO>> listarTodoElStock() {
        return ResponseEntity.ok(stockService.listarTodoElStock());
    }

    @Operation(summary = "Listar stock por empresa")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock listado"),
        @ApiResponse(responseCode = "404", description = "Empresa no encontrada")
    })
    @GetMapping("/empresa/{empresaUuid}")
    ResponseEntity<List<StockResponseDTO>> listarStockPorEmpresa(@PathVariable UUID empresaUuid) {
        return ResponseEntity.ok(stockService.listarStockPorEmpresa(empresaUuid));
    }

    @Operation(summary = "Obtener stock de un producto")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/producto/{productoUuid}")
    ResponseEntity<StockResponseDTO> obtenerStockPorProducto(@PathVariable UUID productoUuid) {
        return ResponseEntity.ok(stockService.obtenerStockPorProductoUuid(productoUuid));
    }

    @Operation(summary = "Agregar stock manualmente", description = "Incrementa la cantidad disponible y registra un movimiento de tipo INGRESO")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock actualizado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PatchMapping("/producto/{productoUuid}/agregar")
    ResponseEntity<StockResponseDTO> agregarStock(@PathVariable UUID productoUuid, @RequestParam int cantidad) {
        return ResponseEntity.ok(stockService.agregarStock(productoUuid, cantidad, TipoMovimientoStock.INGRESO, "Ingreso manual de stock"));
    }
}