package pe.edu.utp.autofast.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.autofast.entity.Tecnico;

import java.util.List;
import java.util.Optional;

@Repository
public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {
    List<Tecnico> findByActivoTrueOrderByNombreAsc();
    
    // Buscar técnico por email (para sincronización)
    Optional<Tecnico> findByEmail(String email);
}
