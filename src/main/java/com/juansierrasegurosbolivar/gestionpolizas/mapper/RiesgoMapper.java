package com.juansierrasegurosbolivar.gestionpolizas.mapper;

import com.juansierrasegurosbolivar.gestionpolizas.dto.response.RiesgoResponse;
import com.juansierrasegurosbolivar.gestionpolizas.entity.Riesgo;

public final class RiesgoMapper {

    private RiesgoMapper() {
    }

    public static RiesgoResponse toResponse(Riesgo riesgo) {
        return new RiesgoResponse(
            riesgo.getId(),
            riesgo.getPoliza().getId(),
            riesgo.getDescripcion(),
            riesgo.getDireccionInmueble(),
            riesgo.getEstado(),
            riesgo.getFechaCancelacion(),
            riesgo.getVersion()
        );
    }
}