package com.juansierrasegurosbolivar.gestion_polizas.repository;

import com.juansierrasegurosbolivar.gestion_polizas.entity.Poliza;
import com.juansierrasegurosbolivar.gestion_polizas.entity.enums.EstadoPoliza;
import com.juansierrasegurosbolivar.gestion_polizas.entity.enums.TipoPoliza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolizaRepository extends JpaRepository<Poliza, Long> {

    List<Poliza> findAllByOrderByIdAsc();

    List<Poliza> findByTipoOrderByIdAsc(TipoPoliza tipo);

    List<Poliza> findByEstadoOrderByIdAsc(EstadoPoliza estado);

    List<Poliza> findByTipoAndEstadoOrderByIdAsc(
        TipoPoliza tipo,
        EstadoPoliza estado
    );
}