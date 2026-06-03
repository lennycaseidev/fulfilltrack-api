package com.fulfilltrack.FulfillTrack.features.producto;

import com.fulfilltrack.FulfillTrack.features.producto.dto.ProductoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.producto.dto.ProductoResponseDTO;
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

@Tag(name = "Productos", description = "Gestión de productos por empresa")
@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {
    private final IProductoService productoService;

    @Operation(summary = "Listar productos", description = "Si se provee empresaUuid, filtra por empresa")
    @GetMapping
    ResponseEntity<List<ProductoResponseDTO>> listarProductos(@RequestParam(required = false) UUID empresaUuid){
        if(empresaUuid != null) return ResponseEntity.ok(productoService.listarProductosPorEmpresa(empresaUuid));
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @Operation(summary = "Obtener producto por UUID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{productoUuid}")
    ResponseEntity<ProductoResponseDTO> obtenerProductoPorUuid(@PathVariable UUID productoUuid){
        return ResponseEntity.ok(productoService.obtenerProductoPorUuid(productoUuid));
    }

    @Operation(summary = "Obtener producto por SKU")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/sku/{productoSku}")
    ResponseEntity<ProductoResponseDTO> obtenerProductoPorSku(@PathVariable String productoSku){
        return ResponseEntity.ok(productoService.obtenerProductoPorSku(productoSku));
    }

    @Operation(summary = "Crear producto", description = "El SKU debe ser único por empresa. Crea el registro de stock automáticamente con cantidades en 0")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Producto creado"),
        @ApiResponse(responseCode = "409", description = "SKU ya registrado para esta empresa"),
        @ApiResponse(responseCode = "404", description = "Empresa no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    ResponseEntity<ProductoResponseDTO> crearProducto(@Valid @RequestBody ProductoRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crearProducto(request));
    }

    @Operation(summary = "Actualizar producto")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto actualizado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{uuid}")
    ResponseEntity<ProductoResponseDTO> actualizarProducto(@PathVariable UUID uuid, @Valid @RequestBody ProductoRequestDTO request){
        return ResponseEntity.ok(productoService.actualizarProducto(uuid, request));
    }

    @Operation(summary = "Activar producto")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Producto activado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "400", description = "El producto ya está activo")
    })
    @PatchMapping("/{uuid}/activar")
    ResponseEntity<Void> activarProducto(@PathVariable UUID uuid){
        productoService.activarProducto(uuid);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Desactivar producto", description = "No se puede desactivar si tiene stock disponible o reservado")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Producto desactivado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "400", description = "El producto tiene stock activo o ya está inactivo")
    })
    @PatchMapping("/{uuid}/desactivar")
    ResponseEntity<Void> desactivarProducto(@PathVariable UUID uuid){
        productoService.desactivarProducto(uuid);
        return ResponseEntity.noContent().build();
    }
}