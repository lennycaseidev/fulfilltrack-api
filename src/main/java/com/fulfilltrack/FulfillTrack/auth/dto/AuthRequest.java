package com.fulfilltrack.FulfillTrack.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(
        @NotBlank(message = "El username no puede estar vacío") String username,
        @NotBlank(message = "La contraseña no puede estar vacía") @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres") String password
) {}
