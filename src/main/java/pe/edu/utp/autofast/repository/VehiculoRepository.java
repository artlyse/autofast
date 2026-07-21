package pe.edu.utp.autofast.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.autofast.entity.Vehiculo;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    
    Optional<Vehiculo> findByPlaca(String placa);
    
    List<Vehiculo> findByClienteIdAndActivoTrue(Long clienteId);
    
    List<Vehiculo> findByActivoTrueOrderByMarcaAsc();
    
    boolean existsByPlaca(String placa);
}