package com.fulfilltrack.FulfillTrack.features.deposito;

import com.fulfilltrack.FulfillTrack.features.deposito.dto.DepositoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.deposito.dto.DepositoResponseDTO;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.NativeQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/depositos")
@RequiredArgsConstructor
public class DepositoController {
    private final IDepositoService depositoService;

    @GetMapping
    ResponseEntity<List<DepositoResponseDTO>> listarDepositos(){
        return ResponseEntity.ok(depositoService.obtenerDepositos());
    }

    @GetMapping("/{uuid}")
    ResponseEntity<DepositoResponseDTO> obtenerDepositoPorUuid(@PathVariable UUID uuid){
        return ResponseEntity.ok(depositoService.obtenerDepositoPorUuid(uuid));
    }

    @PostMapping
    ResponseEntity<DepositoResponseDTO> crearDeposito(@Valid @RequestBody DepositoRequestDTO request){
       DepositoResponseDTO respuesta = depositoService.crearDeposito(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping("/{uuid}")
    ResponseEntity<DepositoResponseDTO> actualizarDeposito(@PathVariable UUID uuid, @Valid @RequestBody DepositoRequestDTO request){
        return ResponseEntity.ok(depositoService.actualizarDeposito(uuid, request));
    }

    @PatchMapping("/{uuid}/desactivar")
    ResponseEntity<Void> desactivarDeposito(@PathVariable UUID uuid){
        depositoService.desactivarDeposito(uuid);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{uuid}/activar")
    ResponseEntity<Void> activarDeposito(@PathVariable UUID uuid){
        depositoService.activarDeposito(uuid);
        return ResponseEntity.noContent().build();
    }

}