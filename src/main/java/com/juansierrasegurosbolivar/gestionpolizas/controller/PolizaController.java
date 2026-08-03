package com.juansierrasegurosbolivar.gestionpolizas.controller;

import com.juansierrasegurosbolivar.gestionpolizas.dto.request.CrearRiesgoRequest;
import com.juansierrasegurosbolivar.gestionpolizas.dto.request.RenovarPolizaRequest;
import com.juansierrasegurosbolivar.gestionpolizas.dto.response.PolizaResponse;
import com.juansierrasegurosbolivar.gestionpolizas.dto.response.RiesgoResponse;
import com.juansierrasegurosbolivar.gestionpolizas.entity.enums.EstadoPoliza;
import com.juansierrasegurosbolivar.gestionpolizas.entity.enums.TipoPoliza;
import com.juansierrasegurosbolivar.gestionpolizas.service.PolizaService;
import com.juansierrasegurosbolivar.gestionpolizas.service.RiesgoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/polizas")
@RequiredArgsConstructor
public class PolizaController {

    private final PolizaService polizaService;
    private final RiesgoService riesgoService;

    @GetMapping
    public ResponseEntity<List<PolizaResponse>> consultar(
        @RequestParam(required = false) TipoPoliza tipo,
        @RequestParam(required = false) EstadoPoliza estado
    ) {
        return ResponseEntity.ok(
            polizaService.consultar(tipo, estado)
        );
    }

    @GetMapping("/{id}/riesgos")
    public ResponseEntity<List<RiesgoResponse>> consultarRiesgos(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(
            riesgoService.consultarPorPoliza(id)
        );
    }

    @PostMapping("/{id}/renovar")
    public ResponseEntity<PolizaResponse> renovar(
        @PathVariable Long id,
        @Valid @RequestBody RenovarPolizaRequest request
    ) {
        return ResponseEntity.ok(
            polizaService.renovar(
                id,
                request.getIpcPorcentaje()
            )
        );
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<PolizaResponse> cancelar(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(
            polizaService.cancelar(id)
        );
    }

    @PostMapping("/{id}/riesgos")
    public ResponseEntity<RiesgoResponse> agregarRiesgo(
        @PathVariable Long id,
        @Valid @RequestBody CrearRiesgoRequest request
    ) {
        RiesgoResponse riesgoCreado =
            riesgoService.crear(id, request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(riesgoCreado);
    }
}