package com.fulfilltrack.FulfillTrack.features.pedido;

import com.fulfilltrack.FulfillTrack.features.pedido.dto.PedidoImportResultDTO;
import com.fulfilltrack.FulfillTrack.features.pedido.dto.PedidoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.pedido.dto.PedidoResponseDTO;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "Pedidos", description = "Gestión de pedidos y su ciclo de vida")
@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final IPedidoService pedidoService;
    private final IPedidoCsvService pedidoCsvService;

    @PreAuthorize("hasAuthority('VER_PEDIDOS')")
    @Operation(summary = "Listar pedidos activos", description = "Devuelve todos los pedidos que no están en estado DESPACHADO ni DEVUELTO")
    @GetMapping
    ResponseEntity<List<PedidoResponseDTO>> listarPedidosActivos() {
        return ResponseEntity.ok(pedidoService.listarPedidosActivos());
    }

    @PreAuthorize("hasAuthority('VER_PEDIDOS')")
    @Operation(summary = "Historial de pedidos", description = "Devuelve todos los pedidos sin importar el estado, incluyendo despachados y devueltos")
    @GetMapping("/historial")
    ResponseEntity<List<PedidoResponseDTO>> listarHistorial() {
        return ResponseEntity.ok(pedidoService.listarHistorial());
    }

    @PreAuthorize("hasAuthority('VER_PEDIDOS')")
    @Operation(summary = "Listar pedidos por empresa")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Listado de pedidos"),
        @ApiResponse(responseCode = "404", description = "Empresa no encontrada")
    })
    @GetMapping("/empresa/{empresaUuid}")
    ResponseEntity<List<PedidoResponseDTO>> listarPedidosPorEmpresa(@PathVariable UUID empresaUuid) {
        return ResponseEntity.ok(pedidoService.listarPedidosPorEmpresa(empresaUuid));
    }

    @PreAuthorize("hasAuthority('VER_PEDIDOS')")
    @Operation(summary = "Obtener pedido por UUID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @GetMapping("/{uuid}")
    ResponseEntity<PedidoResponseDTO> obtenerPedido(@PathVariable UUID uuid) {
        return ResponseEntity.ok(pedidoService.obtenerPedidoPorUuid(uuid));
    }

    @PreAuthorize("hasAuthority('CREAR_PEDIDO')")
    @Operation(summary = "Crear pedido", description = "Crea el pedido en estado RECIBIDO y reserva el stock de cada ítem")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pedido creado"),
        @ApiResponse(responseCode = "400", description = "Stock insuficiente para algún ítem"),
        @ApiResponse(responseCode = "404", description = "Empresa o producto no encontrado")
    })
    @PostMapping
    ResponseEntity<PedidoResponseDTO> crearPedido(@Valid @RequestBody PedidoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crearPedido(request));
    }

    @PreAuthorize("hasAuthority('ACTUALIZAR_ESTADO_PEDIDO')")
    @Operation(summary = "Confirmar pedido", description = "Transición RECIBIDO → CONFIRMADO. Consume el stock reservado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pedido confirmado"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
        @ApiResponse(responseCode = "400", description = "Transición de estado no permitida")
    })
    @PatchMapping("/{uuid}/confirmar")
    ResponseEntity<PedidoResponseDTO> confirmarPedido(@PathVariable UUID uuid) {
        return ResponseEntity.ok(pedidoService.confirmarPedido(uuid));
    }

    @PreAuthorize("hasAuthority('ACTUALIZAR_ESTADO_PEDIDO')")
    @Operation(summary = "Iniciar preparación", description = "Transición CONFIRMADO → EN_PREPARACION")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Preparación iniciada"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
        @ApiResponse(responseCode = "400", description = "Transición de estado no permitida")
    })
    @PatchMapping("/{uuid}/iniciar-preparacion")
    ResponseEntity<PedidoResponseDTO> iniciarPreparacion(@PathVariable UUID uuid) {
        return ResponseEntity.ok(pedidoService.iniciarPreparacion(uuid));
    }

    @PreAuthorize("hasAuthority('ACTUALIZAR_ESTADO_PEDIDO')")
    @Operation(summary = "Marcar listo para despacho", description = "Transición EN_PREPARACION → LISTO_PARA_DESPACHO")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pedido listo para despacho"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
        @ApiResponse(responseCode = "400", description = "Transición de estado no permitida")
    })
    @PatchMapping("/{uuid}/listo-para-despacho")
    ResponseEntity<PedidoResponseDTO> marcarListoParaDespacho(@PathVariable UUID uuid) {
        return ResponseEntity.ok(pedidoService.marcarListoParaDespacho(uuid));
    }

    @PreAuthorize("hasAuthority('CREAR_PEDIDO')")
    @Operation(summary = "Importar pedidos desde CSV", description = "El archivo debe tener cabecera: numeroOrden,direccionEntrega,nombreDestinatario,empresaUuid,sku,cantidad. Una fila por ítem; filas con el mismo numeroOrden se agrupan en un solo pedido")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Importación procesada. Revisar errores en el response"),
        @ApiResponse(responseCode = "400", description = "Archivo inválido o mal formado")
    })
    @PostMapping(value = "/importar", consumes = "multipart/form-data")
    ResponseEntity<PedidoImportResultDTO> importarDesdeCsv(@RequestParam("archivo") MultipartFile archivo) {
        return ResponseEntity.ok(pedidoCsvService.importarDesdeCsv(archivo));
    }

    @PreAuthorize("hasAuthority('ACTUALIZAR_ESTADO_PEDIDO')")
    @Operation(summary = "Devolver pedido", description = "Desde RECIBIDO libera la reserva de stock; desde cualquier otro estado devuelve el stock como DEVOLUCION")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pedido devuelto"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
        @ApiResponse(responseCode = "400", description = "Transición de estado no permitida")
    })
    @PatchMapping("/{uuid}/devolver")
    ResponseEntity<PedidoResponseDTO> devolverPedido(@PathVariable UUID uuid) {
        return ResponseEntity.ok(pedidoService.devolverPedido(uuid));
    }
}