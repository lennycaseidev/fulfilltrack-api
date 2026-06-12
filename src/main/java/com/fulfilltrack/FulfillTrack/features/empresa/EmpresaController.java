package com.fulfilltrack.FulfillTrack.features.empresa;

import com.fulfilltrack.FulfillTrack.features.empresa.dto.EmpresaRequestDTO;
import com.fulfilltrack.FulfillTrack.features.empresa.dto.EmpresaResponseDTO;
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

@Tag(name = "Empresas", description = "Gestión de empresas cliente")
@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor
public class EmpresaController {
    private final IEmpresaService empresaService;

    @PreAuthorize("hasAuthority('VER_EMPRESAS')")
    @Operation(summary = "Listar todas las empresas")
    @GetMapping
    public ResponseEntity<List<EmpresaResponseDTO>> listarEmpresas(){
        return ResponseEntity.ok(empresaService.listarEmpresas());
    }

    @PreAuthorize("hasAuthority('VER_EMPRESAS')")
    @Operation(summary = "Obtener empresa por UUID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Empresa encontrada"),
        @ApiResponse(responseCode = "404", description = "Empresa no encontrada")
    })
    @GetMapping("/{uuid}")
    public ResponseEntity<EmpresaResponseDTO> obtenerEmpresaPorUuid(@PathVariable UUID uuid){
        return ResponseEntity.ok(empresaService.obtenerEmpresaPorUuid(uuid));
    }

    @PreAuthorize("hasAuthority('CREAR_EMPRESA')")
    @Operation(summary = "Crear empresa")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Empresa creada"),
        @ApiResponse(responseCode = "409", description = "Email ya registrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<EmpresaResponseDTO> crearEmpresa(@RequestBody @Valid EmpresaRequestDTO nuevaEmpresa){
        EmpresaResponseDTO respuesta = empresaService.crearEmpresa(nuevaEmpresa);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PreAuthorize("hasAuthority('ACTUALIZAR_EMPRESA')")
    @Operation(summary = "Actualizar empresa")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Empresa actualizada"),
        @ApiResponse(responseCode = "404", description = "Empresa no encontrada"),
        @ApiResponse(responseCode = "409", description = "Email ya registrado por otra empresa")
    })
    @PutMapping("/{uuid}")
    public ResponseEntity<EmpresaResponseDTO> actualizarEmpresa(@PathVariable UUID uuid,
            @RequestBody @Valid EmpresaRequestDTO modificar){
        return ResponseEntity.ok(empresaService.actualizarEmpresa(uuid, modificar));
    }

    @PreAuthorize("hasAuthority('DESACTIVAR_EMPRESA')")
    @Operation(summary = "Desactivar empresa")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Empresa desactivada"),
        @ApiResponse(responseCode = "404", description = "Empresa no encontrada"),
        @ApiResponse(responseCode = "400", description = "La empresa ya está inactiva")
    })
    @PatchMapping("/{uuid}/desactivar")
    public ResponseEntity<Void> desactivarEmpresa(@PathVariable UUID uuid) {
        empresaService.desactivarEmpresa(uuid);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('DESACTIVAR_EMPRESA')")
    @Operation(summary = "Activar empresa")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Empresa activada"),
        @ApiResponse(responseCode = "404", description = "Empresa no encontrada"),
        @ApiResponse(responseCode = "400", description = "La empresa ya está activa")
    })
    @PatchMapping("/{uuid}/activar")
    public ResponseEntity<Void> activarEmpresa(@PathVariable UUID uuid){
        empresaService.activarEmpresa(uuid);
        return ResponseEntity.noContent().build();
    }
}