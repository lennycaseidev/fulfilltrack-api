package com.fulfilltrack.FulfillTrack.features.infoEmpleados;

import com.fulfilltrack.FulfillTrack.features.infoEmpleados.dto.InfoEmpleadosRequestDTO;
import com.fulfilltrack.FulfillTrack.features.infoEmpleados.dto.InfoEmpleadosResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Empleados", description = "Gestión de información de empleados del depósito")
@RestController
@RequestMapping("/api/empleados")
@AllArgsConstructor
public class InfoEmpleadosController {
    private final IInfoEmpleadosService infoEmpleadosService;

    @PreAuthorize("hasAuthority('VER_USUARIOS')")
    @Operation(summary = "Listar todos los empleados")
    @GetMapping
    public ResponseEntity<List<InfoEmpleadosResponseDTO>> listarEmpleados(){
        return ResponseEntity.ok(infoEmpleadosService.listarEmpleados());
    }

    @PreAuthorize("hasAuthority('VER_USUARIOS')")
    @Operation(summary = "Obtener empleado por UUID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Empleado encontrado"),
        @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
    })
    @GetMapping("/{uuid}")
    public ResponseEntity<InfoEmpleadosResponseDTO> obtenerEmpleadoPorUuid(@PathVariable UUID uuid){
        return ResponseEntity.ok(infoEmpleadosService.obtenerEmpleadoPorUuid(uuid));
    }

    @PreAuthorize("hasAuthority('CREAR_USUARIO')")
    @Operation(summary = "Registrar empleado", description = "El usuario no puede estar vinculado como contacto de empresa")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Empleado registrado"),
        @ApiResponse(responseCode = "409", description = "Documento ya registrado"),
        @ApiResponse(responseCode = "400", description = "El usuario ya está vinculado como contacto de empresa"),
        @ApiResponse(responseCode = "404", description = "Usuario, depósito o puesto no encontrado")
    })
    @PostMapping
    public ResponseEntity<InfoEmpleadosResponseDTO> crearEmpleado(@Valid @RequestBody InfoEmpleadosRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(infoEmpleadosService.crearEmpleado(request));
    }

    @PreAuthorize("hasAuthority('ACTUALIZAR_USUARIO')")
    @Operation(summary = "Actualizar empleado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Empleado actualizado"),
        @ApiResponse(responseCode = "404", description = "Empleado, depósito o puesto no encontrado")
    })
    @PutMapping("/{uuid}")
    public ResponseEntity<InfoEmpleadosResponseDTO> actualizarEmpleado(@PathVariable UUID uuid,
                                                                       @Valid @RequestBody InfoEmpleadosRequestDTO request){
        return ResponseEntity.ok(infoEmpleadosService.actualizarEmpleado(uuid, request));
    }

    @PreAuthorize("hasAuthority('DESACTIVAR_USUARIO')")
    @Operation(summary = "Activar empleado")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Empleado activado"),
        @ApiResponse(responseCode = "404", description = "Empleado no encontrado"),
        @ApiResponse(responseCode = "400", description = "El empleado ya está activo")
    })
    @PatchMapping("/{uuid}/activar")
    public ResponseEntity<Void> activarEmpleado(@PathVariable UUID uuid){
        infoEmpleadosService.activarEmpleado(uuid);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('DESACTIVAR_USUARIO')")
    @Operation(summary = "Desactivar empleado")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Empleado desactivado"),
        @ApiResponse(responseCode = "404", description = "Empleado no encontrado"),
        @ApiResponse(responseCode = "400", description = "El empleado ya está inactivo")
    })
    @PatchMapping("/{uuid}/desactivar")
    public ResponseEntity<Void> desactivarEmpleado(@PathVariable UUID uuid){
        infoEmpleadosService.desactivarEmpleado(uuid);
        return ResponseEntity.noContent().build();
    }
}