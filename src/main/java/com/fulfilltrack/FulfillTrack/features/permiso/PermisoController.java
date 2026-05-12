package com.fulfilltrack.FulfillTrack.features.permiso;

import com.fulfilltrack.FulfillTrack.features.permiso.dto.PermisoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.permiso.dto.PermisoResponseDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/permisos")
@AllArgsConstructor
public class PermisoController {
    private final IPermisoService permisoService;
    @GetMapping
    public ResponseEntity<List<PermisoResponseDTO>> listarPermisos(){
        return ResponseEntity.ok(permisoService.listarPermisos());
    }
    @PostMapping
    public ResponseEntity<PermisoResponseDTO> crearPermiso(@Valid @RequestBody PermisoRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(permisoService.crearPermiso(request));
    }
    @PutMapping("/{uuid}")
    public ResponseEntity<PermisoResponseDTO> actualizarPermiso(@PathVariable UUID uuid, @Valid@RequestBody PermisoRequestDTO request){
        return ResponseEntity.ok(permisoService.actualizarPermiso(uuid,request));
    }


}
