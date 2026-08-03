package com.juansierrasegurosbolivar.gestionpolizas.service;

import com.juansierrasegurosbolivar.gestionpolizas.dto.response.PolizaResponse;
import com.juansierrasegurosbolivar.gestionpolizas.entity.enums.EstadoPoliza;
import com.juansierrasegurosbolivar.gestionpolizas.entity.enums.TipoPoliza;

import java.math.BigDecimal;
import java.util.List;

public interface PolizaService {

    List<PolizaResponse> consultar(
        TipoPoliza tipo,
        EstadoPoliza estado
    );

    PolizaResponse renovar(
        Long polizaId,
        BigDecimal ipcPorcentaje
    );

    PolizaResponse cancelar(Long polizaId);
}