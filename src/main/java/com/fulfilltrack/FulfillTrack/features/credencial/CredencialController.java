package com.fulfilltrack.FulfillTrack.features.credencial;

import com.fulfilltrack.FulfillTrack.features.credencial.dto.CredencialResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Credenciales", description = "Gestión de credenciales de acceso")
@RestController
@RequestMapping("/api/credenciales")
@RequiredArgsConstructor
public class CredencialController {
    private final ICredencialService credencialService;

    @Operation(summary = "Listar credenciales", description = "Si se provee email, retorna solo la credencial correspondiente")
    @GetMapping
    ResponseEntity<List<CredencialResponseDTO>> obtenerCredenciales(@RequestParam(required = false) String email){
        if(email != null) return ResponseEntity.ok(List.of(credencialService.obtenerCredencialPorEmail(email)));
        return ResponseEntity.ok(credencialService.obtenerCredenciales());
    }

    @Operation(summary = "Obtener credencial por UUID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Credencial encontrada"),
        @ApiResponse(responseCode = "404", description = "Credencial no encontrada")
    })
    @GetMapping("/{uuid}")
    ResponseEntity<CredencialResponseDTO> obtenerCredencialPorUuid(@PathVariable UUID uuid){
        return ResponseEntity.ok(credencialService.obtenerCredencialPorUuid(uuid));
    }

    @Operation(summary = "Asignar permiso a una credencial")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Permiso asignado correctamente"),
        @ApiResponse(responseCode = "404", description = "Credencial o permiso no encontrado")
    })
    @PatchMapping("/{uuid}/asignar-permiso/{permisoUuid}")
    ResponseEntity<CredencialResponseDTO> asignarPermiso(@PathVariable UUID uuid, @PathVariable UUID permisoUuid){
        return ResponseEntity.ok(credencialService.asignarPermiso(uuid, permisoUuid));
    }

    @Operation(summary = "Activar credencial")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Credencial activada"),
        @ApiResponse(responseCode = "404", description = "Credencial no encontrada"),
        @ApiResponse(responseCode = "400", description = "La credencial ya está activa")
    })
    @PatchMapping("/{uuid}/activar")
    ResponseEntity<Void> activarCredencial(@PathVariable UUID uuid){
        credencialService.activarCredencial(uuid);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Desactivar credencial")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Credencial desactivada"),
        @ApiResponse(responseCode = "404", description = "Credencial no encontrada"),
        @ApiResponse(responseCode = "400", description = "La credencial ya está inactiva")
    })
    @PatchMapping("/{uuid}/desactivar")
    ResponseEntity<Void> desactivarCredencial(@PathVariable UUID uuid){
        credencialService.desactivarCredencial(uuid);
        return ResponseEntity.noContent().build();
    }
}