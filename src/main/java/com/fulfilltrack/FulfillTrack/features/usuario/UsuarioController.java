package com.fulfilltrack.FulfillTrack.features.usuario;

import com.fulfilltrack.FulfillTrack.auth.permisos.Roles;
import com.fulfilltrack.FulfillTrack.features.usuario.dto.CambiarPasswordDTO;
import com.fulfilltrack.FulfillTrack.features.usuario.dto.UsuarioResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema")
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final IUsuarioService usuarioService;

    @PreAuthorize("hasAuthority('VER_USUARIOS')")
    @Operation(summary = "Listar todos los usuarios")
    @GetMapping
    ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios(){
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @PreAuthorize("hasAuthority('VER_USUARIOS')")
    @Operation(summary = "Listar usuarios activos")
    @GetMapping("/activos")
    ResponseEntity<List<UsuarioResponseDTO>> listarUsuariosActivos(){
        return ResponseEntity.ok(usuarioService.listarUsuariosActivos());
    }

    @PreAuthorize("hasAuthority('VER_USUARIOS')")
    @Operation(summary = "Obtener usuario por UUID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{uuid}")
    ResponseEntity<UsuarioResponseDTO> obtenerUsuarioPorUuid(@PathVariable UUID uuid){
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorUuid(uuid));
    }

    @PreAuthorize("hasAuthority('ACTUALIZAR_USUARIO')")
    @PatchMapping("/{uuid}/rol")
    ResponseEntity<Void> asignarRol(@PathVariable UUID uuid, @RequestParam Roles rol){
        usuarioService.asignarRol(uuid, rol);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ACTUALIZAR_USUARIO')")
    @Operation(summary = "Cambiar contraseña de usuario")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Contraseña actualizada"),
        @ApiResponse(responseCode = "400", description = "Contraseña actual incorrecta"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PatchMapping("/{uuid}/password")
    ResponseEntity<Void> cambiarPassword(@PathVariable UUID uuid, @Valid @RequestBody CambiarPasswordDTO dto){
        usuarioService.cambiarPassword(uuid, dto);
        return ResponseEntity.noContent().build();
    }

}