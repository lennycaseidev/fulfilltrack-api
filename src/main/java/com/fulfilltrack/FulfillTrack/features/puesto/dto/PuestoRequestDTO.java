package com.fulfilltrack.FulfillTrack.features.puesto.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PuestoRequestDTO {
    @NotBlank(message = "El nombre del puesto es obligatorio")
    @Size(max = 100, message = "El nombre del puesto no puede superar los 100 caracteres")
    private String nombrePuesto;
    @Size(max=255, message = "El nombre del puesto no puede superar los 100 caracteres")
    private String descripcion;
}
