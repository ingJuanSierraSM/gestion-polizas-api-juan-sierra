package com.juansierrasegurosbolivar.gestion_polizas.entity;

import com.juansierrasegurosbolivar.gestion_polizas.entity.enums.EstadoRiesgo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "riesgos",
    indexes = {
        @Index(
            name = "idx_riesgo_poliza_estado",
            columnList = "poliza_id, estado"
        )
    }
)
public class Riesgo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "poliza_id",
        nullable = false
    )
    private Poliza poliza;

    @Column(nullable = false, length = 200)
    private String descripcion;

    @Column(
        name = "direccion_inmueble",
        nullable = false,
        length = 250
    )
    private String direccionInmueble;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoRiesgo estado;

    @Column(name = "fecha_cancelacion")
    private LocalDateTime fechaCancelacion;

    @Version
    private Long version;

    public Riesgo() {
    }

    public Riesgo(
        String descripcion,
        String direccionInmueble,
        EstadoRiesgo estado
    ) {
        this.descripcion = descripcion;
        this.direccionInmueble = direccionInmueble;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public Poliza getPoliza() {
        return poliza;
    }

    public void setPoliza(Poliza poliza) {
        this.poliza = poliza;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccionInmueble() {
        return direccionInmueble;
    }

    public void setDireccionInmueble(String direccionInmueble) {
        this.direccionInmueble = direccionInmueble;
    }

    public EstadoRiesgo getEstado() {
        return estado;
    }

    public void setEstado(EstadoRiesgo estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCancelacion() {
        return fechaCancelacion;
    }

    public void setFechaCancelacion(LocalDateTime fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
    }

    public Long getVersion() {
        return version;
    }
}