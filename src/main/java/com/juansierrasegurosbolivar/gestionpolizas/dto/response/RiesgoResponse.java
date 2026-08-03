package com.juansierrasegurosbolivar.gestionpolizas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import com.juansierrasegurosbolivar.gestionpolizas.entity.enums.EstadoRiesgo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiesgoResponse {

    private Long id;
    private Long polizaId;
    private String descripcion;
    private String direccionInmueble;
    private EstadoRiesgo estado;
    private LocalDateTime fechaCancelacion;
    private Long version;
}