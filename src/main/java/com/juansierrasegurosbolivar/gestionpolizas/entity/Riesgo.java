package com.juansierrasegurosbolivar.gestionpolizas.entity;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import com.juansierrasegurosbolivar.gestionpolizas.entity.enums.EstadoRiesgo;

@Getter
@Setter
@NoArgsConstructor
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


    public Riesgo(
        String descripcion,
        String direccionInmueble,
        EstadoRiesgo estado
    ) {
        this.descripcion = descripcion;
        this.direccionInmueble = direccionInmueble;
        this.estado = estado;
    }

}