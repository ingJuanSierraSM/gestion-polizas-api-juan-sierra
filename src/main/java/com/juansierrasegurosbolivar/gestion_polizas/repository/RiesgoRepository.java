package com.juansierrasegurosbolivar.gestion_polizas.repository;

import com.juansierrasegurosbolivar.gestion_polizas.entity.Riesgo;
import com.juansierrasegurosbolivar.gestion_polizas.entity.enums.EstadoRiesgo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RiesgoRepository extends JpaRepository<Riesgo, Long> {

    List<Riesgo> findByPoliza_IdOrderByIdAsc(Long polizaId);

    List<Riesgo> findByPoliza_IdAndEstadoOrderByIdAsc(
        Long polizaId,
        EstadoRiesgo estado
    );

    boolean existsByPoliza_Id(Long polizaId);
}