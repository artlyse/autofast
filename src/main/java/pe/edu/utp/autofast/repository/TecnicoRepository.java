package pe.edu.utp.autofast.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.autofast.entity.Tecnico;

import java.util.List;

@Repository
public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {
    
    List<Tecnico> findByActivoTrueOrderByNombreAsc();
}