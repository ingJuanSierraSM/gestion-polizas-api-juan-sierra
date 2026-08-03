package com.juansierrasegurosbolivar.gestionpolizas.dto.request;

import com.juansierrasegurosbolivar.gestionpolizas.integration.OperacionCore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoreEventoRequest {

    @NotNull(message = "La operación del CORE es obligatoria")
    private OperacionCore operacion;

    @NotNull(message = "El identificador de la póliza es obligatorio")
    @Positive(message = "El identificador de la póliza debe ser positivo")
    private Long polizaId;

    @Positive(message = "El identificador del riesgo debe ser positivo")
    private Long riesgoId;
}