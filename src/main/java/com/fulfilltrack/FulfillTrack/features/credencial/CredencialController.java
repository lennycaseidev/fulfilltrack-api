package com.fulfilltrack.FulfillTrack.features.credencial;


import com.fulfilltrack.FulfillTrack.features.credencial.dto.CredencialResponseDTO;

import lombok.RequiredArgsConstructor;

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
    ResponseEntity<List<CredencialResponseDTO>> obtenerCredenciales(@RequestParam(required = false) String email){
        if(email != null) return ResponseEntity.ok(List.of(credencialService.obtenerCredencialPorEmail(email)));
        return ResponseEntity.ok(credencialService.obtenerCredenciales());
    }

    @GetMapping("/{uuid}")
    ResponseEntity<CredencialResponseDTO> obtenerCredencialPorUuid(@PathVariable UUID uuid){
        return ResponseEntity.ok(credencialService.obtenerCredencialPorUuid(uuid));
    }



    @PatchMapping("/{uuid}/asignar-permiso/{permisoUuid}")
    ResponseEntity<CredencialResponseDTO> asignarPermiso(@PathVariable UUID uuid, @PathVariable UUID permisoUuid){
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
