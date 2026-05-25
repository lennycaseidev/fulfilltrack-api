package com.fulfilltrack.FulfillTrack.features.stock;

import com.fulfilltrack.FulfillTrack.features.stock.dto.StockResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {

    private final IStockService stockService;

    @GetMapping
    ResponseEntity<List<StockResponseDTO>> listarTodoElStock() {
        return ResponseEntity.ok(stockService.listarTodoElStock());
    }

    @GetMapping("/empresa/{empresaUuid}")
    ResponseEntity<List<StockResponseDTO>> listarStockPorEmpresa(@PathVariable UUID empresaUuid) {
        return ResponseEntity.ok(stockService.listarStockPorEmpresa(empresaUuid));
    }

    @GetMapping("/producto/{productoUuid}")
    ResponseEntity<StockResponseDTO> obtenerStockPorProducto(@PathVariable UUID productoUuid) {
        return ResponseEntity.ok(stockService.obtenerStockPorProductoUuid(productoUuid));
    }

    @PatchMapping("/producto/{productoUuid}/agregar")
    ResponseEntity<StockResponseDTO> agregarStock(@PathVariable UUID productoUuid, @RequestParam int cantidad) {
        return ResponseEntity.ok(stockService.agregarStock(productoUuid, cantidad));
    }
}