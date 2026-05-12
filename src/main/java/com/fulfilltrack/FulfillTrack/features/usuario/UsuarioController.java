package com.fulfilltrack.FulfillTrack.features.usuario;

import com.fulfilltrack.FulfillTrack.features.usuario.dto.UsuarioRequestDTO;
import com.fulfilltrack.FulfillTrack.features.usuario.dto.UsuarioResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final IUsuarioService usuarioService;

    @GetMapping
    ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios(){
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }
    @GetMapping("/activos")
    ResponseEntity<List<UsuarioResponseDTO>> listarUsuariosActivos(){
        return ResponseEntity.ok(usuarioService.listarUsuariosActivos());
    }
    @GetMapping("/{uuid}")
    ResponseEntity<UsuarioResponseDTO> obtenerUsuarioPorUuid(@PathVariable UUID uuid){
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorUuid(uuid));
    }

    @PostMapping
    ResponseEntity<UsuarioResponseDTO> registrarUsuario(@Valid @RequestBody UsuarioRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.registrarUsuario(request));
    }



}
