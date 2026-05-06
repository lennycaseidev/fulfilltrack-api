package com.fulfilltrack.FulfillTrack.features.infoEmpleados;

import com.fulfilltrack.FulfillTrack.features.infoEmpleados.dto.InfoEmpleadosRequestDTO;
import com.fulfilltrack.FulfillTrack.features.infoEmpleados.dto.InfoEmpleadosResponseDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/empleados")
@AllArgsConstructor
public class InfoEmpleadosController {
    private final IInfoEmpleadosService infoEmpleadosService;

    @GetMapping
    public ResponseEntity<List<InfoEmpleadosResponseDTO>> listarEmpleados(){
        return ResponseEntity.ok(infoEmpleadosService.listarEmpleados());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<InfoEmpleadosResponseDTO> obtenerEmpleadoPorUuid(@PathVariable UUID uuid){
        return ResponseEntity.ok(infoEmpleadosService.obtenerEmpleadoPorUuid(uuid));
    }

    @PostMapping
    public ResponseEntity<InfoEmpleadosResponseDTO> crearEmpleado(@Valid @RequestBody InfoEmpleadosRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(infoEmpleadosService.crearEmpleado(request));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<InfoEmpleadosResponseDTO> actualizarEmpleado(@PathVariable UUID uuid,
                                                                       @Valid @RequestBody InfoEmpleadosRequestDTO request){
        return ResponseEntity.ok(infoEmpleadosService.actualizarEmpleado(uuid,request));
    }
    @PatchMapping("/{uuid}/activar")
    public ResponseEntity<Void> activarEmpleado(@PathVariable UUID uuid){
        infoEmpleadosService.activarEmpleado(uuid);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{uuid}/desactivar")
    public ResponseEntity<Void> desactivarEmpleado(@PathVariable UUID uuid){
        infoEmpleadosService.desactivarEmpleado(uuid);
        return ResponseEntity.noContent().build();
    }

}
