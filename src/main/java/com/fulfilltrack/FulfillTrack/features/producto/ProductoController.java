package com.fulfilltrack.FulfillTrack.features.producto;

import com.fulfilltrack.FulfillTrack.features.producto.dto.ProductoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.producto.dto.ProductoResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {
    private final IProductoService productoService;

    @GetMapping
    ResponseEntity<List<ProductoResponseDTO>> listarProductos(@RequestParam(required = false) UUID empresaUuid){
        if(empresaUuid != null) return ResponseEntity.ok(productoService.listarProductosPorEmpresa(empresaUuid));
        return ResponseEntity.ok(productoService.listarProductos());
    }


    @GetMapping("/{productoUuid}")
    ResponseEntity<ProductoResponseDTO> obtenerProductoPorUuid(@PathVariable UUID productoUuid){
        return ResponseEntity.ok(productoService.obtenerProductoPorUuid(productoUuid));
    }

    @GetMapping("/sku/{productoSku}")
    ResponseEntity<ProductoResponseDTO> obtenerProductoPorSku(@PathVariable String productoSku){
        return ResponseEntity.ok(productoService.obtenerProductoPorSku(productoSku));
    }

    @PostMapping
    ResponseEntity<ProductoResponseDTO> crearProducto(@Valid @RequestBody ProductoRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crearProducto(request));
    }
    @PatchMapping("/{uuid}/activar")
    ResponseEntity<Void> activarProducto(@PathVariable UUID uuid){
        productoService.activarProducto(uuid);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{uuid}/desactivar")
    ResponseEntity<Void> desactivarProducto(@PathVariable UUID uuid){
        productoService.desactivarProducto(uuid);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{uuid}")
    ResponseEntity<ProductoResponseDTO> actualizarProducto(@PathVariable UUID uuid, @Valid @RequestBody ProductoRequestDTO request){
        return  ResponseEntity.ok(productoService.actualizarProducto(uuid,request));
    }

}
