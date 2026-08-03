package com.juansierrasegurosbolivar.gestionpolizas.dto.request;

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
public class CrearRiesgoRequest {

    @NotBlank(message = "La descripción es obligatoria")
    @Size(
        max = 200,
        message = "La descripción no puede superar los 200 caracteres"
    )
    private String descripcion;

    @NotBlank(message = "La dirección del inmueble es obligatoria")
    @Size(
        max = 250,
        message = "La dirección no puede superar los 250 caracteres"
    )
    private String direccionInmueble;
}