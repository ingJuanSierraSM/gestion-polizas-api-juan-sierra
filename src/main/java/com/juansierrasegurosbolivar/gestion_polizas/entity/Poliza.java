package com.juansierrasegurosbolivar.gestion_polizas.entity;

import com.juansierrasegurosbolivar.gestion_polizas.entity.enums.EstadoPoliza;
import com.juansierrasegurosbolivar.gestion_polizas.entity.enums.TipoPoliza;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "polizas",
    indexes = {
        @Index(
            name = "idx_poliza_tipo_estado",
            columnList = "tipo, estado"
        )
    }
)
public class Poliza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoPoliza tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPoliza estado;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "meses_vigencia_inicial", nullable = false)
    private Integer mesesVigenciaInicial;

    @Column(
        name = "canon_mensual",
        nullable = false,
        precision = 19,
        scale = 2
    )
    private BigDecimal canonMensual;

    @Column(
        nullable = false,
        precision = 19,
        scale = 2
    )
    private BigDecimal prima;

    @Column(name = "fecha_cancelacion")
    private LocalDateTime fechaCancelacion;

    @Version
    private Long version;

    @OneToMany(
        mappedBy = "poliza",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY
    )
    private List<Riesgo> riesgos = new ArrayList<>();

    public Poliza() {
    }

    public Poliza(
        TipoPoliza tipo,
        EstadoPoliza estado,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        Integer mesesVigenciaInicial,
        BigDecimal canonMensual,
        BigDecimal prima
    ) {
        this.tipo = tipo;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.mesesVigenciaInicial = mesesVigenciaInicial;
        this.canonMensual = canonMensual;
        this.prima = prima;
    }

    public void agregarRiesgo(Riesgo riesgo) {
        riesgos.add(riesgo);
        riesgo.setPoliza(this);
    }

    public Long getId() {
        return id;
    }

    public TipoPoliza getTipo() {
        return tipo;
    }

    public void setTipo(TipoPoliza tipo) {
        this.tipo = tipo;
    }

    public EstadoPoliza getEstado() {
        return estado;
    }

    public void setEstado(EstadoPoliza estado) {
        this.estado = estado;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getMesesVigenciaInicial() {
        return mesesVigenciaInicial;
    }

    public void setMesesVigenciaInicial(Integer mesesVigenciaInicial) {
        this.mesesVigenciaInicial = mesesVigenciaInicial;
    }

    public BigDecimal getCanonMensual() {
        return canonMensual;
    }

    public void setCanonMensual(BigDecimal canonMensual) {
        this.canonMensual = canonMensual;
    }

    public BigDecimal getPrima() {
        return prima;
    }

    public void setPrima(BigDecimal prima) {
        this.prima = prima;
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

    public List<Riesgo> getRiesgos() {
        return riesgos;
    }

    public void setRiesgos(List<Riesgo> riesgos) {
        this.riesgos = riesgos;
    }
}