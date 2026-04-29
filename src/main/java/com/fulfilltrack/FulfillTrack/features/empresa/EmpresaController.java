package com.fulfilltrack.FulfillTrack.features.empresa;

import com.fulfilltrack.FulfillTrack.features.empresa.dto.EmpresaRequestDTO;
import com.fulfilltrack.FulfillTrack.features.empresa.dto.EmpresaResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor
public class EmpresaController {
    private final IEmpresaService empresaService;

    @GetMapping
    public ResponseEntity<List<EmpresaResponseDTO>> listarEmpresas(){
        return ResponseEntity.ok(empresaService.listarEmpresas());
    }
    @GetMapping("/{uuid}")
    public ResponseEntity<EmpresaResponseDTO> obtenerEmpresaPorUuid(@PathVariable UUID uuid){
        return ResponseEntity.ok(empresaService.obtenerEmpresaPorUuid(uuid));
    }

    @PostMapping
    public ResponseEntity<EmpresaResponseDTO> crearEmpresa(@RequestBody @Valid EmpresaRequestDTO nuevaEmpresa){
        EmpresaResponseDTO respuesta = empresaService.crearEmpresa(nuevaEmpresa);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<EmpresaResponseDTO> actualizarEmpresa(@PathVariable UUID uuid
            , @RequestBody @Valid EmpresaRequestDTO modificar){
        return ResponseEntity.ok(empresaService.actualizarEmpresa(uuid, modificar));
    }

    @PatchMapping("/{uuid}/desactivar")
    public ResponseEntity<Void> desactivarEmpresa(@PathVariable UUID uuid) {
        empresaService.desactivarEmpresa(uuid);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{uuid}/activar")
    public ResponseEntity<Void> activarEmpresa(@PathVariable UUID uuid){
        empresaService.activarEmpresa(uuid);
        return ResponseEntity.noContent().build();
    }





}
