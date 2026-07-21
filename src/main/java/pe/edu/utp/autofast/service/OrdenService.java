package pe.edu.utp.autofast.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.utp.autofast.entity.Diagnostico;
import pe.edu.utp.autofast.entity.OrdenRepuesto;
import pe.edu.utp.autofast.entity.OrdenServicio;
import pe.edu.utp.autofast.entity.Repuesto;
import pe.edu.utp.autofast.repository.OrdenRepository;
import pe.edu.utp.autofast.repository.OrdenRepuestoRepository;

@Service
public class OrdenService {

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private OrdenRepuestoRepository ordenRepuestoRepository;

    @Autowired
    private RepuestoService repuestoService;

    public List<OrdenServicio> findAll() {
        return ordenRepository.findByActivoTrueOrderByFechaAperturaDesc();
    }

    public List<OrdenServicio> findOrdenesAbiertas() {
        return ordenRepository.findOrdenesAbiertas();
    }

    public Optional<OrdenServicio> findById(Long id) {
        return ordenRepository.findById(id);
    }

    public Optional<OrdenServicio> findByNumeroOrden(String numeroOrden) {
        return ordenRepository.findByNumeroOrden(numeroOrden);
    }

    public List<OrdenServicio> findByCliente(Long clienteId) {
        return ordenRepository.findByClienteId(clienteId);
    }

    public List<OrdenServicio> findByEstado(String estado) {
        return ordenRepository.findByEstadoOrderByFechaAperturaDesc(estado);
    }

    public long countByEstado(String estado) {
        return ordenRepository.countByEstado(estado);
    }

    @Transactional
    public OrdenServicio save(OrdenServicio orden) {
        if (orden.getNumeroOrden() == null || orden.getNumeroOrden().isEmpty()) {
            orden.setNumeroOrden(generarNumeroOrden());
        }
        orden.setEstado("Pendiente");
        return ordenRepository.save(orden);
    }

    @Transactional
    public OrdenServicio update(OrdenServicio orden) {
        return ordenRepository.save(orden);
    }

    @Transactional
    public OrdenServicio actualizarEstado(Long id, String estado) {
        OrdenServicio orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        orden.setEstado(estado);
        if ("Finalizado".equals(estado)) {
            orden.setFechaCierre(LocalDateTime.now());
        }
        return ordenRepository.save(orden);
    }

    @Transactional
    public OrdenServicio agregarRepuesto(Long ordenId, Long repuestoId, Integer cantidad) {
        OrdenServicio orden = ordenRepository.findById(ordenId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        Repuesto repuesto = repuestoService.findById(repuestoId)
                .orElseThrow(() -> new RuntimeException("Repuesto no encontrado"));

        if (!repuesto.tieneStockDisponible(cantidad)) {
            throw new RuntimeException("Stock insuficiente");
        }

        OrdenRepuesto ordenRepuesto = new OrdenRepuesto();
        ordenRepuesto.setOrdenServicio(orden);
        ordenRepuesto.setRepuesto(repuesto);
        ordenRepuesto.setCantidad(cantidad);
        ordenRepuesto.setPrecioUnitario(repuesto.getPrecioVenta());
        ordenRepuesto.calcularSubtotal();

        ordenRepuestoRepository.save(ordenRepuesto);
        repuestoService.descontarStock(repuestoId, cantidad);

        // Recalcular total de repuestos
        BigDecimal totalRepuestos = orden.getRepuestosUtilizados().stream()
                .map(OrdenRepuesto::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        orden.setTotalRepuestos(totalRepuestos);
        orden.calcularTotal();

        return ordenRepository.save(orden);
    }

    @Transactional
    public OrdenServicio registrarDiagnostico(Long id, String diagnostico, String recomendaciones) {
        OrdenServicio orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        orden.setDiagnostico(diagnostico);
        orden.setEstado("Diagnosticado");

        Diagnostico diag = new Diagnostico();
        diag.setOrdenServicio(orden);
        diag.setDescripcion(diagnostico);
        diag.setRecomendaciones(recomendaciones);
        return ordenRepository.save(orden);
    }

    @Transactional
    public OrdenServicio registrarTrabajo(Long id, String trabajoRealizado, BigDecimal manoObra) {
        OrdenServicio orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        orden.setTrabajoRealizado(trabajoRealizado);
        orden.setManoObra(manoObra);
        orden.calcularTotal();
        orden.setEstado("En reparación");
        return ordenRepository.save(orden);
    }

    private String generarNumeroOrden() {
        String año = String.valueOf(LocalDateTime.now().getYear());
        String mes = String.format("%02d", LocalDateTime.now().getMonthValue());
        long count = ordenRepository.count() + 1;
        return "OS-" + año + mes + "-" + String.format("%03d", count);
    }

    public long count() {
        return ordenRepository.count();
    }
}