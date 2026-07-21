package pe.edu.utp.autofast.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.utp.autofast.entity.Repuesto;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepuestoRepository extends JpaRepository<Repuesto, Long> {
    
    Optional<Repuesto> findByCodigo(String codigo);
    
    List<Repuesto> findByActivoTrueOrderByNombreAsc();
    
    @Query("SELECT r FROM Repuesto r WHERE r.stockActual <= r.stockMinimo AND r.activo = true")
    List<Repuesto> findRepuestosConStockBajo();
    
    List<Repuesto> findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(String nombre, String codigo);
    
    boolean existsByCodigo(String codigo);
}