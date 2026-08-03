package com.juansierrasegurosbolivar.gestionpolizas.service.impl;

import com.juansierrasegurosbolivar.gestionpolizas.dto.request.CrearRiesgoRequest;
import com.juansierrasegurosbolivar.gestionpolizas.dto.response.RiesgoResponse;
import com.juansierrasegurosbolivar.gestionpolizas.entity.Poliza;
import com.juansierrasegurosbolivar.gestionpolizas.entity.Riesgo;
import com.juansierrasegurosbolivar.gestionpolizas.entity.enums.EstadoPoliza;
import com.juansierrasegurosbolivar.gestionpolizas.entity.enums.EstadoRiesgo;
import com.juansierrasegurosbolivar.gestionpolizas.entity.enums.TipoPoliza;
import com.juansierrasegurosbolivar.gestionpolizas.exception.BusinessException;
import com.juansierrasegurosbolivar.gestionpolizas.exception.ResourceNotFoundException;
import com.juansierrasegurosbolivar.gestionpolizas.mapper.RiesgoMapper;
import com.juansierrasegurosbolivar.gestionpolizas.repository.PolizaRepository;
import com.juansierrasegurosbolivar.gestionpolizas.repository.RiesgoRepository;
import com.juansierrasegurosbolivar.gestionpolizas.service.RiesgoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RiesgoServiceImpl implements RiesgoService {

    private final PolizaRepository polizaRepository;
    private final RiesgoRepository riesgoRepository;

    @Override
    public List<RiesgoResponse> consultarPorPoliza(
        Long polizaId
    ) {
        validarExistenciaPoliza(polizaId);

        return riesgoRepository
            .findByPoliza_IdOrderByIdAsc(polizaId)
            .stream()
            .map(RiesgoMapper::toResponse)
            .toList();
    }

    @Override
    @Transactional
    public RiesgoResponse crear(
        Long polizaId,
        CrearRiesgoRequest request
    ) {
        Poliza poliza = obtenerPoliza(polizaId);

        validarCreacionRiesgo(poliza);

        Riesgo riesgo = new Riesgo(
            request.getDescripcion().trim(),
            request.getDireccionInmueble().trim(),
            EstadoRiesgo.ACTIVO
        );

        poliza.agregarRiesgo(riesgo);

        Riesgo riesgoGuardado =
            riesgoRepository.save(riesgo);

        return RiesgoMapper.toResponse(riesgoGuardado);
    }

    @Override
    @Transactional
    public RiesgoResponse cancelar(Long riesgoId) {
        Riesgo riesgo = obtenerRiesgo(riesgoId);

        if (riesgo.getEstado() == EstadoRiesgo.CANCELADO) {
            return RiesgoMapper.toResponse(riesgo);
        }

        riesgo.setEstado(EstadoRiesgo.CANCELADO);
        riesgo.setFechaCancelacion(LocalDateTime.now());

        Riesgo riesgoCancelado =
            riesgoRepository.save(riesgo);

        return RiesgoMapper.toResponse(riesgoCancelado);
    }

    private void validarExistenciaPoliza(Long polizaId) {
        if (!polizaRepository.existsById(polizaId)) {
            throw new ResourceNotFoundException(
                "No se encontró la póliza con id "
                    + polizaId
            );
        }
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

    private Riesgo obtenerRiesgo(Long riesgoId) {
        return riesgoRepository
            .findById(riesgoId)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "No se encontró el riesgo con id "
                        + riesgoId
                )
            );
    }

    private void validarCreacionRiesgo(Poliza poliza) {
        if (poliza.getTipo() != TipoPoliza.COLECTIVA) {
            throw new BusinessException(
                "Solo se pueden agregar riesgos "
                    + "a pólizas colectivas"
            );
        }

        if (poliza.getEstado() == EstadoPoliza.CANCELADA) {
            throw new BusinessException(
                "No se pueden agregar riesgos "
                    + "a una póliza cancelada"
            );
        }
    }
}