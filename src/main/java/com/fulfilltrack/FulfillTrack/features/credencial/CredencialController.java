package com.fulfilltrack.FulfillTrack.features.credencial;

import com.fulfilltrack.FulfillTrack.features.credencial.dto.CredencialRequestDTO;
import com.fulfilltrack.FulfillTrack.features.credencial.dto.CredencialResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/credenciales")
@RequiredArgsConstructor
public class CredencialController {
    private final ICredencialService credencialService;



    @GetMapping
    ResponseEntity<List<CredencialResponseDTO>> obtenerCredenciales(){
        return ResponseEntity.ok(credencialService.obtenerCredenciales());
    }

    @GetMapping("/{uuid}")
    ResponseEntity<CredencialResponseDTO> obtenerCredencialPorUuid(@PathVariable UUID uuid){
        return ResponseEntity.ok(credencialService.obtenerCredencialPorUuid(uuid));
    }

    @PostMapping
    ResponseEntity<CredencialResponseDTO> registrarCredencial(@Valid @RequestBody CredencialRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(credencialService.registrarCredencial(request));
    }

    @PatchMapping("/{uuid}/asignar-permiso")
    ResponseEntity<CredencialResponseDTO> asignarPermiso(@PathVariable UUID uuid, @RequestBody UUID permisoUuid){
        return ResponseEntity.ok(credencialService.asignarPermiso(uuid,permisoUuid));
    }

    @PatchMapping("/{uuid}/activar")
    ResponseEntity<Void> activarCredencial(@PathVariable UUID uuid){
        credencialService.activarCredencial(uuid);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{uuid}/desactivar")
    ResponseEntity<Void> desactivarCredencial(@PathVariable UUID uuid){
        credencialService.desactivarCredencial(uuid);
        return ResponseEntity.noContent().build();
    }


}
