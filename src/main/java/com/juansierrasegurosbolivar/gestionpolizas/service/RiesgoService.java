package com.juansierrasegurosbolivar.gestionpolizas.service;

import com.juansierrasegurosbolivar.gestionpolizas.dto.request.CrearRiesgoRequest;
import com.juansierrasegurosbolivar.gestionpolizas.dto.response.RiesgoResponse;

import java.util.List;

public interface RiesgoService {

    List<RiesgoResponse> consultarPorPoliza(Long polizaId);

    RiesgoResponse crear(
        Long polizaId,
        CrearRiesgoRequest request
    );

    RiesgoResponse cancelar(Long riesgoId);
}