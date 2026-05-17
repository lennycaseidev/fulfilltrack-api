package com.fulfilltrack.FulfillTrack.features.stock;

import com.fulfilltrack.FulfillTrack.features.stock.dto.StockResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {

    private final IStockService stockService;

    @GetMapping("/producto/{productoUuid}")
    ResponseEntity<StockResponseDTO> obtenerStockPorProducto(@PathVariable UUID productoUuid) {
        return ResponseEntity.ok(stockService.obtenerStockPorProductoUuid(productoUuid));
    }
}