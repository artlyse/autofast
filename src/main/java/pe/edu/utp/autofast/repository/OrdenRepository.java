package pe.edu.utp.autofast.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.utp.autofast.entity.OrdenServicio;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenRepository extends JpaRepository<OrdenServicio, Long> {
    
    Optional<OrdenServicio> findByNumeroOrden(String numeroOrden);
    
    List<OrdenServicio> findByClienteId(Long clienteId);
    
    List<OrdenServicio> findByEstadoOrderByFechaAperturaDesc(String estado);
    
    List<OrdenServicio> findByActivoTrueOrderByFechaAperturaDesc();
    
    @Query("SELECT o FROM OrdenServicio o WHERE o.estado NOT IN ('Finalizado', 'Cancelado') ORDER BY o.fechaApertura DESC")
    List<OrdenServicio> findOrdenesAbiertas();
    
    long countByEstado(String estado);
}