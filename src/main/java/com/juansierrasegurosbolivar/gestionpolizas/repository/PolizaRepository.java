package com.juansierrasegurosbolivar.gestionpolizas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.juansierrasegurosbolivar.gestionpolizas.entity.Poliza;
import com.juansierrasegurosbolivar.gestionpolizas.entity.enums.EstadoPoliza;
import com.juansierrasegurosbolivar.gestionpolizas.entity.enums.TipoPoliza;

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