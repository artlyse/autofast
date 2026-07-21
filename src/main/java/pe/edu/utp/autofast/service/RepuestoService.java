package pe.edu.utp.autofast.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.utp.autofast.entity.Repuesto;
import pe.edu.utp.autofast.repository.RepuestoRepository;

@Service
public class RepuestoService {

    @Autowired
    private RepuestoRepository repuestoRepository;

    public List<Repuesto> findAll() {
        return repuestoRepository.findByActivoTrueOrderByNombreAsc();
    }

    public Optional<Repuesto> findById(Long id) {
        return repuestoRepository.findById(id);
    }

    public Optional<Repuesto> findByCodigo(String codigo) {
        return repuestoRepository.findByCodigo(codigo);
    }

    public List<Repuesto> findStockBajo() {
        return repuestoRepository.findRepuestosConStockBajo();
    }

    public List<Repuesto> search(String term) {
        return repuestoRepository.findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(term, term);
    }

    @Transactional
    public Repuesto save(Repuesto repuesto) {
        return repuestoRepository.save(repuesto);
    }

    @Transactional
    public Repuesto update(Repuesto repuesto) {
        return repuestoRepository.save(repuesto);
    }

    @Transactional
    public void delete(Long id) {
        Repuesto repuesto = repuestoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Repuesto no encontrado"));
        repuesto.setActivo(false);
        repuestoRepository.save(repuesto);
    }

    @Transactional
    public Repuesto descontarStock(Long id, Integer cantidad) {
        Repuesto repuesto = repuestoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Repuesto no encontrado"));
        repuesto.setStockActual(repuesto.getStockActual() - cantidad);
        return repuestoRepository.save(repuesto);
    }

    @Transactional
    public Repuesto agregarStock(Long id, Integer cantidad) {
        Repuesto repuesto = repuestoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Repuesto no encontrado"));
        repuesto.setStockActual(repuesto.getStockActual() + cantidad);
        return repuestoRepository.save(repuesto);
    }

    public boolean existsByCodigo(String codigo) {
        return repuestoRepository.existsByCodigo(codigo);
    }

    public long count() {
        return repuestoRepository.count();
    }
}