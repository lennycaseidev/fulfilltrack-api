package com.fulfilltrack.FulfillTrack.features.puesto;

import com.fulfilltrack.FulfillTrack.features.puesto.dto.PuestoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.puesto.dto.PuestoResponseDTO;
import com.fulfilltrack.FulfillTrack.features.puesto.IPuestoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/puestos")
@AllArgsConstructor
public class PuestoController {
    private final IPuestoService puestoService;

    @GetMapping
    public ResponseEntity<List<PuestoResponseDTO>> listarPuestos(){
        return ResponseEntity.ok(puestoService.listarPuestos());
    }
    @GetMapping("/{uuid}")
    public ResponseEntity<PuestoResponseDTO> obtenerPuestoPorUuid(@PathVariable UUID uuid){
        return ResponseEntity.ok(puestoService.obtenerPuestoPorUuid(uuid));
    }

    @PostMapping
    public ResponseEntity<PuestoResponseDTO> crearPuesto(@Valid @RequestBody PuestoRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(puestoService.crearPuesto(request));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<PuestoResponseDTO> actualizarPuesto(@PathVariable UUID uuid, @Valid @RequestBody PuestoRequestDTO request){
        return ResponseEntity.ok(puestoService.actualizarPuesto(uuid, request));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> eliminarPuesto(@PathVariable UUID uuid){
        puestoService.eliminarPuesto(uuid);
        return ResponseEntity.noContent().build();
    }


}
