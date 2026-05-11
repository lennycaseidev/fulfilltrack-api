package com.fulfilltrack.FulfillTrack.features.permiso.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PermisoRequestDTO {
    @NotBlank(message = "El nombre del permiso es obligatorio")
    private String nombrePermiso;
}
