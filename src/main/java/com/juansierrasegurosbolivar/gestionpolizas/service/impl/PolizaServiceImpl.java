package com.juansierrasegurosbolivar.gestionpolizas.service.impl;

import com.juansierrasegurosbolivar.gestionpolizas.dto.response.PolizaResponse;
import com.juansierrasegurosbolivar.gestionpolizas.entity.Poliza;
import com.juansierrasegurosbolivar.gestionpolizas.entity.Riesgo;
import com.juansierrasegurosbolivar.gestionpolizas.entity.enums.EstadoPoliza;
import com.juansierrasegurosbolivar.gestionpolizas.entity.enums.EstadoRiesgo;
import com.juansierrasegurosbolivar.gestionpolizas.entity.enums.TipoPoliza;
import com.juansierrasegurosbolivar.gestionpolizas.exception.BusinessException;
import com.juansierrasegurosbolivar.gestionpolizas.exception.ResourceNotFoundException;
import com.juansierrasegurosbolivar.gestionpolizas.mapper.PolizaMapper;
import com.juansierrasegurosbolivar.gestionpolizas.repository.PolizaRepository;
import com.juansierrasegurosbolivar.gestionpolizas.repository.RiesgoRepository;
import com.juansierrasegurosbolivar.gestionpolizas.service.PolizaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.juansierrasegurosbolivar.gestionpolizas.dto.request.CoreEventoRequest;
import com.juansierrasegurosbolivar.gestionpolizas.integration.CoreClient;
import com.juansierrasegurosbolivar.gestionpolizas.integration.OperacionCore;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PolizaServiceImpl implements PolizaService {

    private static final BigDecimal CIEN = new BigDecimal("100");
    private static final int ESCALA_MONEDA = 2;
    private static final int ESCALA_CALCULO = 10;

    private final PolizaRepository polizaRepository;
    private final RiesgoRepository riesgoRepository;
    private final CoreClient coreClient;

    @Override
    public List<PolizaResponse> consultar(
        TipoPoliza tipo,
        EstadoPoliza estado
    ) {
        List<Poliza> polizas;

        if (tipo != null && estado != null) {
            polizas = polizaRepository
                .findByTipoAndEstadoOrderByIdAsc(tipo, estado);

        } else if (tipo != null) {
            polizas = polizaRepository
                .findByTipoOrderByIdAsc(tipo);

        } else if (estado != null) {
            polizas = polizaRepository
                .findByEstadoOrderByIdAsc(estado);

        } else {
            polizas = polizaRepository
                .findAllByOrderByIdAsc();
        }

        return polizas.stream()
            .map(PolizaMapper::toResponse)
            .toList();
    }

    @Override
    @Transactional
    public PolizaResponse renovar(
        Long polizaId,
        BigDecimal ipcPorcentaje
    ) {
        Poliza poliza = obtenerPoliza(polizaId);

        validarRenovacion(poliza, ipcPorcentaje);

        BigDecimal factorIncremento = BigDecimal.ONE.add(
            ipcPorcentaje.divide(
                CIEN,
                ESCALA_CALCULO,
                RoundingMode.HALF_UP
            )
        );

        BigDecimal canonNuevo = poliza
            .getCanonMensual()
            .multiply(factorIncremento)
            .setScale(
                ESCALA_MONEDA,
                RoundingMode.HALF_UP
            );

        BigDecimal primaNueva = canonNuevo
            .multiply(
                BigDecimal.valueOf(
                    poliza.getMesesVigenciaInicial()
                )
            )
            .setScale(
                ESCALA_MONEDA,
                RoundingMode.HALF_UP
            );

        LocalDate nuevaFechaInicio = poliza
            .getFechaFin()
            .plusDays(1);

        LocalDate nuevaFechaFin = nuevaFechaInicio
            .plusMonths(poliza.getMesesVigenciaInicial())
            .minusDays(1);
        
        coreClient.enviarEvento(
            new CoreEventoRequest(
                OperacionCore.RENOVAR_POLIZA,
                polizaId,
                null
            )
        );

        poliza.setFechaInicio(nuevaFechaInicio);
        poliza.setFechaFin(nuevaFechaFin);
        poliza.setCanonMensual(canonNuevo);
        poliza.setPrima(primaNueva);
        poliza.setEstado(EstadoPoliza.RENOVADA);

        Poliza polizaActualizada =
            polizaRepository.save(poliza);

        return PolizaMapper.toResponse(polizaActualizada);
    }

    @Override
    @Transactional
    public PolizaResponse cancelar(Long polizaId) {
        Poliza poliza = obtenerPoliza(polizaId);

        if (poliza.getEstado() == EstadoPoliza.CANCELADA) {
            return PolizaMapper.toResponse(poliza);
        }

        coreClient.enviarEvento(
            new CoreEventoRequest(
                OperacionCore.CANCELAR_POLIZA,
                polizaId,
                null
            )
        );

        LocalDateTime fechaCancelacion =
            LocalDateTime.now();

        poliza.setEstado(EstadoPoliza.CANCELADA);
        poliza.setFechaCancelacion(fechaCancelacion);

        List<Riesgo> riesgosActivos = riesgoRepository
            .findByPoliza_IdAndEstadoOrderByIdAsc(
                polizaId,
                EstadoRiesgo.ACTIVO
            );

        riesgosActivos.forEach(riesgo -> {
            riesgo.setEstado(EstadoRiesgo.CANCELADO);
            riesgo.setFechaCancelacion(fechaCancelacion);
        });

        if (!riesgosActivos.isEmpty()) {
            riesgoRepository.saveAll(riesgosActivos);
        }

        Poliza polizaCancelada =
            polizaRepository.save(poliza);

        return PolizaMapper.toResponse(polizaCancelada);
    }

    private Poliza obtenerPoliza(Long polizaId) {
        return polizaRepository
            .findById(polizaId)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "No se encontró la póliza con id "
                        + polizaId
                )
            );
    }

    private void validarRenovacion(
        Poliza poliza,
        BigDecimal ipcPorcentaje
    ) {
        if (poliza.getEstado() == EstadoPoliza.CANCELADA) {
            throw new BusinessException(
                "No se puede renovar una póliza cancelada"
            );
        }

        if (
            ipcPorcentaje == null
                || ipcPorcentaje.compareTo(BigDecimal.ZERO) <= 0
        ) {
            throw new BusinessException(
                "El porcentaje de IPC debe ser mayor que cero"
            );
        }
    }
}