package com.juansierrasegurosbolivar.gestionpolizas.mapper;

import com.juansierrasegurosbolivar.gestionpolizas.dto.response.PolizaResponse;
import com.juansierrasegurosbolivar.gestionpolizas.entity.Poliza;

public final class PolizaMapper {

    private PolizaMapper() {
    }

    public static PolizaResponse toResponse(Poliza poliza) {
        return new PolizaResponse(
            poliza.getId(),
            poliza.getTipo(),
            poliza.getEstado(),
            poliza.getFechaInicio(),
            poliza.getFechaFin(),
            poliza.getMesesVigenciaInicial(),
            poliza.getCanonMensual(),
            poliza.getPrima(),
            poliza.getFechaCancelacion(),
            poliza.getVersion()
        );
    }
}