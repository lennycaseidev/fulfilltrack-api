package com.fulfilltrack.FulfillTrack.features.usuario;

import com.fulfilltrack.FulfillTrack.features.usuario.dto.UsuarioRequestDTO;
import com.fulfilltrack.FulfillTrack.features.usuario.dto.UsuarioResponseDTO;
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

@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema")
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final IUsuarioService usuarioService;

    @Operation(summary = "Listar todos los usuarios")
    @GetMapping
    ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios(){
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @Operation(summary = "Listar usuarios activos")
    @GetMapping("/activos")
    ResponseEntity<List<UsuarioResponseDTO>> listarUsuariosActivos(){
        return ResponseEntity.ok(usuarioService.listarUsuariosActivos());
    }

    @Operation(summary = "Obtener usuario por UUID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{uuid}")
    ResponseEntity<UsuarioResponseDTO> obtenerUsuarioPorUuid(@PathVariable UUID uuid){
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorUuid(uuid));
    }

    @Operation(summary = "Registrar usuario")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario registrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    ResponseEntity<UsuarioResponseDTO> registrarUsuario(@Valid @RequestBody UsuarioRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.registrarUsuario(request));
    }
}