package pe.edu.utp.autofast.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.autofast.entity.OrdenRepuesto;

@Repository
public interface OrdenRepuestoRepository extends JpaRepository<OrdenRepuesto, Long> {
}