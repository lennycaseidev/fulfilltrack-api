package com.fulfilltrack.FulfillTrack.features.usuarioEmpresa;

import com.fulfilltrack.FulfillTrack.features.usuarioEmpresa.dto.UsuarioEmpresaRequestDTO;
import com.fulfilltrack.FulfillTrack.features.usuarioEmpresa.dto.UsuarioEmpresaResponseDTO;
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

@Tag(name = "Usuarios Empresa", description = "Gestión de contactos vinculados a empresas cliente")
@RestController
@RequestMapping("/api/usuarios-empresa")
@AllArgsConstructor
public class UsuarioEmpresaController {
    private final IUsuarioEmpresaService usuarioEmpresaService;

    @PreAuthorize("hasAuthority('VER_USUARIOS')")
    @Operation(summary = "Listar todos los contactos de empresa")
    @GetMapping
    public ResponseEntity<List<UsuarioEmpresaResponseDTO>> listarUsuariosEmpresa(){
        return ResponseEntity.ok(usuarioEmpresaService.listarUsuariosEmpresa());
    }

    @PreAuthorize("hasAuthority('VER_USUARIOS')")
    @Operation(summary = "Obtener contacto por UUID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Contacto encontrado"),
        @ApiResponse(responseCode = "404", description = "Contacto no encontrado")
    })
    @GetMapping("/{uuid}")
    public ResponseEntity<UsuarioEmpresaResponseDTO> obtenerUsuarioEmpresaPorUuid(@PathVariable UUID uuid){
        return ResponseEntity.ok(usuarioEmpresaService.obtenerUsuarioEmpresaPorUuid(uuid));
    }

    @PreAuthorize("hasAuthority('VER_USUARIOS')")
    @Operation(summary = "Listar contactos por empresa")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Listado de contactos"),
        @ApiResponse(responseCode = "404", description = "Empresa no encontrada")
    })
    @GetMapping("/empresa/{empresaUuid}")
    public ResponseEntity<List<UsuarioEmpresaResponseDTO>> listarPorEmpresa(@PathVariable UUID empresaUuid){
        return ResponseEntity.ok(usuarioEmpresaService.listarUsuariosPorEmpresa(empresaUuid));
    }

    @PreAuthorize("hasAuthority('CREAR_USUARIO')")
    @Operation(summary = "Vincular usuario a empresa", description = "El usuario no puede estar registrado como empleado del depósito")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Vínculo creado"),
        @ApiResponse(responseCode = "409", description = "El usuario ya está vinculado a una empresa"),
        @ApiResponse(responseCode = "400", description = "El usuario ya está registrado como empleado"),
        @ApiResponse(responseCode = "404", description = "Usuario o empresa no encontrado")
    })
    @PostMapping
    public ResponseEntity<UsuarioEmpresaResponseDTO> crearUsuarioEmpresa(@Valid @RequestBody UsuarioEmpresaRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioEmpresaService.crearUsuarioEmpresa(request));
    }

    @PreAuthorize("hasAuthority('ACTUALIZAR_USUARIO')")
    @Operation(summary = "Actualizar datos del contacto")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Contacto actualizado"),
        @ApiResponse(responseCode = "404", description = "Contacto o empresa no encontrado")
    })
    @PutMapping("/{uuid}")
    public ResponseEntity<UsuarioEmpresaResponseDTO> actualizarUsuarioEmpresa(@PathVariable UUID uuid,
                                                                              @Valid @RequestBody UsuarioEmpresaRequestDTO request){
        return ResponseEntity.ok(usuarioEmpresaService.actualizarUsuarioEmpresa(uuid, request));
    }

    @PreAuthorize("hasAuthority('DESACTIVAR_USUARIO')")
    @Operation(summary = "Activar contacto de empresa")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Contacto activado"),
        @ApiResponse(responseCode = "404", description = "Contacto no encontrado"),
        @ApiResponse(responseCode = "400", description = "El contacto ya está activo")
    })
    @PatchMapping("/{uuid}/activar")
    public ResponseEntity<Void> activarUsuarioEmpresa(@PathVariable UUID uuid){
        usuarioEmpresaService.activarUsuarioEmpresa(uuid);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('DESACTIVAR_USUARIO')")
    @Operation(summary = "Desactivar contacto de empresa")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Contacto desactivado"),
        @ApiResponse(responseCode = "404", description = "Contacto no encontrado"),
        @ApiResponse(responseCode = "400", description = "El contacto ya está inactivo")
    })
    @PatchMapping("/{uuid}/desactivar")
    public ResponseEntity<Void> desactivarUsuarioEmpresa(@PathVariable UUID uuid){
        usuarioEmpresaService.desactivarUsuarioEmpresa(uuid);
        return ResponseEntity.noContent().build();
    }
}