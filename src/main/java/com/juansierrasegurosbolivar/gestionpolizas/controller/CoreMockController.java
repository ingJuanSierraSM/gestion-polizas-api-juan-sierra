package com.juansierrasegurosbolivar.gestionpolizas.controller;

import com.juansierrasegurosbolivar.gestionpolizas.dto.request.CoreEventoRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/core-mock")
public class CoreMockController {

    @PostMapping("/evento")
    public ResponseEntity<Void> recibirEvento(
        @Valid @RequestBody CoreEventoRequest request
    ) {
        log.info(
            "CORE MOCK - operación={}, polizaId={}, riesgoId={}",
            request.getOperacion(),
            request.getPolizaId(),
            request.getRiesgoId()
        );

        return ResponseEntity.noContent().build();
    }
}