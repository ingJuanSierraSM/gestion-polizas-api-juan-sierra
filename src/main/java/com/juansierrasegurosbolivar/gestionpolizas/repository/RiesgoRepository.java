package com.juansierrasegurosbolivar.gestionpolizas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.juansierrasegurosbolivar.gestionpolizas.entity.Riesgo;
import com.juansierrasegurosbolivar.gestionpolizas.entity.enums.EstadoRiesgo;

import java.util.List;

public interface RiesgoRepository extends JpaRepository<Riesgo, Long> {

    List<Riesgo> findByPoliza_IdOrderByIdAsc(Long polizaId);

    List<Riesgo> findByPoliza_IdAndEstadoOrderByIdAsc(
        Long polizaId,
        EstadoRiesgo estado
    );

    boolean existsByPoliza_Id(Long polizaId);
}