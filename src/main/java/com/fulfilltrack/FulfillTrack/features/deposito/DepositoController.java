package com.fulfilltrack.FulfillTrack.features.deposito;

import com.fulfilltrack.FulfillTrack.features.deposito.dto.DepositoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.deposito.dto.DepositoResponseDTO;
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

import java.util.List;
import java.util.UUID;

@Tag(name = "Depósitos", description = "Gestión de depósitos logísticos")
@RestController
@RequestMapping("/api/depositos")
@RequiredArgsConstructor
public class DepositoController {
    private final IDepositoService depositoService;

    @PreAuthorize("hasAuthority('VER_DEPOSITOS')")
    @Operation(summary = "Listar todos los depósitos")
    @GetMapping
    ResponseEntity<List<DepositoResponseDTO>> listarDepositos(){
        return ResponseEntity.ok(depositoService.obtenerDepositos());
    }

    @PreAuthorize("hasAuthority('VER_DEPOSITOS')")
    @Operation(summary = "Obtener depósito por UUID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Depósito encontrado"),
        @ApiResponse(responseCode = "404", description = "Depósito no encontrado")
    })
    @GetMapping("/{uuid}")
    ResponseEntity<DepositoResponseDTO> obtenerDepositoPorUuid(@PathVariable UUID uuid){
        return ResponseEntity.ok(depositoService.obtenerDepositoPorUuid(uuid));
    }

    @PreAuthorize("hasAuthority('CREAR_DEPOSITO')")
    @Operation(summary = "Crear depósito")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Depósito creado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    ResponseEntity<DepositoResponseDTO> crearDeposito(@Valid @RequestBody DepositoRequestDTO request){
       DepositoResponseDTO respuesta = depositoService.crearDeposito(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PreAuthorize("hasAuthority('ACTUALIZAR_DEPOSITO')")
    @Operation(summary = "Actualizar depósito")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Depósito actualizado"),
        @ApiResponse(responseCode = "404", description = "Depósito no encontrado")
    })
    @PutMapping("/{uuid}")
    ResponseEntity<DepositoResponseDTO> actualizarDeposito(@PathVariable UUID uuid, @Valid @RequestBody DepositoRequestDTO request){
        return ResponseEntity.ok(depositoService.actualizarDeposito(uuid, request));
    }

    @PreAuthorize("hasAuthority('DESACTIVAR_DEPOSITO')")
    @Operation(summary = "Desactivar depósito")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Depósito desactivado"),
        @ApiResponse(responseCode = "404", description = "Depósito no encontrado"),
        @ApiResponse(responseCode = "400", description = "El depósito ya está inactivo")
    })
    @PatchMapping("/{uuid}/desactivar")
    ResponseEntity<Void> desactivarDeposito(@PathVariable UUID uuid){
        depositoService.desactivarDeposito(uuid);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('DESACTIVAR_DEPOSITO')")
    @Operation(summary = "Activar depósito")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Depósito activado"),
        @ApiResponse(responseCode = "404", description = "Depósito no encontrado"),
        @ApiResponse(responseCode = "400", description = "El depósito ya está activo")
    })
    @PatchMapping("/{uuid}/activar")
    ResponseEntity<Void> activarDeposito(@PathVariable UUID uuid){
        depositoService.activarDeposito(uuid);
        return ResponseEntity.noContent().build();
    }
}