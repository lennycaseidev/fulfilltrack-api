package com.fulfilltrack.FulfillTrack.features.usuarioEmpresa;

import com.fulfilltrack.FulfillTrack.features.usuarioEmpresa.dto.UsuarioEmpresaRequestDTO;
import com.fulfilltrack.FulfillTrack.features.usuarioEmpresa.dto.UsuarioEmpresaResponseDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios-empresa")
@AllArgsConstructor
public class UsuarioEmpresaController {
private final IUsuarioEmpresaService usuarioEmpresaService;
    @GetMapping
    public ResponseEntity<List<UsuarioEmpresaResponseDTO>> listarUsuariosEmpresa(){
        return ResponseEntity.ok(usuarioEmpresaService.listarUsuariosEmpresa());
    }
    @GetMapping("/{uuid}")
    public ResponseEntity<UsuarioEmpresaResponseDTO> obtenerUsuarioEmpresaPorUuid(@PathVariable UUID uuid){
        return ResponseEntity.ok(usuarioEmpresaService.obtenerUsuarioEmpresaPorUuid(uuid));
    }
    @GetMapping("/empresa/{empresaUuid}")
    public ResponseEntity<List<UsuarioEmpresaResponseDTO>> listarPorEmpresa(@PathVariable UUID empresaUuid){
        return ResponseEntity.ok(usuarioEmpresaService.listarUsuariosPorEmpresa(empresaUuid));
    }

    @PostMapping
    public ResponseEntity<UsuarioEmpresaResponseDTO> crearUsuarioEmpresa(@Valid @RequestBody UsuarioEmpresaRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioEmpresaService.crearUsuarioEmpresa(request));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<UsuarioEmpresaResponseDTO> actualizarUsuarioEmpresa(@PathVariable UUID uuid,
                                                                              @Valid @RequestBody UsuarioEmpresaRequestDTO request){
        return ResponseEntity.ok(usuarioEmpresaService.actualizarUsuarioEmpresa(uuid,request));
    }

    @PatchMapping("/{uuid}/activar")
    public ResponseEntity<Void> activarUsuarioEmpresa(@PathVariable UUID uuid){
        usuarioEmpresaService.activarUsuarioEmpresa(uuid);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{uuid}/desactivar")
    public ResponseEntity<Void> desactivarUsuarioEmpresa(@PathVariable UUID uuid){
        usuarioEmpresaService.desactivarUsuarioEmpresa(uuid);
        return ResponseEntity.noContent().build();
    }
}