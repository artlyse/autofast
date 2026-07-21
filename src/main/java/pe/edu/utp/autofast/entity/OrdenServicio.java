package pe.edu.utp.autofast.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "orden_servicio")
public class OrdenServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden")
    private Long id;

    @Column(name = "numero_orden", length = 20, unique = true, nullable = false)
    private String numeroOrden;

    @Column(name = "fecha_apertura", nullable = false)
    private LocalDateTime fechaApertura;

    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;

    @Column(name = "estado", length = 30, nullable = false)
    private String estado;

    @Column(name = "problema_reportado", columnDefinition = "TEXT")
    private String problemaReportado;

    @Column(name = "diagnostico", columnDefinition = "TEXT")
    private String diagnostico;

    @Column(name = "trabajo_realizado", columnDefinition = "TEXT")
    private String trabajoRealizado;

    @Column(name = "mano_obra", nullable = false)
    private BigDecimal manoObra = BigDecimal.ZERO;

    @Column(name = "total_repuestos", nullable = false)
    private BigDecimal totalRepuestos = BigDecimal.ZERO;

    @Column(name = "total", nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "activo")
    private Boolean activo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_id")
    private Tecnico tecnico;

    @OneToOne(mappedBy = "ordenServicio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Diagnostico diagnosticoEntity;

    @OneToMany(mappedBy = "ordenServicio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrdenRepuesto> repuestosUtilizados = new ArrayList<>();

    public OrdenServicio() {}

    @PrePersist
    public void prePersist() {
        fechaApertura = LocalDateTime.now();
        if (estado == null) {
            estado = "Pendiente";
        }
        if (activo == null) {
            activo = true;
        }
        if (manoObra == null) {
            manoObra = BigDecimal.ZERO;
        }
        if (totalRepuestos == null) {
            totalRepuestos = BigDecimal.ZERO;
        }
        if (total == null) {
            total = BigDecimal.ZERO;
        }
    }

    public void calcularTotal() {
        if (manoObra == null) {
            manoObra = BigDecimal.ZERO;
        }
        if (totalRepuestos == null) {
            totalRepuestos = BigDecimal.ZERO;
        }
        this.total = manoObra.add(totalRepuestos);
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumeroOrden() { return numeroOrden; }
    public void setNumeroOrden(String numeroOrden) { this.numeroOrden = numeroOrden; }
    public LocalDateTime getFechaApertura() { return fechaApertura; }
    public void setFechaApertura(LocalDateTime fechaApertura) { this.fechaApertura = fechaApertura; }
    public LocalDateTime getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(LocalDateTime fechaCierre) { this.fechaCierre = fechaCierre; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getProblemaReportado() { return problemaReportado; }
    public void setProblemaReportado(String problemaReportado) { this.problemaReportado = problemaReportado; }
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public String getTrabajoRealizado() { return trabajoRealizado; }
    public void setTrabajoRealizado(String trabajoRealizado) { this.trabajoRealizado = trabajoRealizado; }
    public BigDecimal getManoObra() { return manoObra; }
    public void setManoObra(BigDecimal manoObra) { this.manoObra = manoObra; }
    public BigDecimal getTotalRepuestos() { return totalRepuestos; }
    public void setTotalRepuestos(BigDecimal totalRepuestos) { this.totalRepuestos = totalRepuestos; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Vehiculo getVehiculo() { return vehiculo; }
    public void setVehiculo(Vehiculo vehiculo) { this.vehiculo = vehiculo; }
    public Tecnico getTecnico() { return tecnico; }
    public void setTecnico(Tecnico tecnico) { this.tecnico = tecnico; }
    public Diagnostico getDiagnosticoEntity() { return diagnosticoEntity; }
    public void setDiagnosticoEntity(Diagnostico diagnosticoEntity) { this.diagnosticoEntity = diagnosticoEntity; }
    public List<OrdenRepuesto> getRepuestosUtilizados() { return repuestosUtilizados; }
    public void setRepuestosUtilizados(List<OrdenRepuesto> repuestosUtilizados) { this.repuestosUtilizados = repuestosUtilizados; }
}