package com.juansierrasegurosbolivar.gestionpolizas.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RenovarPolizaRequest {

    @NotNull(message = "El porcentaje de IPC es obligatorio")
    @DecimalMin(
        value = "0.0",
        inclusive = false,
        message = "El porcentaje de IPC debe ser mayor que cero"
    )
    private BigDecimal ipcPorcentaje;
}