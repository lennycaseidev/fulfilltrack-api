package com.fulfilltrack.FulfillTrack.features.puesto;

import com.fulfilltrack.FulfillTrack.features.puesto.dto.PuestoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.puesto.dto.PuestoResponseDTO;
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

@Tag(name = "Puestos", description = "Gestión de puestos de trabajo del depósito")
@RestController
@RequestMapping("/api/puestos")
@AllArgsConstructor
public class PuestoController {
    private final IPuestoService puestoService;

    @Operation(summary = "Listar todos los puestos")
    @GetMapping
    public ResponseEntity<List<PuestoResponseDTO>> listarPuestos(){
        return ResponseEntity.ok(puestoService.listarPuestos());
    }

    @Operation(summary = "Obtener puesto por UUID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Puesto encontrado"),
        @ApiResponse(responseCode = "404", description = "Puesto no encontrado")
    })
    @GetMapping("/{uuid}")
    public ResponseEntity<PuestoResponseDTO> obtenerPuestoPorUuid(@PathVariable UUID uuid){
        return ResponseEntity.ok(puestoService.obtenerPuestoPorUuid(uuid));
    }

    @Operation(summary = "Crear puesto")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Puesto creado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<PuestoResponseDTO> crearPuesto(@Valid @RequestBody PuestoRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(puestoService.crearPuesto(request));
    }

    @Operation(summary = "Actualizar puesto")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Puesto actualizado"),
        @ApiResponse(responseCode = "404", description = "Puesto no encontrado")
    })
    @PutMapping("/{uuid}")
    public ResponseEntity<PuestoResponseDTO> actualizarPuesto(@PathVariable UUID uuid, @Valid @RequestBody PuestoRequestDTO request){
        return ResponseEntity.ok(puestoService.actualizarPuesto(uuid, request));
    }

    @Operation(summary = "Eliminar puesto")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Puesto eliminado"),
        @ApiResponse(responseCode = "404", description = "Puesto no encontrado")
    })
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> eliminarPuesto(@PathVariable UUID uuid){
        puestoService.eliminarPuesto(uuid);
        return ResponseEntity.noContent().build();
    }
}