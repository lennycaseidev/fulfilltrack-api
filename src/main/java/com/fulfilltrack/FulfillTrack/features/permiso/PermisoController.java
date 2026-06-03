package com.fulfilltrack.FulfillTrack.features.permiso;

import com.fulfilltrack.FulfillTrack.features.permiso.dto.PermisoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.permiso.dto.PermisoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Permisos", description = "Gestión de permisos del sistema")
@RestController
@RequestMapping("/api/permisos")
@AllArgsConstructor
public class PermisoController {
    private final IPermisoService permisoService;

    @Operation(summary = "Listar todos los permisos")
    @GetMapping
    public ResponseEntity<List<PermisoResponseDTO>> listarPermisos(){
        return ResponseEntity.ok(permisoService.listarPermisos());
    }

    @Operation(summary = "Crear permiso")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Permiso creado"),
        @ApiResponse(responseCode = "409", description = "Permiso ya existente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<PermisoResponseDTO> crearPermiso(@Valid @RequestBody PermisoRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(permisoService.crearPermiso(request));
    }

    @Operation(summary = "Actualizar permiso")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Permiso actualizado"),
        @ApiResponse(responseCode = "404", description = "Permiso no encontrado")
    })
    @PutMapping("/{uuid}")
    public ResponseEntity<PermisoResponseDTO> actualizarPermiso(@PathVariable UUID uuid, @Valid @RequestBody PermisoRequestDTO request){
        return ResponseEntity.ok(permisoService.actualizarPermiso(uuid, request));
    }
}