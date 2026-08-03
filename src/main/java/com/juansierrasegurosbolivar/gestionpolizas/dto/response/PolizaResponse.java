package com.juansierrasegurosbolivar.gestionpolizas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.juansierrasegurosbolivar.gestionpolizas.entity.enums.EstadoPoliza;
import com.juansierrasegurosbolivar.gestionpolizas.entity.enums.TipoPoliza;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PolizaResponse {

    private Long id;
    private TipoPoliza tipo;
    private EstadoPoliza estado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer mesesVigenciaInicial;
    private BigDecimal canonMensual;
    private BigDecimal prima;
    private LocalDateTime fechaCancelacion;
    private Long version;
}