package com.fulfilltrack.FulfillTrack.features.empresa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaRequestDTO {
    private String nombreEmpresa;
    private String email;
    private BigDecimal costoPorEnvio;
}
